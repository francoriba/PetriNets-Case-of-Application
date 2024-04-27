package threads;

import source.Constants;

public class Packager extends Worker implements Runnable{
    public Packager(){
        Integer[] transitions = new Integer[]{7}; // T8
        setTransitions(transitions);
    }
    @Override
    public void run() {
        Thread.currentThread().setName("Packager");
        doTask();
    }
}
