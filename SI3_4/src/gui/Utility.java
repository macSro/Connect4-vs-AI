package gui;

import game.GameSettings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Utility {


    public static void saveTXT(long totalTime, long averageTime, int averageWinnerMoves){
        try{
            File file = new File("benchmark/benchmark_result.txt");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write("Benchmark results");
            bw.newLine();
            bw.write("*****************");
            bw.newLine();
            bw.newLine();

            bw.write("Settings:");
            bw.newLine();
            bw.write("Benchmark iterations: " + GameSettings.BENCHMARK_ITERATIONS);
            bw.newLine();
            bw.write("Algorithm used: " + (GameSettings.ALPHA_BETA?"Min-Max + Alpha Beta":"Min-Max"));
            bw.newLine();
            bw.write("Heuristic used: " + (GameSettings.AI_HEURISTIC==GameSettings.HEURISTIC_NONE ?"None":GameSettings.AI_HEURISTIC==GameSettings.HEURISTIC_RANDOM_EQUAL?"Random Equal":"Middle First"));
            bw.newLine();
            bw.write("1st AI depth: " + GameSettings.MAX_DEPTH_AI_1);
            bw.newLine();
            bw.write("2nd AI depth: " + GameSettings.MAX_DEPTH_AI_2);
            bw.newLine();
            bw.newLine();

            bw.write("Total elapsed time: " + (double) totalTime/1000 + "s");
            bw.newLine();
            bw.write("Average time per iteration: " + (double) averageTime/1000 + "s");
            bw.newLine();
            bw.write("Average number of moves made by a winner: " + averageWinnerMoves);

            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
