package source;

import java.io.IOException;
import java.util.Arrays;

import logs.Log;

public class PetriNet {
    private static Integer[][] incidenceMatrix;
    private static Integer[] currentMarking;
    private static PetriNet uniqueInstance;
    private static Boolean[] transitionStates;
    private static final Long[] timeStamp = new Long[Constants.TransitionsAmount];

    /**
     * Constructor for a singleton PetriNet instance.
     */

    private PetriNet(){
        incidenceMatrix = Constants.IncidenceMatrix;
        currentMarking = Constants.InitialMarking;
        setTimeConstants();
        transitionStates = new Boolean[Constants.TransitionsAmount];
        for (int i = 0; i < Constants.TransitionsAmount; i++){
            transitionStates[i] = false;
        }
        transitionStates[0] = true;
        transitionStates[8] = true;
        for (int i = 0; i < Constants.TransitionsAmount; i++){
            timeStamp[i] = System.currentTimeMillis();
        }
    }

    /**
     * Only one instance of PetriNet can be created in the program.
     * @return PetriNet
     */
    public static PetriNet getInstance(){
        if(uniqueInstance == null){
            uniqueInstance = new PetriNet();
        }
        return uniqueInstance;
    }

    /**
     * Multiplies a matrix and a vector
     * @param m incidence Matrix (PxT)
     * @param s firing sequence vector (1xT)
     * @return resulting vector
     */
    public Integer[] computeMultiplication(Integer[][] m, Integer[] s){
        Integer[] res = new Integer[Constants.PlacesAmount];
        Arrays.fill(res, 0);
        for (int i = 0; i< Constants.PlacesAmount; i++){
            for (int j = 0; j< Constants.TransitionsAmount; j++){
                res[i] += m[i][j]*s[j];
            }
        }
        return res;
    }

    /**
     * Add two vectors: initial marking state vector and the computed vector resulting of the multiplication of
     * the incidence matrix and the firing sequence vector.
     * @param v1 initial marking vector.
     * @param v2 computed multiplication vector.
     * @return resulting vector representing the current marking of the PN
     */
    public Integer[] addVectors(Integer[] v1, Integer[] v2){
        if (v1.length == v2.length){
            Integer[] res = new Integer[v1.length];
            for (int i=0; i<v1.length; i++){
                res[i] = v1[i] + v2[i];
            }
            return res;
        }
        System.out.println("ERROR");
        return null;
    }

    /**
     * Algorithm to search all enabled transitions in the PetriNet.
     * Reference paper: Ecuación de estado generalizada para redes de Petri no autónomas y con distintos tipos de arcos
     * @return Bolean vector showing enabled (1) and disabled (0) transitions.
     */
    public Boolean[] findOutTransitionStates(){
        Boolean[] eTransicion = new Boolean[Constants.TransitionsAmount];
        for (int i = 0; i< Constants.TransitionsAmount; i++){
            int[] sn = new int[Constants.PlacesAmount];
            for (int j = 0; j< Constants.PlacesAmount; j++){
                sn[j] = currentMarking[j] + incidenceMatrix[j][i];
                if (sn[j] < 0){
                    eTransicion[i] = false;
                    break;
                }else {
                    eTransicion[i] = true;
                }
            }
        }
        return eTransicion;
    }

    /**
     * Generates and updates the state of the transitions and sets the times for temporal transitions
     */
    public void updateTransitionState(){
        Boolean[] NewTrans = findOutTransitionStates();
        setTimeStamp(NewTrans);
        setTransitionStates(NewTrans);
    }

    /**
     * Verifies if a specific transition is enabled.
     * @param T transition index in the vector.
     * @return  true(transition is enabled) or false (transition is disabled).
     */
    public boolean isEnabled(Integer T){
        return transitionStates[T];
    }

