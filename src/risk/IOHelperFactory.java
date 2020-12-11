package risk;

public abstract class IOHelperFactory {
    private static Consola iOHelperInstance;

    public static void setType(Class<?> clase) {
        if (clase == ConsolaConsola.class) {
            iOHelperInstance = new ConsolaConsola();
        } else if (clase == ConsolaArchivo.class) {
            iOHelperInstance = new ConsolaArchivo("output.txt", "comandos.txt");
        } else {
            throw new IllegalArgumentException("No se reconoce esa clase");
        }
    }

    public static Consola getInstance() {
        return iOHelperInstance;
    }
}
