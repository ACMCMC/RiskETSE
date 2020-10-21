/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Partida {
    private static final Partida partidaSingleton = new Partida();

    private Map<String, Jugador> jugadores;

    public Partida() {
        this.jugadores = new HashMap<>();
    }

    public static Partida getPartida() {
        return(partidaSingleton);
    }

    /**
     * Devuelve una copia del Set de los Jugadores
     * @return
     */
    public Set<Jugador> getJugadores() {
        return jugadores.entrySet().parallelStream().map(entry -> {return (entry.getValue());}).collect(Collectors.toSet());
    }

    public void addJugador(Jugador jugador) {
        this.jugadores.put(jugador.getNombre(), jugador);
    }

    public Jugador getJugador(String nombre) {
        return(this.jugadores.get(nombre));
    }

    /**
     * Devuelve el jugador del Color especificado, o null
     * @param color
     * @return
     */
    public Jugador getJugador(Color color) {
        Optional<Jugador> jugador = this.jugadores.entrySet().parallelStream().map(entry -> {return (entry.getValue());}).filter(jug -> {return (jug.getColor().equals(color));}).findFirst();
        if (jugador.isPresent()) {
            return jugador.get();
        } else {
            return null;
        }
    }

    /**
     * Asigna el número de ejércitos sin repartir adecuado a cada jugador, de acuerdo con la especificación del proyecto
     */
    public void asignarEjercitosSinRepartir() {
        this.jugadores.entrySet().forEach(entry -> {
            entry.getValue().setEjercitosSinRepartir(50 - (5 * this.jugadores.size())); // 50 - (5 * numJugadores) es una fórmula que hace que para 3 jugadores, tengamos 35 ejércitos, 
        });
    }
}
