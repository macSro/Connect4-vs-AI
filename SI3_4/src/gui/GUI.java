package gui;


import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import game.Board;
import game.GameSettings;
import game.Move;
import minmax.*;


public class GUI {

	final int numOfRows = GameSettings.NUMBER_OF_ROWS;
	final int numOfColumns = GameSettings.NUMBER_OF_COLUMNS;
	final int inARow = GameSettings.WINNING_LENGTH;

	Board board;
	JFrame frameMainWindow;

	JPanel panelMain;
	JPanel panelBoardNumbers;
	JLayeredPane gameboard;

	final int DEFAULT_WIDTH = 570;
	final int DEFAULT_HEIGHT = 525;

	JButton[] buttons;

	JLabel turnMessage;

	MinMaxAI ai;

	boolean benchmarkEnabled = false;
	int iterations = GameSettings.BENCHMARK_ITERATIONS;
	LocalTime timeStart;
	LocalTime timeFinish;
	int totalWinnerMoves;
	int totalDraws;

	public GUI() {
		buttons = new JButton[numOfColumns];
		for (int i=0; i<numOfColumns; i++) {
			buttons[i] = new JButton(i+1+"");
			buttons[i].setFocusable(false);
		}
	}

	public static void main(String[] args){
		new GUI().createNewGame();
	}

	public void createNewGame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (GameSettings.GAME_MODE != GameSettings.AI_VS_AI) {
			setAllButtonsEnabled(true);
		}

		board = new Board();

		if (frameMainWindow != null) frameMainWindow.dispose();
		frameMainWindow = new JFrame("Connect 4 - MinMax");

		centerWindow(frameMainWindow, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Component compMainWindowContents = createComponents();
		frameMainWindow.getContentPane().add(compMainWindowContents, BorderLayout.CENTER);

		frameMainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		if (frameMainWindow.getKeyListeners().length == 0) {
			frameMainWindow.addKeyListener(gameKeyListener);
		}

		frameMainWindow.setFocusable(true);
		frameMainWindow.pack();

		JToolBar tools = new JToolBar();
		tools.setBackground(new Color(245, 210, 129));
		turnMessage = new JLabel("Turn: " + board.getTurn());
		tools.add(turnMessage);

		tools.setFloatable(false);
		frameMainWindow.add(tools, BorderLayout.PAGE_END);

		frameMainWindow.setVisible(true);

		if (GameSettings.GAME_MODE == GameSettings.USER_VS_AI) {
			ai = new MinMaxAI(GameSettings.MAX_DEPTH_AI_1, GameSettings.PLAYER_2);
		}
		else if (GameSettings.GAME_MODE == GameSettings.AI_VS_AI) {
			setAllButtonsEnabled(false);

			MinMaxAI ai1 = new MinMaxAI(GameSettings.MAX_DEPTH_AI_1, GameSettings.PLAYER_1);
			MinMaxAI ai2 = new MinMaxAI(GameSettings.MAX_DEPTH_AI_2, GameSettings.PLAYER_2);

			while (!board.isGameOver()) {
				aiMove(ai1);

				if (!board.isGameOver()) {
					aiMove(ai2);
				}
			}
		}
	}

