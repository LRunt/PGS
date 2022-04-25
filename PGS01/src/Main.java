/**
 * The main class, the entry point of program
 * @author Lukas Runt
 * @version 1.1 (24-03-2022)
 */
public class Main {

    /**
     * Entry point of program
     * @param args program arguments
     */
    public static void main(String[] args){
        InputParameters parameters= new InputParameters(args[1], args[3], Integer.parseInt(args[5]), Integer.parseInt(args[7]), Integer.parseInt(args[9]), Integer.parseInt(args[11]), Integer.parseInt(args[13]));
        Farmer farmer = new Farmer(parameters, "Farmer 1");
        farmer.printStartData(parameters);
        farmer.run();
    }
}
