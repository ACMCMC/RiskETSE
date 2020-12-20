package risk;

/**
 * Interfaz Consola, que contiene funciones de lectura y escritura
 */
public interface Consola {

    public static final String PROMPT = "$> ";

    public String readLine();

    public void printToErrOutput(Throwable th);

    public void printToOutput(String output);

    public void escribirFinComandos();
}