    /**
     * Determines if a transition can be fired based on its sensitization time.
     * Checks if it falls within the configured time interval, defined by tMin and tMax.
     * If within the interval, the transition is eligible for firing.
     * If not, it waits until it enters the interval, releasing the mutex.
     * If it exceeds tMax, the transition is not fired, and the thread exits the monitor.
     *
     * @param T Transition to be fired.
     * @return 0 if within the time interval, null if passed, or the time to sleep.
     */
    public Long enablingTime(Integer T){
        Long[] timeStamp = getTimeStamp();
        long tActual = System.currentTimeMillis();
        long tMin = timeStamp[T] + Constants.ALFA;
        long tMax = timeStamp[T] + Constants.BETA;

        if ((tActual >= tMin) && (tActual <= tMax)){
            return 0L;
        }

        else if (tActual < tMin) {
            return tMin - tActual;
        }

        else {
            return null;
        }
    }

    /**
     * Updates the current state of the Petri Net, the state of the transitions (and their times) and fills the log file
     * m_i+1 = m_i + MxS
     * @param T transition to fire
     */
    public void fireTransition(Integer T){
        Integer[] sec = new Integer[Constants.TransitionsAmount];
        Arrays.fill(sec, 0);
        sec[T] = 1;
        updateMarking(sec);
        updateTransitionState();
        fillLog(T+1);

        if (!matchesPlaceInvariant()) {
            System.out.println("Does not match P invariant");
            System.exit(1);
        }
    }

    /**
     * Saves the instant in which a transition is enabled.
     * @param NewTransic vector of transitions states
     */
    public void setTimeStamp(Boolean[] NewTransic) {
        for (int i = 0; i < Constants.TransitionsAmount; i++) {
            if (!NewTransic[i].equals(isEnabled(i))){
                timeStamp[i] = System.currentTimeMillis();
            }
        }
    }

    /**
     * @return vector with enabling time of transitions
     */
    public Long[] getTimeStamp(){
        return timeStamp;
    }

    /**
     * At the begining, start all enabling times at 0.
     */
    public static void setTimeConstants(){
        Arrays.fill(timeStamp, 0L);
    }

    /**
     * Sets a new state for the transitions.
     * @param eT Boolean vector with the new state of transitions.
     */
    public void setTransitionStates(Boolean[] eT){
        transitionStates = eT;
    }

    /**
     * Updates the state of the Petri Net, computes the new state using the state equation.
     * @param sec firing sequence vector
     */
    private void updateMarking(Integer[] sec){
        currentMarking = addVectors(currentMarking, computeMultiplication(incidenceMatrix, sec));
    }

    /**
     * Checks that all P invariants are satisfied.
     * @return true if satisfies, false if not
     */
    public static boolean matchesPlaceInvariant(){
        boolean Ip1, Ip2, Ip3, Ip4, Ip5, Ip6, Ip7, Ip8;
        Ip1 = (currentMarking[9] + currentMarking[10] + currentMarking[7] + currentMarking[8] == 4);
        Ip2 = (currentMarking[0] + currentMarking[9] + currentMarking[11] == 2);
        Ip3 = (currentMarking[12] + currentMarking[1] + currentMarking[2] + currentMarking[8] == 2);
        Ip4 = (currentMarking[13] + currentMarking[3] + currentMarking[4] + currentMarking[7] == 3);
        Ip5 = (currentMarking[14] + currentMarking[5] == 1);
        Ip8 = (currentMarking[15] + currentMarking[7] == 2);
        Ip6 = (currentMarking[16] + currentMarking[8] == 1);
        Ip7 = (currentMarking[0] + currentMarking[1] + currentMarking[2] + currentMarking[3] + currentMarking[4] + currentMarking[5] + currentMarking[6] == 4);
        return (Ip1 && Ip2 && Ip3 && Ip4 && Ip5 && Ip6 && Ip7 && Ip8);
    }

    /**
     * Fill the log file for later processing by the Regex (transitions T10, T11 y T12 are replaced by TA, TB y TC)
     */
    public static void fillLog(Integer T){
        try {
            switch (T){
                case 10 -> {
                    Log.fw.write("TA");
                    Log.fw.flush();
                }
                case 11 -> {
                    Log.fw.write("TB");
                    Log.fw.flush();
                }
                case 12 -> {
                    Log.fw.write("TC");
                    Log.fw.flush();
                }
                default -> {
                    Log.fw.write("T" + T);
                    Log.fw.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

