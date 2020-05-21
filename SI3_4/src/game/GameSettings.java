package game;

public class GameSettings {
	public static final int NUMBER_OF_ROWS = 6;
	public static final int NUMBER_OF_COLUMNS = 7;
	public static final int WINNING_LENGTH = 4;
	public static final int MAX_DEPTH_TOTAL = 10;

	public static final int EMPTY = 0;
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	public static final int USER_VS_AI = 1;
	public static final int AI_VS_AI = 2;
	public static final int USER_VS_USER = 3;

	public static final int HEURISTIC_NONE = 0;
	public static final int HEURISTIC_RANDOM_EQUAL = 1;
	public static final int HEURISTIC_MIDDLE_FIRST = 2;

	public static int AI_HEURISTIC = HEURISTIC_MIDDLE_FIRST;

	public static int GAME_MODE = USER_VS_AI;
	public static boolean ALPHA_BETA = false;
	public static int MAX_DEPTH_AI_1 = 3;
	public static int MAX_DEPTH_AI_2 = 3;
	public static int MAX_BENCHMARK_ITERATIONS = 50;
	public static int BENCHMARK_ITERATIONS = 5;
}
