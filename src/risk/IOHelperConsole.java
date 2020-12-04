package risk;

import java.util.Scanner;

public class IOHelperConsole implements IOHelper {

    private Scanner scanner;

    IOHelperConsole() {
        scanner = new Scanner(System.in);
    }
    
    @Override
    public String readLine() {
        System.out.print(IOHelper.PROMPT);
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
        // TODO Auto-generated method stub
        System.out.println("EOF");
    }
}
