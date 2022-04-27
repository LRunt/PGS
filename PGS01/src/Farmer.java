import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code Framer} represents a boss who gives instructions to workers
 * @author Lukas Runt
 * @version 2.0 (27-04-2022)
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
    private String name;
    /** Thread of lorry*/
    private Thread[] lorryThreads;
    /** Array of all lorrys*/
    private Lorry[] lorrys;

    /**
     * Constructor of class {@code Farmer}
     * @param parameters Parameters of simulation
     */
    public Farmer(InputParameters parameters, String name){
        this.inputFile = parameters.getInputFile();
        this.parameters = parameters;
        numberOfBlocks = 0;
        numberOfSources = 0;
        number = 0;
        workers = new Worker[parameters.getcWorker()];
        workerThreads = new Thread[parameters.getcWorker()];
        rowList = readFile(inputFile);
        data = prepareData(rowList);
        dominik = new Ferry(parameters.getCapFerry(), printer);
        int numberOfLorry = (int)Math.ceil((double)numberOfBlocks/parameters.getCapLorry());
        lorryThreads = new Thread[numberOfLorry];
        this.actualLorry = new Lorry(parameters.getCapLorry(), parameters.gettLorry(), this);
        this.lorrys = new Lorry[numberOfLorry];
        lorrys[0] = actualLorry;
        this.name = name;
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
        printer.printAction(this.name, Thread.currentThread().getName(), String.format("Total number of blocks in the mine: " + numberOfBlocks));
        printer.printAction(this.name, Thread.currentThread().getName(), String.format("Total number of sources in the mine: " + numberOfSources));
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
        if(lorrys[Lorry.getNumber() - 1].getActualLoad() > 0){
            sendLastLorry();
        }

        printer.writeToFile("output.txt");

        //Waiting for all lorry threads
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

        //counting transported load
        int transportedLoad = 0;
        for(int i = 0; i < lorrys.length; i++){
            transportedLoad += lorrys[i].getActualLoad();
        }
        printer.printAction("A " + transportedLoad + " has been transported.");

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
     * Method send lorry to the ferry
     */
    public synchronized void sendLastLorry(){
        Thread lorryThread = new Thread(lorrys[Lorry.getNumber() - 1]);
        lorryThreads[Lorry.getNumber() - 1] = lorryThread;
        lorryThread.start();
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
    public Lorry getActualLorry(){
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
     * Setter of actual lorry
     * @param actualLorry
     */
    public synchronized void setActualLorry(Lorry actualLorry) {
        this.actualLorry = actualLorry;
    }

    /**
     * Getter of lorryThread
     * @return lorryThread
     */
    public synchronized Thread[] getLorryThreads() {
        return lorryThreads;
    }

    /**
     * Getter of array of lorrys
     * @return lorrys
     */
    public Lorry[] getLorrys() {
        return lorrys;
    }

    public InputParameters getParameters() {
        return parameters;
    }
}
