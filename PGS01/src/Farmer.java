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
    /** Array of workers whose working in mine*/
    private Thread[] workers;
    /** Number of assigned resources*/
    private int number;
    /** The lorry that is loading*/
    private Lorry actualLorry;
    /** Ferry which transport lorrys to the other side of river*/
    private Ferry dominik;

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
        workers = new Thread[parameters.getcWorker()];
        rowList = readFile(inputFile);
        data = prepareData(rowList);
        actualLorry = new Lorry(parameters.getCapLorry(), parameters.gettLorry(), this);
        dominik = new Ferry(parameters.getCapFerry());
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
        printer.printAction("Starting simulation");
        printer.printAction("Input file: " + parameters.getInputFile());
        printer.printAction("Output file: " + parameters.getOutputFile());
        printer.printAction("Number of workers: " + parameters.getcWorker());
        printer.printAction("Time of digging block: " + parameters.gettWorker());
        printer.printAction("Capacity of one lorry: " + parameters.getCapLorry());
        printer.printAction("Lorry driving time: " + parameters.gettLorry());
        printer.printAction("Capacity of ferry: " + parameters.getCapFerry());
        printer.printAction("Number of sources: " + numberOfSources);
        printer.printAction("Number od blocks: " + numberOfBlocks);
    }

    /**
     * Method creates and supervises the workers
     */
    @Override
    public void run() {
        //Creating workers
        for(int i = 0; i < parameters.getcWorker(); i++){
            Worker newWorker = new Worker("Worker" + (i + 1), this, parameters.gettWorker());
            workers[i] = new Thread(newWorker);
            System.out.println("Vytvoren delnik " + (i + 1));
            workers[i].start();
        }

        //Waiting for workers
        for (int i = 0; i < workers.length; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * The method assigns a block to the worker
     * @return the source of blocks in the mine
     */
    public synchronized String getSource(){
        System.out.println("zadam zdroj");
        String output;
        try{
            while(number < data.size()){
                output = data.get(number);
                System.out.println("Přidělen zdroj " + (number + 1));
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
    public synchronized void sendLorry(){
        Thread lorryThread = new Thread(actualLorry);
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
}
