package risk.riskexception;

public class ExcepcionPropia extends ExcepcionRISK {

    private static final long serialVersionUID = 1L; // No tiene funcionalidad, sólo evita el warning

    ExcepcionPropia(int codigo, String codigoTexto) {
        super(codigo, codigoTexto);
    }
    
}
