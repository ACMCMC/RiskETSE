package risk.riskexception;

/**
 * Excepciones relacionadas con la introducción de comandos
 */
public class ExcepcionComando extends ExcepcionRISK {

    private static final long serialVersionUID = 1L; // No tiene funcionalidad, sólo evita el warning

    ExcepcionComando(int codigo, String codigoTexto) {
        super(codigo, codigoTexto);
    }
    
}
