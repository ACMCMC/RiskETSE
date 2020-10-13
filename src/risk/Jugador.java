/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Jugador {
    private String nombre;
    private Color color;

    public Jugador(String nombre, Color color) {
        this.setNombre(nombre);
        this.setColor(color);
    }

    public String getNombre() {
        return this.nombre;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Color getColor() {
        return this.color;
    }

    private void setColor(Color color) {
        this.color = color;
    }
}
