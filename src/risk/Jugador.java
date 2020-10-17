/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.Set;
import java.util.stream.Collectors;

public class Jugador {
    private String nombre;
    private Color color;

    public Jugador(String nombre, Color color) {
        this.setNombre(nombre);
        this.setColor(color);
    }

    /**
     * Devuelve un Set de los Paises de este Jugador
     */
    public Set<Pais> getPaises() {
        Set<Pais> paisesJugador = Mapa.getMapa().getPaises().parallelStream().filter(pais -> {return(pais.getJugador().equals(this));}).collect(Collectors.toSet());
        return paisesJugador;
    }

    /**
     * Devuelve el total de ejércitos de este Jugador, buscando por todos sus países
     * @return
     */
    public int getTotalEjercitos() {
        int totalEjercitos = this.getPaises().parallelStream().reduce(0, (accum, pais) -> {return (accum + pais.getNumEjercitos());}, Integer::sum);
        return totalEjercitos;
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
