package risk.riskexception;

/**
 * Excepciones relacionadas con las Cartas de equipamiento
 */
public class ExcepcionCarta extends ExcepcionRISK {

    private static final long serialVersionUID = 1L; // No tiene funcionalidad, sólo evita el warning

    ExcepcionCarta(int codigo, String codigoTexto) {
        super(codigo, codigoTexto);
    }
    
}
