/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOutputHelper {

    private static File outputFile = new File("output.txt");
    private static BufferedWriter bufferedWriter;
    private static File errorOutputFile = new File("errorOutput.txt");
    private static BufferedWriter errorBufferedWriter;

    static {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            errorBufferedWriter = new BufferedWriter(new FileWriter(errorOutputFile));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static void printToErrOutput(String th) {
        System.out.println(th); // Lo imprimimos también por consola
        try {
            errorBufferedWriter.write(th);
            errorBufferedWriter.write(System.getProperty("line.separator"));
            errorBufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printToOutput(String output) {
        System.out.println(output); // Lo imprimimos también por consola
        try {
            bufferedWriter.write(output);
            bufferedWriter.write(System.getProperty("line.separator"));
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
