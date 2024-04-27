package source;

public class Politics {
    private static Politics politics;
    public static double inv1 = 0;
    private static double inv2 = 0;
    private static double inv3 = 0;
    private static boolean switchinv = false;
    public Politics(){

    }

    /**
     *Ensures that only one instance of Politics is created using the Singleton design pattern.
     */
    public static Politics getInstance(){
        if (politics == null){
            politics = new Politics();
        } else {
            System.out.println("Cant create another Politics");
        }
        return politics;
    }

    /**
     *returns the set of invariant based on their average values.
     */
    public static Integer[] getLessTriggeredInv(){
        if(inv2 < inv3 && inv2 < inv1){
            return Constants.Invariant2;
        }
        else if(inv3 < inv1 && inv3 < inv2){
            return Constants.Invariant3;
        }
        else if(inv1 < inv3 && inv1 < inv2){
            return Constants.Invariant1;
        }
            return getRandomInv();
    }

    /**
     *method returns a random invariant set if the averages are equal.
     * It alternates between the invariants
     */
    private static Integer[] getRandomInv(){
        if (switchinv) {
            switchinv = !switchinv;
            return Constants.Invariant2;
        }
        else {
            switchinv = !switchinv;
            return Constants.Invariant3;
        }
    }
    /**
     * updating the transitions values based on the type of firing.
     * prints transitions
     */
    public void increaseFirings(Integer T){
        switch (T) {
            case 5 -> {
                inv1++;
                System.out.println(inv1 + inv2 + inv3);
            }
            case 6 -> {
                inv2++;
                System.out.println(inv1 + inv2 + inv3);
            }
            case 11 -> {
                inv3++;
                System.out.println(inv1 + inv2 + inv3);
            }
        }

        if (inv1 + inv2 + inv3 == Constants.FireAmount) {
            Main.stopRunning();
            printpercentageinv();
            System.exit(0);
        }
    }
    private void printpercentageinv(){
        System.out.println("Avg invariante 1: " + (inv1 /(Constants.FireAmount))*100 + "%");
        System.out.println("Avg invariante 2: " + (inv2 / Constants.FireAmount)*100 + "%");
        System.out.println("Avg invariante 3: " + (inv3 / Constants.FireAmount)*100 + "%");
        if(PetriNet.matchesPlaceInvariant()) System.out.println("Cumple invariantes de Plaza");
    }


}
