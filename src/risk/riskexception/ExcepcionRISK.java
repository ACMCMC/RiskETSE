/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk.riskexception;

/**
 * Clase base que representa una excepción del RISK
 */
public abstract class ExcepcionRISK extends Exception {

    private static final long serialVersionUID = 1L; // No tiene funcionalidad, sólo evita el warning

    final int codigo;
    final String codigoTexto;

    public ExcepcionRISK(int codigo, String codigoTexto) {
        this.codigo = codigo;
        this.codigoTexto = codigoTexto;
    }

    private int getCodigo() {
        return this.codigo;
    }

    private String getCodigoTexto() {
        return this.codigoTexto;
    }

    @Override
    public String toString() {
        return risk.OutputBuilder.beginBuild().autoAdd("código de error", this.getCodigo())
                .autoAdd("descripción", this.getCodigoTexto()).build();
    }

    public String getMessage() {
        return this.getCodigoTexto();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExcepcionRISK)) {
            return false;
        }
        if (((ExcepcionRISK) o).getCodigo() != this.getCodigo()) {
            return false;
        }
        return true;
    }
}
