/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Pais {
    private String codigo;
    private String nombreHumano;
    private Continente continente;

    Pais(String nombre, String nombreHumano, Continente continente) {
        this.setCodigo(nombre);
        this.setNombreHumano(nombreHumano);
        this.setContinente(continente);
    }

    /**
     * Devuelve el nombre humano del país
     * @return
     */
    public String getNombreHumano() {
        return this.nombreHumano;
    }

    private void setNombreHumano(String nombreHumano) {
    this.nombreHumano = nombreHumano;
    }

    /**
     * Devuelve el código del país
     * @return
     */
    public String getCodigo() {
        return this.codigo;
    }
    
    private void setCodigo(String codigo) {
		this.codigo = codigo;
    }
    
    private void setContinente(Continente continente) {
		this.continente = continente;
    }
    
    /**
     * Devuelve el Continente asociado a este país
     * @return
     */
    public Continente getContinente() {
        return this.continente;
    }

    @Override
    public String toString() {
        return getCodigo();
    }

}
