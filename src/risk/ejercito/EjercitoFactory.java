package risk.ejercito;

import risk.RiskColor;

/**
 * Clase auxiliar que genera un ejército del tipo adecuado
 */
public abstract class EjercitoFactory {
    public static Ejercito getEjercito(RiskColor color) {
        switch(color) {
            case CYAN :
            return new EjercitoCyan();
            case ROJO :
            return new EjercitoRojo();
            case VIOLETA :
            return new EjercitoVioleta();
            case VERDE :
            return new EjercitoVerde();
            case AZUL :
            return new EjercitoAzul();
            case AMARILLO :
            return new EjercitoAmarillo();
            default:
            throw new IllegalArgumentException("No se acepta el color especificado.");
        }
    }
}
