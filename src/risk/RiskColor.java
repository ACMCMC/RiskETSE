/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import javafx.scene.paint.Color;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.RiskExceptionEnum;

/**
 * Representa un Color en el juego
 */
public enum RiskColor {
    // El primer parámetro es el nombre del color, el segundo la secuencia para
    // colorear texto de ese color, el tercero para colorear el fondo de ese color
    CYAN("CYAN", "\033[0;36m", "\033[46m", Color.CYAN), VERDE("VERDE", "\033[0;32m", "\033[42m", Color.GREEN),
    AMARILLO("AMARILLO", "\033[0;33m", "\033[43m", Color.YELLOW), VIOLETA("VIOLETA", "\033[0;35m", "\033[45m", Color.PURPLE),
    ROJO("ROJO", "\033[0;31m", "\033[41m", Color.RED), AZUL("AZUL", "\033[0;36m", "\033[44m", Color.BLUE),
    INDEFINIDO("INDEFINIDO", "", "", Color.BLACK);

    private final String nombre;
    private final String secTexto;
    private final String secFondo;
    private final Color fxColor;

    private RiskColor(String nombre, String secTexto, String secFondo, Color fxColor) {
        this.nombre = nombre;
        this.secTexto = secTexto;
        this.secFondo = secFondo;
        this.fxColor = fxColor;
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

    public Color getFxColor() {
        return fxColor;
    }

    public static RiskColor getColorByString(String nombreColor) throws ExcepcionGeo {
        for (RiskColor c : values()) {
            if (c.nombre.equals(nombreColor.toUpperCase())) {
                return c;
            }
        }
        throw (ExcepcionGeo) RiskExceptionEnum.COLOR_NO_PERMITIDO.get();
    }

    public static String getSecColorReset() {
        return "\033[0m";
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
}
