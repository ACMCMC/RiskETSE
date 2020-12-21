package risk.riskexception;

/**
 * Excepciones relacionadas con los Jugadores o sus estados
 */
public class ExcepcionJugador extends ExcepcionRISK {

    private static final long serialVersionUID = 1L; // No tiene funcionalidad, sólo evita el warning

    ExcepcionJugador(int codigo, String codigoTexto) {
        super(codigo, codigoTexto);
    }
    
}
