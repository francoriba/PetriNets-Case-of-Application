package threads;

import source.Constants;

public class KnifeMaker extends Worker implements Runnable{
    public KnifeMaker(){
        Integer[] transtions = new Integer[]{2, 4, 6}; // T3, T5, T7
        setTransitions(transtions);
    }
    @Override
    public void run() {
        Thread.currentThread().setName("KnifeMaker");
        doTask();
    }
}
