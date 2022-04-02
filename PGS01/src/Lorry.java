public class Lorry implements Runnable{

    private int capLorry;
    private int tLorry;
    private int load;
    private Farmer farmer;
    private String name;
    private static int number = 0;

    public Lorry(int capLorry, int tLorry, Farmer farmer){
        this.capLorry = capLorry;
        this.farmer = farmer;
        load = 0;
        number++;
        name = "Lorry " + number;
        this.tLorry = tLorry;
    }

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

    public String getName() {
        return name;
    }

    public Farmer getFarmer() {
        return farmer;
    }


}
