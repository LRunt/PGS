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

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public int getcWorker() {
        return cWorker;
    }

    public void setcWorker(int cWorker) {
        this.cWorker = cWorker;
    }

    public int gettWorker() {
        return tWorker;
    }

    public void settWorker(int tWorker) {
        this.tWorker = tWorker;
    }

    public int getCapLorry() {
        return capLorry;
    }

    public void setCapLorry(int capLorry) {
        this.capLorry = capLorry;
    }

    public int gettLorry() {
        return tLorry;
    }

    public void settLorry(int tLorry) {
        this.tLorry = tLorry;
    }

    public int getCapFerry() {
        return capFerry;
    }

    public void setCapFerry(int capFerry) {
        this.capFerry = capFerry;
    }


}
