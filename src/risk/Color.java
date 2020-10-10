package risk;

public enum Color {
    // El primer parámetro es el nombre del color, el segundo la secuencia para colorear texto de ese color, el tercero para colorear el fondo de ese color
    CYAN("cyan", "\033[0;36m", "\033[46m"), VERDE("verde", "\033[0;32m", "\033[42m"), AMARILLO("amarillo", "\033[0;33m", "\033[43m"),
    VIOLETA("violeta", "\033[0;35m", "\033[45m"), ROJO("rojo", "\033[0;31m", "\033[41m"), AZUL("azul", "\033[0;36m", "\033[44m"),
    INDEFINIDO((String) null, (String) null, (String) null);

    private final String nombre;
    private final String secTexto;
    private final String secFondo;

    private Color(String nombre, String secTexto, String secFondo) {
        this.nombre = nombre;
        this.secTexto = secTexto;
        this.secFondo = secFondo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getSecTexto() {
        return this.secTexto;
    }

    public String getSecFondo() {
        return this.secFondo;
    }

    public static Color getColorByString(String nombreColor) {
        for (Color c : values()) {
            if (c.nombre.equals(nombreColor.toLowerCase())) {
                return c;
            }
        }
        return Color.INDEFINIDO;
    }

    public static String getSecColorReset() {
        return "\033[0m";
    }
}