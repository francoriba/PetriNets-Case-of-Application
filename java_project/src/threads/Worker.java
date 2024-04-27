package threads;

import java.util.concurrent.TimeUnit;
import static source.Main.isRunning;
import static source.Main.monitor;

public abstract class Worker {
    protected Integer[] transitions;

    /**
     * Set fireble transitions by the Worker
     * **/
    protected void setTransitions(Integer[] transitions) {
        this.transitions = transitions;
    }

    /**
     * Worker performs a task
     * **/
    protected void doTask(){
        while (isRunning){
            for(Integer t: transitions){
                monitor.callMonitor(t);
            }
        }
    }
}
