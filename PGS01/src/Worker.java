import java.util.Random;

/**
 * The class {@Worker} represents a worker who mines a block in mine
 * @author Lukas Runt
 * @version 1.2 (02-04-2022)
 */
public class Worker implements Runnable{
    /** Name of worker*/
    private String name;
    /** A foreman who assigns work*/
    private Farmer farmer;
    /** Maximal time of mining a block*/
    private int time;
    /** Number of block the worker has mined already*/
    private int blocksMined;

    /**
     * Constructor of class {@code Worker}
     * @param name name of worker
     * @param farmer the boss of worker
     * @param time maximal time of mining blocks
     */
    public Worker(String name, Farmer farmer, int time){
        this.name = name;
        this.farmer = farmer;
        this.time = time;
        this.blocksMined = 0;
    }

    /**
     * Method simulates worker who mine blocks and loading the excavated material into the lorry
     */
    @Override
    public void run() {
        String actualSource;
        while(farmer.getNumber() < farmer.getData().size()) {
            actualSource = farmer.getSource();
            int totalTime = 0, sourceLength = actualSource.length();
            //Mining blocks
            for (int i = 0; i < actualSource.length(); i++) {
                int miningTime = generateRandomNumber(0, time);
                totalTime += miningTime;
                try {
                    Thread.sleep(miningTime);
                    blocksMined++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), name + " Mine one block: " + miningTime + "ms");
            }
            farmer.getPrinter().printAction(this.name, Thread.currentThread().getName(), String.format("%s Mine one source (%d blocks) %dms", name, sourceLength, totalTime));
            //Transporting blocks
            farmer.getPrinter().printAction(name + " Carries " + actualSource.length() + " blocks");
            //Loading blocks
            for (int i = 0; i < actualSource.length(); i++) {
                farmer.getLorrys()[Lorry.getNumber()].loadLorry(farmer.getLorrys(), farmer.getLorryThreads(), this);
            }
        }
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
     * Getter of blocksMined
     * @return Number of block the worker has mined already
     */
    public int getBlocksMined() {
        return blocksMined;
    }

    /**
     * Getter of name
     * @return name of worker
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
