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
    
    @Override
    public boolean equals(Object jugador){
        if (this==jugador){
            return true;
        }
        if (jugador==null){
            return false;
        }
        if(getClass() != jugador.getClass()){
            return false;
        }
        final Jugador other = (Jugador) jugador;
        if(!this.getNombre().equals(other.getNombre())){
            return false;
        }
        if(!this.getColor().equals(other.getColor())){
            return false;
        }
        return true;
    }
}
