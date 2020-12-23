package risk;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Implementación de la Consola, leyendo y escribiendo a consola
 */
public class ConsolaNormal implements Consola {

    private Scanner scanner;

    ConsolaNormal() {
        scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }
    
    @Override
    public String readLine() {
        System.out.print(Consola.PROMPT);
        return scanner.nextLine();
    }

    @Override
    public void printToErrOutput(Throwable th) {
        System.out.println(th.toString());
    }

    @Override
    public void printToOutput(String output) {
        System.out.println(output);
    }

    @Override
    public void escribirFinComandos() {
        System.out.println("EOF");
    }
}
