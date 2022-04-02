/**
 * Class {@Lorry} truck that transports the loaded material to the other side of the river
 * @author Lukas Runt
 * @version 1.0 (02-04-2022)
 */
public class Lorry implements Runnable{

    /** Capacity of lorry*/
    private int capLorry;
    /** The time it takes a truck to reach the ferry*/
    private int tLorry;
    /** Actual load on lorry*/
    private int load;
    /** The man who commands the whole process*/
    private Farmer farmer;
    /** Name of lorry*/
    private String name;
    /** Number of existing lorrys*/
    private static int number = 0;

    /**
     * Constructor of {@code Lorry}
     * @param capLorry Capacity of lorry
     * @param tLorry Time what it takes to ferry
     * @param farmer The boss, who manage lorry and workers
     */
    public Lorry(int capLorry, int tLorry, Farmer farmer){
        this.capLorry = capLorry;
        this.farmer = farmer;
        load = 0;
        number++;
        name = "Lorry " + number;
        this.tLorry = tLorry;
    }

    /**
     * Method represents loading of material
     * @param worker worker who loading material
     */
    public synchronized void loadCargo(Worker worker){
        if(load < capLorry){
            farmer.getPrinter().printAction( name + " Prazdno - Aktualni naplneni: " + load + " ze " + capLorry);
            load++;
            farmer.getPrinter().printAction( name + " Nalozeno - Aktualni naplneni: " + load + " ze " + capLorry);
        }else{
            farmer.getPrinter().printAction("Plno");
            farmer.sendLorry();
            farmer.getActualLorry().loadCargo(worker);
        }
    }

    /**
     * Method represents the lorry's journey to the other side of the river
     */
    @Override
    public void run() {
        System.out.println("Lorry vyrazi");
        try {
            Thread.sleep(tLorry);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Lorry dorazi na Ferry");
        farmer.getDominik().transportLorry(this);
        System.out.println("Lorry byl prevezen");
        try {
            Thread.sleep(tLorry);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Lorry dojel na misto urceni");
    }

    /**
     * Getter of name of lorry
     * @return name of lorry
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of farmer
     * @return the man who controls the entire mining operation
     */
    public Farmer getFarmer() {
        return farmer;
    }


}
