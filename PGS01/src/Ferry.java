public class Ferry {

    /** Capacity of ferry*/
    private int capFerry;
    /** Actual number of trucks on board */
    private int lorryOnBoard;
    /** Indicates whether the ferry is waiting */
    private boolean wait = true;


    /**
     * Constuctor of class {@Ferry}
     * @param capFerry capacity of ferry
     */
    public Ferry(int capFerry){
        this.capFerry = capFerry;
        lorryOnBoard = 0;
    }

    /**
     * Ferry waits until it's full and then heads across the river
     * @param lorry
     */
    public synchronized void transportLorry(Lorry lorry){

        while(!wait){
            lorry.getFarmer().getPrinter().printAction(lorry.getName() + "Musi pockat az ostatni vlakna opusti barieru");
            try{
                wait();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }

            if(!wait){
                System.err.println("Vlakno bylo samovolne probuzeno");
            }
        }

        lorryOnBoard++;

        if (lorryOnBoard == capFerry){


            wait = false;

            notifyAll();
        }

        while(wait){
            lorry.getFarmer().getPrinter().printAction(lorry.getName() + "Musi pockat az privoz naplni");

            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            if(wait){
                System.err.println("Vlakno bylo samovolne probuzeno");
            }
        }

        lorryOnBoard--;

        if(lorryOnBoard == 0){
            wait = true;

            System.out.println("Vlakno" + lorry.getName()+ " probouzi ostatni vlakna, predchozi inkarnaci bariery opustila vsechna vlakna.");

            notifyAll();
        }

    }
}
