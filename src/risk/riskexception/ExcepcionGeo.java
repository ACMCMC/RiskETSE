package risk.riskexception;

/**
 * Excepciones relacionadas con la geografía del Mapa
 */
public class ExcepcionGeo extends ExcepcionRISK {

    private static final long serialVersionUID = 1L; // No tiene funcionalidad, sólo evita el warning

    ExcepcionGeo(int codigo, String texto) {
        super(codigo, texto);
    }
}
