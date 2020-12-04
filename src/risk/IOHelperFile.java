package risk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOHelperFile implements IOHelper {

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    IOHelperFile(String fileNameOutput, String fileNameInput) {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(fileNameOutput)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(fileNameInput)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readLine() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void printToErrOutput(Throwable th) {
        try {
            bufferedWriter.write(th.getMessage());
            bufferedWriter.write(System.getProperty("line.separator"));
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printToOutput(String output) {
        try {
            bufferedWriter.write(output);
            bufferedWriter.write(System.getProperty("line.separator"));
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void escribirFinComandos() {
        try {
            bufferedWriter.write("EOF");
            bufferedWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() {
        try {
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
