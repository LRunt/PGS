
public class Main {

    public static void main(String[] args){
        InputParameters parameters= new InputParameters(args[1], args[3], Integer.parseInt(args[5]), Integer.parseInt(args[7]), Integer.parseInt(args[9]), Integer.parseInt(args[11]), Integer.parseInt(args[13]));
        Farmer farmer = new Farmer(parameters);
        farmer.printStartData(parameters);
        farmer.run();
    }
}
