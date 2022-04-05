/**
 * Class {@Ferry} represents ferry who takes lorrys to the other side of the river
 * @author Lukas Runt
 * @version 1.1 (05-04-2022)
 */
public class Ferry {
    /** Capacity of ferry*/
    private int capFerry;
    /** Actual number of trucks on board */
    private int lorryOnBoard;
    /** Indicates whether the ferry is waiting */
    private boolean wait = true;
    /** Printer who prints actions*/
    private Printer printer;
    /** Time when was ferry empty*/
    private long start;
    /** Number of ferry*/
    private int numberOfFerry;

    /**
     * Constuctor of class {@Ferry}
     * @param capFerry capacity of ferry
     * @parem printer printer who prints actions
     */
    public Ferry(int capFerry, Printer printer){
        this.capFerry = capFerry;
        lorryOnBoard = 0;
        this.printer = printer;
        start = System.currentTimeMillis();
        numberOfFerry = 0;
    }

    /**
     * Ferry waits until it's full and then heads across the river
     */
    public synchronized void transportLorry(){
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
            printer.printAction("Ferry " + numberOfFerry, Thread.currentThread().getName(), "The ferry has started. It was waiting to be filled for: " + (System.currentTimeMillis() - start) + "ms");
            notifyAll();
            start = System.currentTimeMillis();
            numberOfFerry++;
        }
    }
}
