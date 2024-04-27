package threads;

import source.Constants;

public class ForkMaker extends Worker implements Runnable{
    public ForkMaker(){
        Integer[] transitions = new Integer[]{1, 3, 5}; // T2, T4, T6
        setTransitions(transitions);
    }
    @Override
    public void run() {
        Thread.currentThread().setName("ForkMaker");
        doTask();
    }
}
