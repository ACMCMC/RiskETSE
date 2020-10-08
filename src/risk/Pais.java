/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Pais {
    private String nombre;

    Pais(String nombre) {
        this.setNombre(nombre);
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
