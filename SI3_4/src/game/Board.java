package game;


import java.util.ArrayList;


public class Board {
	
	static final int numOfRows = GameSettings.NUMBER_OF_ROWS;
	static final int numOfColumns = GameSettings.NUMBER_OF_COLUMNS;
	static final int winningLength = GameSettings.WINNING_LENGTH;

    private Move lastMove;
    private int lastPlayer;

    public boolean isFirstMove = true;
    
    private int winner;
	private int [][] gameboard;
	
	private boolean overflow;
		
	private boolean gameOver;
	
	private int turn;

	public Board() {
		this.lastMove = new Move();
		this.lastPlayer = GameSettings.PLAYER_2;
		this.winner = GameSettings.EMPTY;
		this.gameboard = new int[7][8];
		this.overflow = false;
		this.gameOver = false;
		this.turn = 0;
		for(int i=0; i<numOfRows; i++) {
			for(int j=0; j<numOfColumns; j++) {
				gameboard[i][j] = GameSettings.EMPTY;
			}
		}
	}

	public Board(Board board) {
		lastMove = board.getLastMove();
		lastPlayer = board.getLastPlayer();
		winner = board.getWinner();
		
		this.overflow = board.isOverflow();
		this.gameOver = board.isGameOver();
		this.turn = board.getTurn();
		
		int N1 = board.getGameboard().length;
		int N2 = board.getGameboard()[0].length;
		this.gameboard = new int[N1][N2];
		for(int i=0; i<N1; i++) {
			for(int j=0; j<N2; j++) {
				this.gameboard[i][j] = board.getGameboard()[i][j];
			}
		}
	}

	public void makeMove(int col, int player) {
		try {
			this.lastMove = new Move(getEmptyRowPosition(col), col);
			this.lastPlayer = player;
			this.gameboard[getEmptyRowPosition(col)][col] = player;
			this.turn++;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Column " + (col+1) + " is full!");
			setOverflow(true);
		}
	}

	public boolean canMove(int row, int col) {
		if ((row <= -1) || (col <= -1) || (row > numOfRows-1) || (col > numOfColumns-1)) {
			return false;
		}
		return true;
	}
	
	public boolean checkFullColumn(int col) {
		if (gameboard[0][col] == GameSettings.EMPTY)
			return false;
		return true;
	}

	public int getEmptyRowPosition(int col) {
		int rowPosition = -1;
		for (int row=0; row<numOfRows; row++) {
			if (gameboard[row][col] == GameSettings.EMPTY) {
				rowPosition = row;
			}
		}
		return rowPosition;
	}

	public ArrayList<Board> getChildrenStates(int player) {
		ArrayList<Board> children = new ArrayList<>();
		for(int col=0; col<numOfColumns; col++) {
			if(!checkFullColumn(col)) {
				Board child = new Board(this);
				child.makeMove(col, player);
				children.add(child);
			}
		}
		return children;
	}

	public int evaluate() {
		int player1Score = 0;
		int player2Score = 0;
		
		if (checkWinState()) {
			if (winner == GameSettings.PLAYER_1)
				player1Score = (int) Math.pow(10, (winningLength -2));
			else if (winner == GameSettings.PLAYER_2)
				player2Score = (int) Math.pow(10, (winningLength -2));
		}
		
		for (int i = 0; i< winningLength -2; i++) {
	        player1Score += countNInARow(i+2, GameSettings.PLAYER_1) * Math.pow(10, i);
	        player2Score += countNInARow(i+2, GameSettings.PLAYER_2) * Math.pow(10, i);
		}

		return player1Score - player2Score;
	}

	public boolean checkWinState() {
		
		int times4InARowPlayer1 = countNInARow(winningLength, GameSettings.PLAYER_1);
		if (times4InARowPlayer1 > 0) {
			setWinner(GameSettings.PLAYER_1);
			return true;
		}
		
		int times4InARowPlayer2 = countNInARow(winningLength, GameSettings.PLAYER_2);
		if (times4InARowPlayer2 > 0) {
			setWinner(GameSettings.PLAYER_2);
			return true;
		}
		
		setWinner(GameSettings.EMPTY);
		return false;
	}
	
    public boolean checkForGameOver() {
    	if (checkWinState()) {
    		return true;
    	}
    	
    	return checkForDraw();
    }

    public boolean checkForDraw() {
    	if (gameOver)
    		return false;
    	for(int row=0; row<numOfRows; row++) {
			for(int col=0; col<numOfColumns; col++) {
				if(gameboard[row][col] == GameSettings.EMPTY) {
                    return false;
                }
            }
        }
    	return true;
    }

	public int countNInARow(int N, int player) {
		int times = 0;
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<numOfColumns; j++) {
				if (canMove(i, j+ winningLength -1)) {
					int k = 0;
					while (k < N && gameboard[i][j+k] == player) {
						k++;
					}
					if (k==N) {
						while (k < winningLength && (gameboard[i][j+k] == player || gameboard[i][j+k] == GameSettings.EMPTY)) {
							k++;
						}
						if (k== winningLength) times++;
					}
					
				}
			}
		}
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<numOfColumns; j++) {
				if (canMove(i- winningLength +1, j)) {
					int k = 0;
					while (k < N && gameboard[i-k][j] == player) {
						k++;
					}
					if (k== winningLength) {
						while (k < winningLength && (gameboard[i-k][j] == player || gameboard[i-k][j] == GameSettings.EMPTY)) {
							k++;
						}
						if (k== winningLength) times++;
					}
					
				}
			}
		}
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<numOfColumns; j++) {
				if (canMove(i+ winningLength -1, j+ winningLength -1)) {
					int k = 0;
					while (k < N && gameboard[i+k][j+k] == player) {
						k++;
					}
					if (k== winningLength) {
						while (k < winningLength && (gameboard[i+k][j+k] == player || gameboard[i+k][j+k] == GameSettings.EMPTY)) {
							k++;
						}
						if (k== winningLength) times++;
					}
					
				}
			}
		}

		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<numOfColumns; j++) {
				if (canMove(i- winningLength +1, j+ winningLength -1)) {
					int k = 0;
					while (k < N && gameboard[i-k][j+k] == player) {
						k++;
					}
					if (k== winningLength) {
						while (k < winningLength && (gameboard[i-k][j+k] == player || gameboard[i-k][j+k] == GameSettings.EMPTY)) {
							k++;
						}
						if (k== winningLength) times++;
					}
				}
			}
		}
		return times;
	}

	public Move getLastMove() {
		return lastMove;
	}

	public int getLastPlayer() {
		return lastPlayer;
	}

	public void setLastPlayer(int lastPlayer) {
		this.lastPlayer = lastPlayer;
	}

	public int[][] getGameboard() {
		return gameboard;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int getTurn() {
		return turn;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.gameOver = isGameOver;
	}

	public boolean isOverflow() {
		return overflow;
	}

	public void setOverflow(boolean overflow) {
		this.overflow = overflow;
	}


}
