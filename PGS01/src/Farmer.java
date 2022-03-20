import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Farmer implements Runnable{
    /** Path to the input file*/
    private String inputFile;
    /** List of all rows of file*/
    private List<String> rowList;
    /** number of blocks*/
    private int numberOfBlocks;
    /** number of sources*/
    private int numberOfSources;
    /** data*/
    private List<String> data;
    private Printer printer = new Printer();
    InputParameters parameters;
    private Worker[] workers;
    private int number;

    /**
     * Constructor of class {@code Farmer}
     * @param parameters Parameters of simulation
     */
    public Farmer(InputParameters parameters){
        this.inputFile = parameters.getInputFile();
        this.parameters = parameters;
        numberOfBlocks = 0;
        numberOfSources = 0;
        workers = new Worker[parameters.getcWorker()];
        rowList = readFile(inputFile);
        data = prepareData(rowList);
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

    @Override
    public void run() {
        //Creating workers
        for(int i = 0; i < parameters.getcWorker(); i++){
            workers[i] = new Worker("Worker" + (i + 1), this, parameters.gettWorker());
            workers[i].run();
        }

        //
    }

    /**
     *
     * @return
     */
    public synchronized String getSource(){
        System.out.println("zadam zdroj");
        return null;
    }
}