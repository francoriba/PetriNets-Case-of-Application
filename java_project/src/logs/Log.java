package logs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static source.Main.isRunning;

public class Log implements Runnable{
    private static final File file = new File("log.txt");
    public static FileWriter fw;

    /**
     * Constructor
     * **/
    public Log() {
        try {
            fw = new FileWriter(file.getAbsoluteFile(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                file.createNewFile();
            }
            Thread.sleep(3000);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
