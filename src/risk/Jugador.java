/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.Set;
import java.util.stream.Collectors;

import risk.Ejercito.EjercitoFactory;
import risk.RiskException.ExcepcionJugador;
import risk.RiskException.RiskExceptionEnum;

public class Jugador {
    private String nombre;
    private Color color;
    private int ejercitosSinRepartir; // Los ejércitos que tiene el jugador, pero no están repartidos. Los que sí
                                      // están repartidos se almacenan en el país en sí.

    public Jugador(String nombre, Color color) {
        this.setNombre(nombre);
        this.setColor(color);
        this.setEjercitosSinRepartir(0);
    }

    /**
     * De los ejércitos sin repartir que tiene el jugador, asignar
     * {@code numEjercitos} al país elegido.
     * 
     * @param numEjercitos los ejércitos que se van a asignar
     * @return el número de ejércitos que se han asignado
     */
    public int asignarEjercitosAPais(int numEjercitos, Pais pais) throws ExcepcionJugador {
        if (this.getEjercitosSinRepartir() >= numEjercitos) { // Tenemos suficientes ejércitos como para realizar la
                                                              // asignación
            for (int i = 0; i < numEjercitos; i++) {
                pais.addEjercito(EjercitoFactory.getEjercito(pais.getJugador().getColor()));
            }
            this.setEjercitosSinRepartir(this.getEjercitosSinRepartir() - numEjercitos);
            return (numEjercitos);
        } else if (this.getEjercitosSinRepartir() > 0) { // No tenemos todos los ejércitos que nos piden, pero sí
                                                         // podemos asignar todos los que quedan
            int ejercitosSinRepartir = this.getEjercitosSinRepartir(); // No es realmente necesario, pero me parece
                                                                       // buena práctica porque si modificásemos el
                                                                       // número de ejércitos sin repartir dentro del
                                                                       // for, entonces habría problemas
            for (int i = 0; i < ejercitosSinRepartir; i++) {
                pais.addEjercito(EjercitoFactory.getEjercito(pais.getJugador().getColor()));
            }
            this.setEjercitosSinRepartir(0);
            return (ejercitosSinRepartir);
        } else { // No hay ejércitos disponibles
            throw (ExcepcionJugador) RiskExceptionEnum.EJERCITOS_NO_DISPONIBLES.get();
        }
    }

    /**
     * Establece el número de ejércitos sin repartir de un jugador
     * 
     * @param ejercitosSinRepartir
     */
    public void setEjercitosSinRepartir(int ejercitosSinRepartir) {
        this.ejercitosSinRepartir = ejercitosSinRepartir;
    }

    /**
     * Devuelve el número de ejércitos que tiene el jugador, sin repartir
     * 
     * @return
     */
    public int getEjercitosSinRepartir() {
        return this.ejercitosSinRepartir;
    }

    /**
     * Devuelve un Set de los Paises de este Jugador
     */
    public Set<Pais> getPaises() {
        Set<Pais> paisesJugador = Mapa.getMapa().getPaises().parallelStream().filter(pais -> {
            return (pais.getJugador() != null ? pais.getJugador().equals(this) : false);
        }).collect(Collectors.toSet());
        return paisesJugador;
    }

    /**
     * Devuelve el total de ejércitos de este Jugador, buscando por todos sus países
     * 
     * @return
     */
    public int getTotalEjercitos() {
        int totalEjercitos = this.getPaises().parallelStream().reduce(0, (accum, pais) -> {
            return (accum + pais.getNumEjercitos());
        }, Integer::sum);
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

    /**
     * Devuelve un Set de los Continentes en que este Jugador es el único presente
     * @return
     */
    public Set<Continente> getContinentesOcupadosExcusivamentePorJugador() {
        return this.getPaises().stream().map(p -> p.getContinente()).distinct().filter(c -> c.getPaises().stream().allMatch(p -> p.getJugador().equals(this))).collect(Collectors.toSet());
    }

    public int calcularNumEjercitosRearmar() {
        int numEjercitosRearmar = this.getPaises().size()/3; // El jugador recibe el número de ejércitos que es el resultado de dividir el número de países que pertenecen al jugador entre 3
        numEjercitosRearmar += getContinentesOcupadosExcusivamentePorJugador().stream().mapToInt(Continente::getNumEjercitosRepartirCuandoOcupadoExcusivamentePorJugador).sum(); // Si todos los países de un continente pertenecen a dicho jugador, recibe el número de ejércitos indicados en la Tabla 4
        return numEjercitosRearmar;
    }

    @Override
    public boolean equals(Object jugador) {
        if (jugador == null) {
            return false;
        }
        if (this == jugador) {
            return true;
        }
        if (getClass() != jugador.getClass()) {
            return false;
        }
        final Jugador other = (Jugador) jugador;
        if (!this.getNombre().equals(other.getNombre())) {
            return false;
        }
        if (!this.getColor().equals(other.getColor())) {
            return false;
        }
        return true;
    }
}
