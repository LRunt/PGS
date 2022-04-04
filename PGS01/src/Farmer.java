import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code Framer} represents a boss who gives instructions to workers
 * @author Lukas Runt
 * @version 1.0 (02-04-2022)
 */
public class Farmer implements Runnable{
    /** Path to the input file*/
    private String inputFile;
    /** List of all rows of file*/
    private List<String> rowList;
    /** Number of all blocks*/
    private int numberOfBlocks;
    /** Number of all sources*/
    private int numberOfSources;
    /** Data - array od file rows*/
    private List<String> data;
    /** Printer who prints to file*/
    private Printer printer = new Printer("output.txt");
    /** Input parameters with which was program executed*/
    private InputParameters parameters;
    /** Array of threads of workers whose working in mine*/
    private Thread[] workerThreads;
    /** Array of workers whose working in mine*/
    private Worker[] workers;
    /** Number of assigned resources*/
    private int number;
    /** The lorry that is loading*/
    private Lorry actualLorry;
    /** Ferry which transport lorrys to the other side of river*/
    private Ferry dominik;
    /** Name of farmer*/
    private String name = "Farmer 1";
    /** Actual load on lorry*/
    private int actualLoad;
    /** Loading time of one block*/
    private final int LOADING_TIME = 10;
    /** Thread of lorry*/
    private Thread[] lorryThreads;
    /** Number of required lorrys*/
    int lorryNumber;

    /**
     * Constructor of class {@code Farmer}
     * @param parameters Parameters of simulation
     */
    public Farmer(InputParameters parameters){
        this.inputFile = parameters.getInputFile();
        this.parameters = parameters;
        numberOfBlocks = 0;
        numberOfSources = 0;
        number = 0;
        workers = new Worker[parameters.getcWorker()];
        workerThreads = new Thread[parameters.getcWorker()];
        rowList = readFile(inputFile);
        data = prepareData(rowList);
        actualLorry = new Lorry(parameters.getCapLorry(), parameters.gettLorry(), this);
        dominik = new Ferry(parameters.getCapFerry(), this);
        this.actualLoad = 0;
        this.lorryNumber = 0;
        int numberOfLorry = (int)Math.ceil((double)numberOfBlocks/parameters.getCapLorry());
        lorryThreads = new Thread[numberOfLorry];
    }

    /**
     * Method read and return content of the file
     * @param inputFile path of inputfile
     * @return rowList of file, if file doesn't exist then returns null
     */
    public List<String> readFile(String inputFile){
        List<String> rowList = null;
        try{
            rowList = Files.readAllLines(Paths.get(inputFile));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return rowList;
    }

    /**
     * Method is preparing data to dividing work to workers
     * @param rowList rowList form file
     * @return ArrayList of souces
     */
    public List<String> prepareData(List<String> rowList){
        List<String> data = new ArrayList<>();
        for(int i = 0; i < rowList.size(); i++) {
            String[] splited = rowList.get(i).split(" +");
            numberOfSources += splited.length;
            for (int j = 0; j < splited.length; j++) {
                numberOfBlocks += splited[j].length();
                data.add(splited[j]);
            }
        }
        return data;
    }

    /**
     * Method prints program argumenst
     * @param parameters program arguments
     */
    public void printStartData(InputParameters parameters){
        printer.printAction("Starting...");
        printer.printAction("----------------------------------------\n\t\t\tInput Parameters\n----------------------------------------");
        printer.printAction("Input file:\t\t\t\t" + parameters.getInputFile());
        printer.printAction("Output file:\t\t\t" + parameters.getOutputFile());
        printer.printAction("Number of workers:\t\t" + parameters.getcWorker());
        printer.printAction("Time of digging block:\t" + parameters.gettWorker());
        printer.printAction("Capacity of one lorry:\t" + parameters.getCapLorry());
        printer.printAction("Lorry driving time:\t\t" + parameters.gettLorry());
        printer.printAction("Capacity of ferry:\t\t" + parameters.getCapFerry());
        printer.printAction("Number of sources:\t\t" + numberOfSources);
        printer.printAction("Number od blocks:\t\t" + numberOfBlocks);
        printer.printAction("----------------------------------------\n\t\t\tSimulation\n----------------------------------------");
    }

    /**
     * Method creates and supervises the workers
     */
    @Override
    public void run() {
        //Creating workers
        for(int i = 0; i < parameters.getcWorker(); i++){
            Worker newWorker = new Worker("Worker " + (i + 1), this, parameters.gettWorker());
            workers[i] = newWorker;
            workerThreads[i] = new Thread(newWorker);
            workerThreads[i].start();
        }

        //Waiting for workers
        for (int i = 0; i < workerThreads.length; i++) {
            try {
                workerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Sending the last lorry
        if(actualLoad > 0){
            sendLorry();
        }

        printer.writeToFile("output.txt");

        try {
            for(int i = 0; i < lorryThreads.length; i++) {
                lorryThreads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printer.printAction("----------------------------------------\n\t\t\tStatistics\n----------------------------------------");

        int sumOfMidedBlocks = 0;
        for(int i = 0; i < workerThreads.length; i++){
            printer.printAction(workers[i].getName() + " mined " + workers[i].getBlocksMined() + " blocks.");
            sumOfMidedBlocks += workers[i].getBlocksMined();
        }

        printer.printAction("A " + sumOfMidedBlocks + " has been mined in total.");

        printer.printAction("A " + ((lorryNumber - 1) * parameters.getCapLorry() + actualLoad) + " has been transported.");

        printer.printAction("----------------------------------------\n\t\t\tSimulation ended\n----------------------------------------");
    }

    /**
     * The method assigns a block to the worker
     * @return the source of blocks in the mine
     */
    public synchronized String getSource(){
        String output;
        try{
            while(number < data.size()){
                output = data.get(number);
                number++;
                return output;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Method loads lorry
     */
    public synchronized void loadLorry(){
        if(actualLoad < parameters.getCapLorry()){
            actualLoad++;
            try{
                Thread.sleep(LOADING_TIME);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }else{
            Thread lorryThread = new Thread(actualLorry);
            lorryThreads[lorryNumber] = lorryThread;
            lorryNumber++;
            lorryThread.start();
            actualLoad = 0;
            actualLorry = new Lorry(parameters.getCapLorry(), parameters.gettLorry(), this);
            loadLorry();
        }
    }

    /**
     * Method send lorry to the ferry
     */
    public synchronized void sendLorry(){
        Thread lorryThread = new Thread(actualLorry);
        lorryThreads[lorryNumber] = lorryThread;
        lorryNumber++;
        lorryThread.start();
        actualLorry = new Lorry(parameters.getCapLorry(), parameters.gettLorry(), this);
    }

    /**
     * Getter of datas
     * @return data - array of rows from file
     */
    public List<String> getData() {
        return data;
    }

    /**
     * Getter of number of assigned resources
     * @return number of assigned resources
     */
    public int getNumber() {
        return number;
    }

    /**
     * Getter of printer
     * @return printer
     */
    public Printer getPrinter(){
        return printer;
    }

    /**
     * Getter of lorry
     * @return a lorry that's being loaded
     */
    public synchronized Lorry getActualLorry(){
        return actualLorry;
    }

    /**
     * Getter of Ferry
     * @return Dominik Ferry
     */
    public Ferry getDominik() {
        return dominik;
    }

    /**
     * Getter of actual load of lorry
     * @return actual load
     */
    public int getActualLoad() {
        return actualLoad;
    }

    /**
     * Getter of parameters
     * @return parameters
     */
    public InputParameters getParameters(){
       return parameters;
    }
}
