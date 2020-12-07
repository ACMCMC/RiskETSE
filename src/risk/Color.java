/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import risk.riskexception.ExcepcionGeo;
import risk.riskexception.RiskExceptionEnum;

public enum Color {
    // El primer parámetro es el nombre del color, el segundo la secuencia para
    // colorear texto de ese color, el tercero para colorear el fondo de ese color
    CYAN("CYAN", "\033[0;36m", "\033[46m"), VERDE("VERDE", "\033[0;32m", "\033[42m"),
    AMARILLO("AMARILLO", "\033[0;33m", "\033[43m"), VIOLETA("VIOLETA", "\033[0;35m", "\033[45m"),
    ROJO("ROJO", "\033[0;31m", "\033[41m"), AZUL("AZUL", "\033[0;36m", "\033[44m"),
    INDEFINIDO("INDEFINIDO", "", "");

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

    public static Color getColorByString(String nombreColor) throws ExcepcionGeo {
        for (Color c : values()) {
            if (c.nombre.equals(nombreColor.toUpperCase())) {
                return c;
            }
        }
        throw (ExcepcionGeo) RiskExceptionEnum.COLOR_NO_PERMITIDO.get();
    }

    public static String getSecColorReset() {
        return "\033[0m";
    }
}
