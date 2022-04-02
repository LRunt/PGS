public class Lorry implements Runnable{

    private int capLorry;
    private int load;
    private Farmer farmer;
    private String name;
    private static int number = 0;
    private boolean loading;

    public Lorry(int capLorry, Farmer farmer){
        this.capLorry = capLorry;
        this.farmer = farmer;
        load = 0;
        number++;
        name = "Lorry " + number;
        loading = false;
    }

    /**
     * Method load cargo
     * @param worker
     */
    public synchronized boolean loadCargo(Worker worker){
        if(load < capLorry){
            farmer.getPrinter().printAction( name + " Prazdno - Aktualni naplneni: " + load + " ze " + capLorry);
            farmer.sleep(worker, 10);
            load++;
            farmer.getPrinter().printAction( name + " Nalozeno -  Aktualni naplneni: " + load + " ze " + capLorry);
            return true;
        }else{
            farmer.getPrinter().printAction("Plno");
            //farmer.getActualLorry().run();
            farmer.createLorry();
            //farmer.getActualLorry().loadCargo(worker);
            return false;
        }
    }

    @Override
    public void run() {
        farmer.getActualFerry().transportLorry(this);
    }

    public Farmer getFarmer(){
        return farmer;
    }

    public String getName(){
        return name;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading;
    }
}
