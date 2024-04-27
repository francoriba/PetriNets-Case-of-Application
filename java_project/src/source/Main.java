package source;

import java.util.ArrayList;

import threads.*;
import logs.Log;

public class Main {
    private static final ArrayList<Runnable> threads = new ArrayList<>();
    public static boolean isRunning = true;
    public static final Monitor monitor = Monitor.getInstance();

    public static void main(String[] args){
        Thread logThread = new Thread(new Log());
        logThread.start();

        createThreads();
        startThreads();
    }

    /**
     * Create the specified amount of each thread
     * **/
    private static void createThreads(){
        for(int i = 0; i < Constants.MetalCutterAmount; i++){
            threads.add(new MetalCutter());
        }
        for(int i = 0; i < Constants.KnifeMakerAmount; i++){
            threads.add(new KnifeMaker());
        }
        for(int i = 0; i < Constants.ForkMakerAmount; i++){
            threads.add(new ForkMaker());
        }
        for(int i = 0; i < Constants.MaintenanceAmount; i++){
            threads.add(new Maintenance());
        }
        for(int i = 0; i < Constants.PackagerAmount; i++){
            threads.add(new Packager());
        }
    }

    /**
     * Start all threads from the ArrayList threads
     * **/
    private static void startThreads(){
        for (Runnable thread : threads) {
            new Thread(thread).start();
        }
    }

    /**
     * Stop running the program, set isRunning variable to false
     * **/
    public static void stopRunning(){
        isRunning = false;
    }

}