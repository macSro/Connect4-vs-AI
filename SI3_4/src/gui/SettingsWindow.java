package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import game.GameSettings;


public class SettingsWindow extends JFrame {

	private JLabel gameModeLabel;
	private JLabel algorithmTypeLabel;
	private JLabel heuristicLabel;
	private JLabel maxDepth1Label;
	private JLabel maxDepth2Label;
	private JLabel iterationsLabel;

	private JComboBox<String> gamemodeDropDown;
	private JComboBox<String> algorithmTypeDropDown;
	private JComboBox<String> heuristicDropDown;
	private JComboBox<Integer> maxDepthAI1DropDown;
	private JComboBox<Integer> maxDepthAI2DropDown;
	private JComboBox<Integer> iterationsDropDown;


	private JButton apply;
	private JButton cancel;
	
	private EventHandler handler;
		
	public static final int width = 400;
	public static final int height = 330;
	
	
	public SettingsWindow() {
		super("Settings");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(width, height);
		setLocationRelativeTo(null);
		setResizable(false);
		
		handler = new EventHandler();

		int selectedMode = GameSettings.GAME_MODE;
		int selectedHeuristic = GameSettings.AI_HEURISTIC;
		int maxDepthAI1 = GameSettings.MAX_DEPTH_AI_1 - 1;
		int maxDepthAI2 = GameSettings.MAX_DEPTH_AI_2 - 1;
		boolean alphaBeta = GameSettings.ALPHA_BETA;
		int iterations = GameSettings.BENCHMARK_ITERATIONS - 1;

		gameModeLabel = new JLabel("Game Mode");
		algorithmTypeLabel = new JLabel("Algorithm Type");
		heuristicLabel = new JLabel("AI Heuristic");
		maxDepth1Label = new JLabel("First AI Depth");
		maxDepth2Label = new JLabel("Second AI Depth (AIvsAI)");
		iterationsLabel = new JLabel("Benchmark Iterations");

		add(gameModeLabel);
		add(algorithmTypeLabel);
		add(heuristicLabel);
		add(maxDepth1Label);
		add(maxDepth2Label);
		add(iterationsLabel);

		gamemodeDropDown = new JComboBox<>();
		gamemodeDropDown.addItem("User vs AI");
		gamemodeDropDown.addItem("AI vs AI");
		gamemodeDropDown.addItem("User vs User");

		if (selectedMode == GameSettings.USER_VS_AI)
			gamemodeDropDown.setSelectedIndex(GameSettings.USER_VS_AI - 1);
		else if (selectedMode == GameSettings.AI_VS_AI)
			gamemodeDropDown.setSelectedIndex(GameSettings.AI_VS_AI - 1);
		else if (selectedMode == GameSettings.USER_VS_USER)
			gamemodeDropDown.setSelectedIndex(GameSettings.USER_VS_USER - 1);

		heuristicDropDown = new JComboBox<>();
		heuristicDropDown.addItem("None");
		heuristicDropDown.addItem("Random Equal");
		heuristicDropDown.addItem("Middle First");

		if(selectedHeuristic == GameSettings.HEURISTIC_NONE)
			heuristicDropDown.setSelectedIndex(GameSettings.HEURISTIC_NONE);
		else if(selectedHeuristic == GameSettings.HEURISTIC_RANDOM_EQUAL)
			heuristicDropDown.setSelectedIndex(GameSettings.HEURISTIC_RANDOM_EQUAL);
		else if (selectedHeuristic == GameSettings.HEURISTIC_MIDDLE_FIRST)
			heuristicDropDown.setSelectedIndex(GameSettings.HEURISTIC_MIDDLE_FIRST);
		
		maxDepthAI1DropDown = new JComboBox<>();
		for (int i = 1; i <= GameSettings.MAX_DEPTH_TOTAL; i++) {
			maxDepthAI1DropDown.addItem(i);
		}

		algorithmTypeDropDown = new JComboBox<>();
		algorithmTypeDropDown.addItem("Min-Max");
		algorithmTypeDropDown.addItem("Min-Max + Alpha Beta");

		maxDepthAI2DropDown = new JComboBox<>();
		for (int i = 1; i <= GameSettings.MAX_DEPTH_TOTAL; i++) {
			maxDepthAI2DropDown.addItem(i);
		}

		iterationsDropDown = new JComboBox<>();
		for (int i = 1; i <= GameSettings.MAX_BENCHMARK_ITERATIONS; i++) {
			iterationsDropDown.addItem(i);
		}
		
		maxDepthAI1DropDown.setSelectedIndex(maxDepthAI1);
		maxDepthAI2DropDown.setSelectedIndex(maxDepthAI2);
		if(alphaBeta) algorithmTypeDropDown.setSelectedItem("Min-Max + Alpha Beta");
		else algorithmTypeDropDown.setSelectedItem("Min-Max");
		iterationsDropDown.setSelectedIndex(iterations);

		add(gamemodeDropDown);
		add(algorithmTypeDropDown);
		add(heuristicDropDown);
		add(maxDepthAI1DropDown);
		add(maxDepthAI2DropDown);
		add(iterationsDropDown);

		gameModeLabel.setBounds(25, 40, 175, 20);
		algorithmTypeLabel.setBounds(25, 70, 175, 20);
		heuristicLabel.setBounds(25, 100, 175, 20);
		maxDepth1Label.setBounds(25, 130, 175, 20);
		maxDepth2Label.setBounds(25, 160, 175, 20);
		iterationsLabel.setBounds(25, 190, 175, 20);

		gamemodeDropDown.setBounds(195, 40, 160, 20);
		algorithmTypeDropDown.setBounds(195, 70, 160, 20);
		heuristicDropDown.setBounds(195, 100, 160, 20);
		maxDepthAI1DropDown.setBounds(195, 130, 160, 20);
		maxDepthAI2DropDown.setBounds(195, 160, 160, 20);
		iterationsDropDown.setBounds(195, 190, 160, 20);

		apply = new JButton("Apply");
		cancel = new JButton("Cancel");
		add(apply);
		add(cancel);
		
		int distance = 10;
		apply.setBounds((width / 2) - 110 - (distance / 2), 250, 100, 30);
		apply.addActionListener(handler);
		cancel.setBounds((width / 2) - 10 + (distance / 2), 250, 100, 30);
		cancel.addActionListener(handler);
	}


	private class EventHandler implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			
			if (ev.getSource() == cancel) {
				dispose();
			}
			
			else if (ev.getSource() == apply) {
				try {

					int gameMode = gamemodeDropDown.getSelectedIndex() + 1;
					String algorithmType = (String) algorithmTypeDropDown.getSelectedItem();
					int heuristic = heuristicDropDown.getSelectedIndex();
					int maxDepth1 = (int) maxDepthAI1DropDown.getSelectedItem();
					int maxDepth2 = (int) maxDepthAI2DropDown.getSelectedItem();
					int iterations = (int) iterationsDropDown.getSelectedItem();

					GameSettings.GAME_MODE = gameMode;
					GameSettings.ALPHA_BETA = algorithmType.equals("Min-Max")?false:true;
					GameSettings.AI_HEURISTIC = heuristic;
					GameSettings.MAX_DEPTH_AI_1 = maxDepth1;
					GameSettings.MAX_DEPTH_AI_2 = maxDepth2;
					GameSettings.BENCHMARK_ITERATIONS = iterations;
					
					JOptionPane.showMessageDialog(null,
							"Game settings have been changed.\nThe changes will be applied in the next new game.",
							"Notice", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
