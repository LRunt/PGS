import java.util.Random;

public class Worker implements Runnable{
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
            int totalTime = 0, sourceLength = actualSource.length();
            //Mining blocks
            for(int i = 0; i < actualSource.length(); i++){
                int miningTime = generateRandomNumber(0, time);
                totalTime += miningTime;
                try {
                    Thread.sleep(miningTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                farmer.getPrinter().printAction(name + " Mine one block: "  + miningTime + "s");
            }
            farmer.getPrinter().printAction(String.format("%s Mine one source (%d blocks) %ds", name, sourceLength, totalTime));
            //Transporting blocks
            farmer.getPrinter().printAction(name  + " Carries " + actualSource.length() + " blocks");
            //Loading blocks
            for(int i = 0; i < actualSource.length(); i++) {
                farmer.getActualLorry().loadCargo(this);
            }
        }
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

}
