/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.Set;
import java.util.stream.Collectors;

public class Jugador {
    private String nombre;
    private Color color;
    private int ejercitosSinRepartir; // Los ejércitos que tiene el jugador, pero no están repartidos. Los que sí están repartidos se almacenan en el país en sí.

    public Jugador(String nombre, Color color) {
        this.setNombre(nombre);
        this.setColor(color);
        this.setEjercitosSinRepartir(0);
    }

    /**
     * De los ejércitos sin repartir que tiene el jugador, asignar {@code numEjercitos} al país elegido.
     * @param numEjercitos los ejércitos que se van a asignar
     */
    public void asignarEjercitosAPais(int numEjercitos, Pais pais) {
        if (this.getEjercitosSinRepartir() >= numEjercitos) { // Tenemos suficientes ejércitos como para realizar la asignación
            for (int i = 0; i < numEjercitos; i++) {
                pais.addEjercito(new Ejercito());
            }
            this.setEjercitosSinRepartir(this.getEjercitosSinRepartir() - numEjercitos);
        } else if (this.getEjercitosSinRepartir() > 0) { // No tenemos todos los ejércitos que nos piden, pero sí podemos asignar todos los que quedan
            int ejercitosSinRepartir = this.getEjercitosSinRepartir(); // No es realmente necesario, pero me parece buena práctica porque si modificásemos el número de ejércitos sin repartir dentro del for, entonces habría problemas
                    for (int i = 0; i < ejercitosSinRepartir; i++) {
                        pais.addEjercito(new Ejercito());
                    }
            this.setEjercitosSinRepartir(0);
        } else { // No hay ejércitos disponibles
            FileOutputHelper.printToErrOutput(new RiskException(RiskException.RiskExceptionEnum.EJERCITOS_NO_DISPONIBLES).toString());
        }
    }

    /**
     * Establece el número de ejércitos sin repartir de un jugador
     * @param ejercitosSinRepartir
     */
    public void setEjercitosSinRepartir(int ejercitosSinRepartir) {
        this.ejercitosSinRepartir = ejercitosSinRepartir;
    }

    /**
     * Devuelve el número de ejércitos que tiene el jugador, sin repartir
     * @return
     */
    public int getEjercitosSinRepartir() {
        return this.ejercitosSinRepartir;
    }

    /**
     * Devuelve un Set de los Paises de este Jugador
     */
    public Set<Pais> getPaises() {
        Set<Pais> paisesJugador = Mapa.getMapa().getPaises().parallelStream().filter(pais -> {return(pais.getJugador().isPresent() ? pais.getJugador().get().equals(this) : false);}).collect(Collectors.toSet());
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
