/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Pais {
    private String nombre;
    private Continente continente;

    Pais(String nombre, Continente continente) {
        this.setNombre(nombre);
        this.setContinente(continente);
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
		this.nombre = nombre;
    }
    
    public void setContinente(Continente continente) {
		this.continente = continente;
    }
    
    public Continente getContinente() {
        return this.continente;
    }

}
