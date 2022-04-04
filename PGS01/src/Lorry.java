import java.util.Random;

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
    /** Time of start of loading*/
    private long loadingStart;
    /** Time of end of loading*/
    private long loadingEnd;
    /** Number of existing lorrys*/
    private static int number = 0;
    /** Loading time of one block*/
    private final int LOADING_TIME = 10;

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
        this.loadingStart = System.currentTimeMillis();
    }

    /**
     * Method represents loading of material
     * @param worker worker who loading material
     */
    public synchronized void loadCargo(Worker worker){
        if(load < capLorry){
            //farmer.getPrinter().printAction( name + " Prazdno - Aktualni naplneni: " + load + " ze " + capLorry);
            /*long startLoading = System.currentTimeMillis();
            while(System.currentTimeMillis() - startLoading < LOADING_TIME){}*/
            load++;
            /*try{
                Thread.sleep(LOADING_TIME);
            }catch(InterruptedException e){
                e.printStackTrace();
            }*/
            //farmer.getPrinter().printAction( name + " Nalozeno - Aktualni naplneni: " + load + " ze " + capLorry);
        }else{
            //farmer.getPrinter().printAction("Plno");
            farmer.sendLorry();
            farmer.getActualLorry().loadCargo(worker);
        }
    }

    /**
     * Method represents the lorry's journey to the other side of the river
     */
    @Override
    public void run() {
        int travelTime;
        loadingEnd = System.currentTimeMillis();
        //System.out.println("Lorry vyrazi");
        farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), "The lorry is filled and heading for the ferry, Loading time: " + (loadingEnd - loadingStart) + "ms");
        travelTime = generateRandomNumber(0, tLorry - 1);
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), "The truck has arrived at the ferry, Time of travel: " + travelTime + "ms");
        farmer.getDominik().transportLorry(this);
        //System.out.println("Lorry byl prevezen");
        travelTime = generateRandomNumber(0, tLorry);
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("Lorry dojel na misto urceni");
        farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), "The lorry arrived at its destination, Travel time from ferry: " + travelTime + "ms");

        farmer.getPrinter().writeToFile("output.txt");
    }

    /**
     * Method generate random number
     * @param min minimal possible number
     * @param max maximal number
     * @return random number in range
     */
    private int generateRandomNumber(int min, int max){
        min++;
        if(min >= max){
            System.err.println("Error: minimum is bigger or same than maximum!");
        }
        Random r = new Random();
        return min + r.nextInt(max - min);
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

    /**
     * Getter of load
     * @return actual load of lorry
     */
    public int getLoad() {
        return load;
    }

}
