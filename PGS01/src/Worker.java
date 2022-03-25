public class Worker extends Thread{
    /** Name of worker*/
    private String name;
    /** A foreman who assigns work*/
    private Farmer farmer;
    /** Maximal time of mining a block*/
    private int time;

    /**
     * Constructor of class {@code Worker}
     * @param name
     * @param farmer
     * @param time
     */
    public Worker(String name, Farmer farmer, int time){
        this.name = name;
        this.farmer = farmer;
        this.time = time;
    }

    @Override
    public void run() {
        String actualSource;
        while(farmer.getNumber() < farmer.getData().size()) {
            actualSource = farmer.getSource();
            for(int i = 0; i < actualSource.length(); i++){
                System.out.println(name + "Tezi: "  + time + "s");
                farmer.sleep(this);
            }
        }
    }

}
