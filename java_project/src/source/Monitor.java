package source;

import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.Arrays;

public class Monitor {
    private static final PetriNet petriNet = PetriNet.getInstance();
    private static final Politics politics = Politics.getInstance();
    private static Monitor uniqueInstance;
    private static Semaphore mutex;
    private static Semaphore[] conditionQueues;

    private Monitor(){

        mutex = new Semaphore(1);
        conditionQueues = new Semaphore[Constants.TransitionsAmount];
        for (int i = 0; i < Constants.TransitionsAmount; i++) {
            conditionQueues[i] =  new Semaphore(0);
        }

    }

    public static Monitor getInstance(){
        if(uniqueInstance == null){ uniqueInstance = new Monitor(); }
        return uniqueInstance;
    }

    /**
      This method is used to call the monitor for a given transition with a certain transitionNumber.
      It acquires the mutex and then enters the monitor.
     */
    public void callMonitor(Integer transitionNumber){

        try { mutex.acquire(); }
        catch (InterruptedException e) { System.exit(1); }
        enterMonitor(transitionNumber );
    }

    /**
      If the transition passed as a parameter is enabled in the Petri net If enabled, it will be triggered immediately.
      Otherwise, it releases the mutex and waits on the condition queue until another thread executes a release().
      Upon awakening from the condition queue, this method will make a recursive call to try triggering the transition again
     */

    private void enterMonitor(Integer transitionNumber){

        if(petriNet.isEnabled(transitionNumber)){ triggerTransition(transitionNumber); }

        else{
            try {
                mutex.release();
                conditionQueues[transitionNumber].acquire();
            }
            catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+ "was interrupted upon trying to enter a condition queue");
                System.exit(1);
            }

            enterMonitor(transitionNumber);
        }

    }

    /**
     This method will try to fire the transition passed as parameter. 3 situations can occur:

      1) if it's called within the transitions enabling time frame [ALFA,BETA], the transition is fired inmediately.
         After this, it checks if there are enabled transitions waiting. If there are, it will awaken one of them
         before releasing the mutex

      2) If the current time is less than ALFA, the thread releases the mutex and sleeps for a time period
         equal to BETA-ALFA. Upon waking up, it will try acquiring the mutex and fire the transition again.

      3) if the current time is bigger than BETA (transition is no longer enabled), the thread simply returns

     */
    public void triggerTransition(Integer transitionNumber){

        Long transitionTime = petriNet.enablingTime(transitionNumber);

        //currentTime>BETA
        if(petriNet.enablingTime(transitionNumber) == null) { return; }


        // ALFA<current time<BETA
        else if (transitionTime == 0){

            petriNet.fireTransition(transitionNumber);

            politics.increaseFirings(transitionNumber);

            Integer enabledTransitionInCondQueue = getEnabledTransitionInQueue();

            if (enabledTransitionInCondQueue != null) {
                conditionQueues[enabledTransitionInCondQueue].release();
                return;
            }

            mutex.release();
        }


        // current time<ALFA
        else{
            mutex.release();

            try{ Thread.sleep(transitionTime); }

            catch (InterruptedException e){ e.printStackTrace(); }

            callMonitor(transitionNumber);

        }

    }

    /**
      Searches for an enabled transition in the condition queues. When it finds one, checks if that transition is
      from the least fired invariant, if it is, returns said transition's number,otherwise returns null.
      @return position of the transition; null if there is none enabled transition in the condition queue."
      */

    private Integer getEnabledTransitionInQueue(){
        Integer[] inv = Politics.getLessTriggeredInv();
        for (int i = 0; i < Constants.TransitionsAmount; i++) {
            if((conditionQueues[i].hasQueuedThreads()) && (petriNet.isEnabled(i))){
                for (Integer integer : inv) {
                    if (i == integer) {
                        return i;
                    }
                }
            }
        }
        for (int i = 0; i < Constants.TransitionsAmount; i++) {
            if(conditionQueues[i].hasQueuedThreads() && (petriNet.isEnabled(i))){
                return i;
            }
        }
        return null;
    }
}