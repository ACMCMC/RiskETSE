package risk.riskexception;

/**
 * Excepciones relacionadas con Misiones
 */
public class ExcepcionMision extends ExcepcionRISK {

    private static final long serialVersionUID = 1L; // No tiene funcionalidad, sólo evita el warning

    ExcepcionMision(int codigo, String codigoTexto) {
        super(codigo, codigoTexto);
    }
    
}
