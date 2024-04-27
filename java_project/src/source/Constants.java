package source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public final class Constants {
    public static final Integer PlacesAmount = 17;
    public static final Integer TransitionsAmount = 12;
    public static final Integer[][] IncidenceMatrix;
    public static final Integer[] InitialMarking;
    public static final Integer FireAmount = 1000;
    public static final Integer ForkMakerAmount = 4;
    public static final Integer MetalCutterAmount = 2;
    public static final Integer PackagerAmount = 1;
    public static final Integer MaintenanceAmount = 4;
    public static final Integer KnifeMakerAmount = 4;
    public static final long ALFA = 10; // En ms
    public static final long BETA = 5000;
    public static final Integer[] TimedTransitions = {3,4,5,6,7,9,10,11}; //T4, T5, T6, T7, T8, T10, T11, T12
    public static final Integer[] Invariant3 = {8,9,10,11}; //T9,T10,T11,T12
    public static final Integer[] Invariant2 = {0,2,4,6,7}; //T1,T3,T5,T7,T8
    public static final Integer[] Invariant1 = {0,1,3,5,7}; //T1,T2,T4,T6,T8

    static {
        try {
            IncidenceMatrix = getIncidenceMatrix();
            InitialMarking = getInitialMarking();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read the incidence matrix from a file named "combined_incidence.txt"
     */
    private static Integer[][] getIncidenceMatrix() throws Exception{
        File file = new File("combined_incidence.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        int column = 0;
        Integer[][] incidenceMatrix = new Integer[PlacesAmount][TransitionsAmount];
        String readLine;
        while ((readLine = br.readLine()) != null){
            String[] rowArray = readLine.split("\t");
            for (int row = 0; row < TransitionsAmount; row++) {
                incidenceMatrix[column][row] = Integer.parseInt(rowArray[row]);
            }
            column++;
        }
        return incidenceMatrix;
    }
    /**
     * Read the incidence matrix from a file named "initial_marking.txt"
     */
    private static Integer[] getInitialMarking() throws Exception{
        File file = new File("initial_marking.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String readLine;
        Integer[] initialMarking = new Integer[PlacesAmount];
        while ((readLine = br.readLine()) != null){
            String[] rowArray = readLine.split("\t");
            for (int row = 0; row < PlacesAmount; row++) {
                initialMarking[row] = Integer.parseInt(rowArray[row]);
            }
        }
        return initialMarking;
    }
}