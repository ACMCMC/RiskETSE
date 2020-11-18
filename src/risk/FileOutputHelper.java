/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class FileOutputHelper {

  private static File outputFile = new File("output.txt");
  private static File errorOutputFile = new File("errorOutput.txt");

  static {
    try {
      new FileWriter(outputFile, false).close();
      new FileWriter(errorOutputFile, false).close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  static void printToErrOutput(String th) {
    System.out.println(th); // Lo imprimimos también por consola
    try (
      BufferedWriter errorBufferedWriter = new BufferedWriter(
        new FileWriter(errorOutputFile, true)
      )
    ) {
      errorBufferedWriter.write(th);
      errorBufferedWriter.write(System.getProperty("line.separator"));
      errorBufferedWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void printToOutput(String output) {
    System.out.println(output); // Lo imprimimos también por consola
    try (
      BufferedWriter bufferedWriter = new BufferedWriter(
        new FileWriter(outputFile, true)
      )
    ) {
      bufferedWriter.write(output);
      bufferedWriter.write(System.getProperty("line.separator"));
      bufferedWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
