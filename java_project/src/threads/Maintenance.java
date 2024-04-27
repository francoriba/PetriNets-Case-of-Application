package threads;

import source.Constants;

public class Maintenance extends Worker implements Runnable{
    public Maintenance(){
        Integer[] transitions = new Integer[]{8, 9, 10, 11}; // T9, T10, T11, T12
        setTransitions(transitions);
    }
    @Override
    public void run() {
        Thread.currentThread().setName("Maintenance");
        doTask();
    }
}
