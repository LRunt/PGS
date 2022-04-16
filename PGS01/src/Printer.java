import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class {@code Printer} represents a printer who prints to the console and to the file
 * @author Lukas Runt
 * @version 1.2 (05-04-2022)
 */
public class Printer {
    /** Time stamp format*/
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");;
    /** Local date*/
    private LocalDateTime now;
    /** Name of output file*/
    private String fileName;
    /** Text which will be printed into the file*/
    private String output;
    /** A number of characters after which the string is written to the file */
    private final int BUFFER_SIZE = 10000;

    /**
     * Constructor of class {@Printer}
     * @param fileName name of output file
     */
    public Printer(String fileName){
        now = LocalDateTime.now();
        this.fileName = fileName;
        createNewFile(this.fileName);
        output = "";
    }

    /**
     * Method prints action
     * @param description description of action
     */
    public void printAction(String description){
        System.out.printf(String.format("%s\n", description));
    }

    /**
     * Method prints action into the console and file
     * @param role the role of the entity that announces the action
     * @param thread name of thread
     * @param description description of action
     */
    public synchronized void printAction(String role, String thread, String description){
        now = LocalDateTime.now();
        String out = String.format("<%s><%s><%s><%s>\n", dtf.format(now),role, thread, description);
        System.out.printf(String.format("%s\n", description));
        output += out;
        if(output.length() > BUFFER_SIZE) writeToFile(fileName);
    }

    /**
     * Method create new empty file
     * @param fileName name of file what will be created
     */
    public void createNewFile(String fileName){
        try{
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(fileName, false));
            bw.write("");
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method writes actions to the file
     * @param fileName name of output file
     */
    public synchronized void writeToFile(String fileName){
        try{
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(fileName, true));
            bw.write(output);
            bw.close();
            output = "";
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
