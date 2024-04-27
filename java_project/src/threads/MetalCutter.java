package threads;

import source.Constants;

public class MetalCutter extends Worker implements Runnable{
    public MetalCutter(){
        Integer[] transitions = new Integer[]{0}; // T1
        setTransitions(transitions);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MetalCutter");
        doTask();

    }
}
