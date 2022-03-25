import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Printer {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");;
    private LocalDateTime now;
    private String fileName;
    private String output;


    public Printer(String fileName){
        now = LocalDateTime.now();
        this.fileName = fileName;
        createNewFile(this.fileName);
        output = "";
    }

    public void printAction(String describtion){
        now = LocalDateTime.now();
        String out = String.format("<%s><%s>\n", dtf.format(now), describtion);
        System.out.printf(out);
        output += out;
        if(output.length() > 10000) writeToFile(fileName);
    }

    /**
     * Method create new empty file
     * @param fileName
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
