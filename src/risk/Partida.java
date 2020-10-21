/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.HashMap;
import java.util.Map;

public class Partida {
    private static final Partida partidaSingleton = new Partida();

    private Map<String, Jugador> jugadores;

    public Partida() {
        this.jugadores = new HashMap<>();
    }

    public static Partida getPartida() {
        return(partidaSingleton);
    }

    public void addJugador(Jugador jugador) {
        this.jugadores.put(jugador.getNombre(), jugador);
    }

    public Jugador getJugador(String nombre) {
        return(this.jugadores.get(nombre));
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
