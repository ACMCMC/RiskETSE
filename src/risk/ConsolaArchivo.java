package risk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConsolaArchivo implements Consola {

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    ConsolaArchivo(String fileNameOutput, String fileNameInput) {
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
        String linea;
        try {
            linea = bufferedReader.readLine();
            printToOutput(Consola.PROMPT + linea);
            return linea;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void printToErrOutput(Throwable th) {
        try {
            bufferedWriter.write(th.toString());
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
