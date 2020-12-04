package risk;

public interface IOHelper {

    public static final String PROMPT = "$> ";

    public String readLine();

    public void printToErrOutput(Throwable th);

    public void printToOutput(String output);

    public void escribirFinComandos();
}
