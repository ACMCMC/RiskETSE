package risk;

public class Continente {
    private Color color;
    private String nombre;

    public Continente(String nombre, Color color) {
        this.setColor(color);
        this.setNombre(nombre);
    }

    public Color getColor() {
        return this.color;
    }

    private void setColor(Color color) {
        this.color = color;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }
}
