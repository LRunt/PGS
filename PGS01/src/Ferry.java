/**
 * Class {@Ferry} represents ferry who takes lorrys to the other side of the river
 * @author Lukas Runt
 * @version 1.0 (02-04-2022)
 */
public class Ferry {
    /** Capacity of ferry*/
    private int capFerry;
    /** Actual number of trucks on board */
    private int lorryOnBoard;
    /** Indicates whether the ferry is waiting */
    private boolean wait = true;
    /** A boss who controls everything*/
    private Farmer farmer;
    /** Time when was ferry empty*/
    private long start;
    /** Number of ferry*/
    private int numberOfFerry;

    /**
     * Constuctor of class {@Ferry}
     * @param capFerry capacity of ferry
     */
    public Ferry(int capFerry, Farmer farmer){
        this.capFerry = capFerry;
        lorryOnBoard = 0;
        this.farmer = farmer;
        start = System.currentTimeMillis();
        numberOfFerry = 0;
    }

    /**
     * Ferry waits until it's full and then heads across the river
     * @param lorry
     */
    public synchronized void transportLorry(Lorry lorry){
        while(!wait){
            try{
                wait();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
            if(!wait){
                System.err.println("The thread was spontaneously awakened");
            }
        }
        lorryOnBoard++;
        if (lorryOnBoard == capFerry){
            wait = false;
            notifyAll();
        }
        while(wait){
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            if(wait){
                System.err.println("The thread was spontaneously awakened");
            }
        }
        lorryOnBoard--;
        if(lorryOnBoard == 0){
            wait = true;
            farmer.getPrinter().printAction("Ferry " + numberOfFerry, Thread.currentThread().getName(), "The ferry has started. It was waiting to be filled for: " + (System.currentTimeMillis() - start) + "ms");
            notifyAll();
            start = System.currentTimeMillis();
            numberOfFerry++;
        }
    }
}
