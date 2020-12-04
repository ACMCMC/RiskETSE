package risk;

public class IOHelperConsole implements IOHelper {

    @Override
    public String readLine() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void printToErrOutput(Throwable th) {
        System.out.println(th.getMessage());
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