	public  Component createComponents() {

		panelBoardNumbers = new JPanel();
		panelBoardNumbers.setLayout(new GridLayout(1, numOfColumns, numOfRows, 4));
		panelBoardNumbers.setBorder(BorderFactory.createEmptyBorder(2, 22, 2, 22));
		panelBoardNumbers.setBackground(new Color(245, 210, 129));

		for (JButton button: buttons) {
			button.setBackground(new Color(185,122,87));
			button.setForeground(Color.BLACK);
			button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			button.setBorder(new LineBorder(Color.BLACK, 2));
			panelBoardNumbers.add(button);
		}

		gameboard = createGameboard();

		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		panelMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		panelMain.add(panelBoardNumbers, BorderLayout.NORTH);
		panelMain.add(gameboard, BorderLayout.CENTER);

		panelMain.setBackground(new Color(245, 210, 129));

		JPanel settingsPanel = new JPanel();
		settingsPanel.setBackground(new Color(245, 210, 129));

		JButton newGameButton = new JButton("New Game");

		newGameButton.addActionListener(e ->{
			if(benchmarkEnabled){
				benchmarkEnabled = false;
				GameSettings.GAME_MODE = GameSettings.USER_VS_AI;
			}
			createNewGame();
		});

		settingsPanel.add(newGameButton);

		JButton settingsButton = new JButton("Settings");

		settingsButton.addActionListener(e -> {
			SettingsWindow settings = new SettingsWindow();
			settings.setVisible(true);
		});

		settingsPanel.add(settingsButton);

		JButton rulesButton = new JButton("Rules");

		rulesButton.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"Each turn you get to pick one column to drop a disc in. You can't drop a disk in a full column."
						+"\nThe first person to form a horizontal, vertical, or diagonal line of " + inARow + " discs wins!"
						+"\nUse the buttons or the number keys on your keyboard to select a column.",
				"Rules", JOptionPane.QUESTION_MESSAGE));

		settingsPanel.add(rulesButton);

		JButton controlsButton = new JButton("Controls");

		controlsButton.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"Select the column: number buttons above the board OR 1-" + numOfColumns + " keys."
						+"\nRestart game: R",
				"Controls", JOptionPane.INFORMATION_MESSAGE));

		settingsPanel.add(controlsButton);

		JButton benchmarkButton = new JButton("Benchmark");

		benchmarkButton.addActionListener(e -> {
			GameSettings.GAME_MODE = GameSettings.AI_VS_AI;
			iterations = GameSettings.BENCHMARK_ITERATIONS;
			benchmarkEnabled = true;
			totalWinnerMoves=0;
			totalDraws=0;
			timeStart = LocalTime.now();
			for (int i = 0; i < iterations; i++) {
				createNewGame();
			}
			timeFinish = LocalTime.now();

			Utility.saveTXT(Duration.between(timeStart, timeFinish).toMillis(), Duration.between(timeStart, timeFinish).toMillis()/iterations, iterations==totalDraws?0:totalWinnerMoves/(iterations-totalDraws));
			System.out.println("Finished");
		});

		settingsPanel.add(benchmarkButton);

		panelMain.add(settingsPanel, BorderLayout.SOUTH);

		frameMainWindow.setResizable(false);
		return panelMain;
	}

	public JLayeredPane createGameboard() {
		gameboard = new JLayeredPane();
		gameboard.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		ImageIcon imageBoard = new ImageIcon("res/images/Board_paint.jpg");
		JLabel imageBoardLabel = new JLabel(imageBoard);

		imageBoardLabel.setBounds(19, 5, imageBoard.getIconWidth(), imageBoard.getIconHeight());
		gameboard.add(imageBoardLabel, 0, 1);

		return gameboard;
	}

	public void centerWindow(Window frame, int width, int height) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (dimension.getWidth() - frame.getWidth() - width) / 2;
		int y = (int) (dimension.getHeight() - frame.getHeight() - height) / 2;
		frame.setLocation(x, y);
	}

	public KeyListener gameKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			String keyText = KeyEvent.getKeyText(e.getKeyCode());

			if(keyText.equals("R")){
				if(benchmarkEnabled){
					benchmarkEnabled = false;
					GameSettings.GAME_MODE = GameSettings.USER_VS_AI;
				}
				createNewGame();
			}

			for (int i = 0; i<GameSettings.NUMBER_OF_COLUMNS; i++) {
				if (keyText.equals(i+1+"")) {
					makeMove(i);

					if (!board.isOverflow()) {
						boolean isGameOver = game();
						if (GameSettings.GAME_MODE == GameSettings.USER_VS_AI && !isGameOver) {
							aiMove(ai);
						}
					}
					break;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {}
	};

	public void setAllButtonsEnabled(boolean b) {
		if (b) {

			for (int i=0; i<buttons.length; i++) {
				JButton button = buttons[i];
				int column = i;

				if (button.getActionListeners().length == 0) {
					button.addActionListener(e -> {
						makeMove(column);

						if (!board.isOverflow()) {
							boolean isGameOver = game();
							if (GameSettings.GAME_MODE == GameSettings.USER_VS_AI && !isGameOver) {
								aiMove(ai);
							}
						}
						frameMainWindow.requestFocusInWindow();
					});
				}
			}

		} else {

			for (JButton button: buttons) {
				for (ActionListener actionListener: button.getActionListeners()) {
					button.removeActionListener(actionListener);
				}
			}

		}
	}

	public void makeMove(int col) {
		board.setOverflow(false);

		int previousRow = board.getLastMove().getRow();
		int previousCol = board.getLastMove().getColumn();
		int previousLetter = board.getLastPlayer();

		if (board.getLastPlayer() == GameSettings.PLAYER_2) {
			board.makeMove(col, GameSettings.PLAYER_1);
		} else {
			board.makeMove(col, GameSettings.PLAYER_2);
		}
		if (board.isOverflow()) {
			board.getLastMove().setRow(previousRow);
			board.getLastMove().setColumn(previousCol);
			board.setLastPlayer(previousLetter);
		}
	}

	public boolean game() {

		turnMessage.setText("Turn: " + board.getTurn());

		if(!benchmarkEnabled) {
			int row = board.getLastMove().getRow();
			int col = board.getLastMove().getColumn();
			int currentPlayer = board.getLastPlayer();

			if (currentPlayer == GameSettings.PLAYER_1) placeDisc("res/images/RED.png", row, col);
			if (currentPlayer == GameSettings.PLAYER_2) placeDisc("res/images/YELLOW.png", row, col);
		}

		boolean isGameOver = board.checkForGameOver();
		if (isGameOver) {
			gameOver();
		}

		return isGameOver;
	}

	public void placeDisc(String image, int row, int col) {
		int xOffset = 75 * col;
		int yOffset = 75 * row;
		ImageIcon checkerIcon = new ImageIcon(image);

		JLabel checkerLabel = new JLabel(checkerIcon);
		checkerLabel.setBounds(27 + xOffset, 12 + yOffset, checkerIcon.getIconWidth(),checkerIcon.getIconHeight());
		gameboard.add(checkerLabel, 0, 0);

		try {
			if (GameSettings.GAME_MODE == GameSettings.AI_VS_AI) {
				Thread.sleep(200);
				frameMainWindow.paint(frameMainWindow.getGraphics());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void aiMove(MinMaxAI ai){
		if(board.isFirstMove){
			firstMoveAI(ai);
			board.isFirstMove = false;
		}
		else{
			Move aiMove;
			if(GameSettings.ALPHA_BETA) aiMove = ai.minMaxAlphaBeta(board);
			else aiMove = ai.minMax(board);
			board.makeMove(aiMove.getColumn(), ai.getAiPlayer());
			game();
		}
	}

	private void firstMoveAI(MinMaxAI ai){
		if (GameSettings.AI_HEURISTIC == GameSettings.HEURISTIC_MIDDLE_FIRST) {
			board.makeMove(numOfColumns/2, ai.getAiPlayer());
			game();
		}
		else{
			Move aiMove;
			if(GameSettings.ALPHA_BETA) aiMove = ai.minMaxAlphaBeta(board);
			else aiMove = ai.minMax(board);
			board.makeMove(aiMove.getColumn(), ai.getAiPlayer());
			game();
		}
	}

	public void gameOver(){
		board.setGameOver(true);

		if(!benchmarkEnabled) {
			int choice = 0;
			if (board.getWinner() == GameSettings.PLAYER_1) {
				if (GameSettings.GAME_MODE == GameSettings.USER_VS_AI)
					choice = JOptionPane.showConfirmDialog(null,
							"You win! Restart?",
							"Game Over", JOptionPane.YES_NO_OPTION);
				else if (GameSettings.GAME_MODE == GameSettings.AI_VS_AI)
					choice = JOptionPane.showConfirmDialog(null,
							"Minimax AI 1 wins! Start a new game?",
							"Game Over", JOptionPane.YES_NO_OPTION);
				else if (GameSettings.GAME_MODE == GameSettings.USER_VS_USER)
					choice = JOptionPane.showConfirmDialog(null,
							"Player 1 wins! Start a new game?",
							"Game Over", JOptionPane.YES_NO_OPTION);
			} else if (board.getWinner() == GameSettings.PLAYER_2) {
				if (GameSettings.GAME_MODE == GameSettings.USER_VS_AI)
					choice = JOptionPane.showConfirmDialog(null,
							"AI wins! Restart?",
							"Game Over", JOptionPane.YES_NO_OPTION);
				else if (GameSettings.GAME_MODE == GameSettings.AI_VS_AI)
					choice = JOptionPane.showConfirmDialog(null,
							"Minimax AI 2 wins! Start a new game?",
							"Game Over", JOptionPane.YES_NO_OPTION);
				else if (GameSettings.GAME_MODE == GameSettings.USER_VS_USER)
					choice = JOptionPane.showConfirmDialog(null,
							"Player 2 wins! Start a new game?",
							"Game Over", JOptionPane.YES_NO_OPTION);
			} else {
				choice = JOptionPane.showConfirmDialog(null,
						"Draw! Restart?",
						"Game Over", JOptionPane.YES_NO_OPTION);
			}

			setAllButtonsEnabled(false);

			for (KeyListener keyListener : frameMainWindow.getKeyListeners()) {
				frameMainWindow.removeKeyListener(keyListener);
			}

			if (choice == JOptionPane.YES_OPTION) {
				createNewGame();
			}
		}
		else{
			if(board.getWinner() == GameSettings.PLAYER_1) totalWinnerMoves+=(int)Math.ceil((double) board.getTurn()/2);
			else if(board.getWinner() == GameSettings.PLAYER_2) totalWinnerMoves+=board.getTurn()/2;
			else{
				System.out.println("Draw");
				totalDraws++;
			}
		}

	}

}
