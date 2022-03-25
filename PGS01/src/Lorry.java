public class Lorry implements Runnable{

    private int capLorry;
    private int load;
    private Farmer farmer;
    private String name;
    private static int number = 0;

    public Lorry(int capLorry, Farmer farmer){
        this.capLorry = capLorry;
        this.farmer = farmer;
        load = 0;
        number++;
        name = "Lorry " + number;
    }

    /**
     * Method load cargo
     * @param worker
     */
    public synchronized void loadCargo(Worker worker){
        if(load < capLorry){
            farmer.getPrinter().printAction( name + " Prazdno - Aktualni naplneni: " + load + " ze " + capLorry);
            farmer.sleep(worker, 10);
            load++;
            farmer.getPrinter().printAction( name + " Nalozeno -  Aktualni naplneni: " + load + " ze " + capLorry);
        }else{
            farmer.getPrinter().printAction("Plno");
            farmer.createLorry();
            run();
            farmer.getActualLorry().loadCargo(worker);
        }
    }

    @Override
    public void run() {

    }

    public Farmer getFarmer(){
        return farmer;
    }

    public String getName(){
        return name;
    }
}
