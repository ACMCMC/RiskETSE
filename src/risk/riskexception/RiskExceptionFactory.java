package risk.riskexception;

/**
 * Fábrica de excepciones del Risk
 */
public class RiskExceptionFactory {

    public static ExcepcionRISK fromCode(int code, String codigoTexto) {


        switch (code) {
            case (99):
                return (new ExcepcionComando(code, codigoTexto));
            case (100):
                return (new ExcepcionGeo(code, codigoTexto));
            case (101):
                return (new ExcepcionComando(code, codigoTexto));
            case (102):
                return (new ExcepcionGeo(code, codigoTexto));
            case (103):
                return (new ExcepcionJugador(code, codigoTexto));
            case (104):
                return (new ExcepcionJugador(code, codigoTexto));
            case (105):
                return (new ExcepcionJugador(code, codigoTexto));
            case (106):
                return (new ExcepcionGeo(code, codigoTexto));
            case (107):
                return (new ExcepcionGeo(code, codigoTexto));
            case (109):
                return (new ExcepcionGeo(code, codigoTexto));
            case (110):
                return (new ExcepcionJugador(code, codigoTexto));
            case (111):
                return (new ExcepcionJugador(code, codigoTexto));
            case (112):
                return (new ExcepcionGeo(code, codigoTexto));
            case (113):
                return (new ExcepcionJugador(code, codigoTexto));
            case (114):
                return (new ExcepcionJugador(code, codigoTexto));
            case (115):
                return (new ExcepcionMision(code, codigoTexto));
            case (116):
                return (new ExcepcionMision(code, codigoTexto));
            case (117):
                return (new ExcepcionMision(code, codigoTexto));
            case (118):
                return (new ExcepcionMision(code, codigoTexto));
            case (119):
                return (new ExcepcionJugador(code, codigoTexto));
            case (120):
                return (new ExcepcionCarta(code, codigoTexto));
            case (121):
                return (new ExcepcionCarta(code, codigoTexto));
            case (122):
                return (new ExcepcionCarta(code, codigoTexto));
            case (123):
                return (new ExcepcionCarta(code, codigoTexto));
            case (124):
                return (new ExcepcionJugador(code, codigoTexto));
            case (125):
                return (new ExcepcionCarta(code, codigoTexto));
            case (126):
                return (new ExcepcionCarta(code, codigoTexto));
            default:
                return (new ExcepcionPropia(code, codigoTexto));
        }
    }

}
