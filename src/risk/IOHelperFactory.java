package risk;

public abstract class IOHelperFactory {
    private static IOHelper iOHelperInstance;

    public static void setType(Class<?> clase) {
        if (clase == IOHelperConsole.class) {
            iOHelperInstance = new IOHelperConsole();
        } else if (clase == IOHelperFile.class) {
            iOHelperInstance = new IOHelperFile("output.txt", "comandos.txt");
        } else {
            throw new IllegalArgumentException("No se reconoce esa clase");
        }
    }

    public static IOHelper getInstance() {
        return iOHelperInstance;
    }
}
