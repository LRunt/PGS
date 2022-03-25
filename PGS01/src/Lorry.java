public class Lorry {

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

    public synchronized void loadCargo(Worker worker){
        if(load < capLorry){
            farmer.getPrinter().printAction( name + " Prazdno - Aktualni naplneni: " + load + " ze " + capLorry);
            load++;
        }else{
            farmer.getPrinter().printAction("Plno");
            farmer.createLorry();
            farmer.getActualLorry().loadCargo(worker);
        }
    }

}
