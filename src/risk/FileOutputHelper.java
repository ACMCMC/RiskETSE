package risk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class FileOutputHelper {

    private static File outputFile = new File("output.txt");
    private static FileWriter fileWriter;
    private static BufferedWriter bufferedWriter;

    static {
        try {
            fileWriter = new FileWriter(outputFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    static void printToOutput(String output) {
        System.out.print(output);
        System.out.println();

        try {
            bufferedWriter.write(output);
            bufferedWriter.write(System.getProperty("line.separator"));
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
