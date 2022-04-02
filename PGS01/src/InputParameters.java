/**
 * Class {@code InputParamaters} represents parameters that were entered when the application was executed
 * @author Lukas Runt
 * @version 1.0 (14-03-2022)
 */
public class InputParameters {
    /** Path to the input file*/
    private String inputFile;
    /** Path to the output file*/
    private String outputFile;
    /** Number of workers*/
    private int cWorker;
    /** Maximum time of mining of one block X*/
    private int tWorker;
    /** Capacity of one lorry*/
    private int capLorry;
    /** Lorry driving time*/
    private int tLorry;
    /** Capacity of one ferry*/
    private int capFerry;

    /**
     * Constructor of {@code InputParameters}
     * @param inputFile Path of input file
     * @param outputFile Path of output file
     * @param cWorker Number of workers
     * @param tWorker Maximal time of mining one block
     * @param capLorry Capacity of one lorry
     * @param tLorry Lorry driving time
     * @param capFerry Capacity of one ferry
     */
    public InputParameters(String inputFile, String outputFile, int cWorker, int tWorker, int capLorry, int tLorry, int capFerry){
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.cWorker = cWorker;
        this.tWorker = tWorker;
        this.capLorry = capLorry;
        this.tLorry = tLorry;
        this.capFerry = capFerry;
    }

    /**
     * Getter of input file
     * @return name of input file
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * Getter of output file
     * @return name of output file
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * Getter of cWorker
     * @return number of workers
     */
    public int getcWorker() {
        return cWorker;
    }

    /**
     * Getter of tWorker
     * @return maximum time of mining
     */
    public int gettWorker() {
        return tWorker;
    }

    /**
     * Getter of capLorry
     * @return the capacity of lorry
     */
    public int getCapLorry() {
        return capLorry;
    }

    /**
     * Getter of tLorry
     * @return transport time of lorry
     */
    public int gettLorry() {
        return tLorry;
    }

    /**
     * Getter of capFerry
     * @return capacity of ferry
     */
    public int getCapFerry() {
        return capFerry;
    }

}
