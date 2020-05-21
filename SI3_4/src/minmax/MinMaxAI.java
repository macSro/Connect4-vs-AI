package minmax;


import game.Board;
import game.GameSettings;
import game.Move;

import java.util.ArrayList;
import java.util.Random;


public class MinMaxAI {

	private int maxDepth;

	private int aiPlayer;

	public MinMaxAI(int maxDepth, int aiPlayer) {
		this.maxDepth = maxDepth;
		this.aiPlayer = aiPlayer;
	}

	public int getAiPlayer() {
			return aiPlayer;
		}

	public Move minMax(Board board) {
		if (aiPlayer == GameSettings.PLAYER_1) {
	 	    return max(new Board(board), 0);
	 	}
	 	else {
	 	    return min(new Board(board), 0);
	 	}
	}

	public Move max(Board board, int depth) {
	    Random r = new Random();

		if((board.checkForGameOver()) || (depth == maxDepth)) {
			Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.evaluate());
			return lastMove;
		}
		ArrayList<Board> children = new ArrayList<>(board.getChildrenStates(GameSettings.PLAYER_1));
		Move maxMove = new Move(Integer.MIN_VALUE);
		for (Board child : children) {
			Move move = min(child, depth + 1);
			if (move.getValue() >= maxMove.getValue()) {
	            if (GameSettings.AI_HEURISTIC == GameSettings.HEURISTIC_RANDOM_EQUAL && move.getValue() == maxMove.getValue()) {
	                if (r.nextInt(2) == 0) {
	                    maxMove.setRow(child.getLastMove().getRow());
	                    maxMove.setColumn(child.getLastMove().getColumn());
	                    maxMove.setValue(move.getValue());
	                }
	            }
	            else {
	                maxMove.setRow(child.getLastMove().getRow());
	                maxMove.setColumn(child.getLastMove().getColumn());
	                maxMove.setValue(move.getValue());
	            }
			}
		}
		return maxMove;
	}

	public Move min(Board board, int depth) {
	    Random r = new Random();

		if((board.checkForGameOver()) || (depth == maxDepth)) {
			Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.evaluate());
			return lastMove;
		}
		ArrayList<Board> children = new ArrayList<>(board.getChildrenStates(GameSettings.PLAYER_2));
		Move minMove = new Move(Integer.MAX_VALUE);
		for (Board child : children) {
			Move move = max(child, depth + 1);
			if(move.getValue() <= minMove.getValue()) {
	            if (GameSettings.AI_HEURISTIC == GameSettings.HEURISTIC_RANDOM_EQUAL && move.getValue() == minMove.getValue()) {
	                if (r.nextInt(2) == 0) {
	                    minMove.setRow(child.getLastMove().getRow());
	                    minMove.setColumn(child.getLastMove().getColumn());
	                    minMove.setValue(move.getValue());
	                }
	            }
	            else {
	            	minMove.setRow(child.getLastMove().getRow());
	            	minMove.setColumn(child.getLastMove().getColumn());
	            	minMove.setValue(move.getValue());
	            }
	        }
	    }
	    return minMove;
	}

	public Move minMaxAlphaBeta(Board board) {
		if (aiPlayer == GameSettings.PLAYER_1) {
			return maxAlphaBeta(new Board(board), 0, Double.MIN_VALUE, Double.MAX_VALUE);
		}
		else {
			return minAlphaBeta(new Board(board), 0, Double.MIN_VALUE, Double.MAX_VALUE);
		}
	}

	public Move maxAlphaBeta(Board board, int depth, double a, double b) {
		Random r = new Random();

		if((board.checkForGameOver()) || (depth == maxDepth)) {
			Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.evaluate());
			return lastMove;
		}
		ArrayList<Board> children = new ArrayList<>(board.getChildrenStates(GameSettings.PLAYER_1));
		Move maxMove = new Move(Integer.MIN_VALUE);
		for (Board child : children) {
			Move move = minAlphaBeta(child, depth + 1, a, b);
			if (move.getValue() >= maxMove.getValue()) {
				if (GameSettings.AI_HEURISTIC == GameSettings.HEURISTIC_RANDOM_EQUAL && move.getValue() == maxMove.getValue()) {
					if (r.nextInt(2) == 0) {
						maxMove.setRow(child.getLastMove().getRow());
						maxMove.setColumn(child.getLastMove().getColumn());
						maxMove.setValue(move.getValue());
					}
				}
				else {
					maxMove.setRow(child.getLastMove().getRow());
					maxMove.setColumn(child.getLastMove().getColumn());
					maxMove.setValue(move.getValue());
				}
			}

			a = (a > maxMove.getValue()) ? a : maxMove.getValue();

			if(a>=b) return maxMove;


		}
		return maxMove;
	}

	public Move minAlphaBeta(Board board, int depth, double a, double b) {
		Random r = new Random();

		if((board.checkForGameOver()) || (depth == maxDepth)) {
			Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.evaluate());
			return lastMove;
		}
		ArrayList<Board> children = new ArrayList<Board>(board.getChildrenStates(GameSettings.PLAYER_2));
		Move minMove = new Move(Integer.MAX_VALUE);
		for (Board child : children) {
			Move move = maxAlphaBeta(child, depth + 1, a, b);
			if(move.getValue() <= minMove.getValue()) {
				if (GameSettings.AI_HEURISTIC == GameSettings.HEURISTIC_RANDOM_EQUAL && move.getValue() == minMove.getValue()) {
					if (r.nextInt(2) == 0) {
						minMove.setRow(child.getLastMove().getRow());
						minMove.setColumn(child.getLastMove().getColumn());
						minMove.setValue(move.getValue());
					}
				}
				else {
					minMove.setRow(child.getLastMove().getRow());
					minMove.setColumn(child.getLastMove().getColumn());
					minMove.setValue(move.getValue());
				}
			}

			b = (b < minMove.getValue()) ? b : minMove.getValue();

			if(a>=b) return minMove;
		}
		return minMove;
	}
}
