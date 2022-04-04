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
     * Method represents the lorry's journey to the other side of the river
     */
    @Override
    public void run() {
        int travelTime;
        loadingEnd = System.currentTimeMillis();
        farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), this.name + " is filled and heading for the ferry, Loading time: " + (loadingEnd - loadingStart) + "ms");
        travelTime = generateRandomNumber(0, tLorry - 1);
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), this.name + " has arrived at the ferry, Time of travel: " + travelTime + "ms");
        farmer.getDominik().transportLorry(this);
        travelTime = generateRandomNumber(0, tLorry);
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), this.name + " arrived to its destination, Travel time from ferry: " + travelTime + "ms");

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

}
