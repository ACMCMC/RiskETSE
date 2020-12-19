package risk.riskexception;

/**
 * Fábrica de excepciones del Risk
 */
public class RiskExceptionFactory {

    public static ExcepcionRISK fromCode(int code) {
        switch (code) {
            case (99):
                return (new ExcepcionComando(code, "Comando no permitido en este momento"));
            case (100):
                return (new ExcepcionGeo(code, "Color no permitido"));
            case (101):
                return (new ExcepcionComando(code, "Comando incorrecto"));
            case (102):
                return (new ExcepcionGeo(code, "El continente no existe"));
            case (103):
                return (new ExcepcionJugador(code, "El jugador no existe"));
            case (104):
                return (new ExcepcionJugador(code, "El jugador ya existe"));
            case (105):
                return (new ExcepcionJugador(code, "Los jugadores no están creados"));
            case (106):
                return (new ExcepcionGeo(code, "El mapa no está creado"));
            case (107):
                return (new ExcepcionGeo(code, "El mapa ya ha sido creado"));
            case (109):
                return (new ExcepcionGeo(code, "El país no existe"));
            case (110):
                return (new ExcepcionJugador(code, "El país no pertenece al jugador"));
            case (111):
                return (new ExcepcionJugador(code, "El país pertenece al jugador"));
            case (112):
                return (new ExcepcionGeo(code, "Los países no son frontera"));
            case (113):
                return (new ExcepcionJugador(code, "El país ya está asignado"));
            case (114):
                return (new ExcepcionJugador(code, "El color ya está asignado"));
            case (115):
                return (new ExcepcionMision(code, "La misión ya está asignada"));
            case (116):
                return (new ExcepcionMision(code, "La misión no existe"));
            case (117):
                return (new ExcepcionMision(code, "El jugador ya tiene asignada una misión"));
            case (118):
                return (new ExcepcionMision(code, "Las misiones no están asignadas"));
            case (119):
                return (new ExcepcionJugador(code, "Ejércitos no disponibles"));
            case (120):
                return (new ExcepcionCarta(code, "No hay cartas suficientes"));
            case (121):
                return (new ExcepcionCarta(code, "No hay configuración de cambio"));
            case (122):
                return (new ExcepcionCarta(code, "Algunas cartas no pertenecen al jugador"));
            case (123):
                return (new ExcepcionCarta(code, "Algunas cartas no existen"));
            case (124):
                return (new ExcepcionJugador(code, "No hay ejércitos suficientes"));
            case (125):
                return (new ExcepcionCarta(code, "El identificador no sigue el formato correcto"));
            case (126):
                return (new ExcepcionCarta(code, "Carta de equipamiento ya asignada"));
            default:
                throw new IllegalArgumentException("El código especificado no es correcto"); // Si especificamos un código incorrecto
        }
    }

}
