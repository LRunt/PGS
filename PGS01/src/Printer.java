import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Printer {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");;
    private LocalDateTime now;

    public Printer(){
        now = LocalDateTime.now();

    }

    public void printAction(String describtion){
        System.out.printf("<%s><%s>\n", dtf.format(now), describtion);
    }
}
