/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import risk.CartasMision.CartaMision;

public class Partida {

    private Map<String, Jugador> jugadores;
    private Queue<Jugador> colaJugadores;
    private Map<Jugador,CartaMision> misionesJugadores;

    private Partida() {
        this.jugadores = new HashMap<>();
        this.misionesJugadores = new HashMap<>();
        this.colaJugadores = new LinkedList<>();
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
        this.colaJugadores.add(jugador);
    }

    /**
     * Devuelve un Jugador con el nombre especificado
     * @param nombre
     * @return
     */
    public Optional<Jugador> getJugador(String nombre) {
        return(Optional.ofNullable(this.jugadores.get(nombre)));
    }

    /**
     * Asigna una carta de misión a un Jugador, que tiene que estar registrado
     * @param cartaMision
     * @param jugador
     */
    public void asignarCartaMisionJugador(CartaMision cartaMision, Jugador jugador) {
        if (jugadores.containsValue(jugador)) { // Podría darse el caso de que el Jugador no esté en la Partida
            this.misionesJugadores.put(jugador, cartaMision);
        }
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
      )
      .collect(Collectors.toSet());
  }

  public void addJugador(Jugador jugador) {
    this.jugadores.put(jugador.getNombre(), jugador);
    this.colaJugadores.add(jugador);
  }

  public Optional<Jugador> getJugador(String nombre) {
    return (Optional.ofNullable(this.jugadores.get(nombre)));
  }

  /**
   * Devuelve el jugador del Color especificado, o null
   * @param color
   * @return
   */
  public Jugador getJugador(Color color) {
    Optional<Jugador> jugador =
      this.jugadores.entrySet()
        .parallelStream()
        .map(
          entry -> {
            return (entry.getValue());
          }
        )
        .filter(
          jug -> {
            return (jug.getColor().equals(color));
          }
        )
        .findFirst();
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
    this.getJugadores()
      .forEach(
        entry -> {
          entry.setEjercitosSinRepartir(50 - (5 * this.jugadores.size())); // 50 - (5 * numJugadores) es una fórmula que hace que para 3 jugadores, tengamos 35 ejércitos, y lo del pdf en general
        }
        if (defensor.getNumEjercitos()==0) {
            defensor.setJugador(atacante.getJugador());
            while (ejercitosAtacados > 0 && atacante.getNumEjercitos() > 1) { // Trasladamos el número de ejércitos con los que hemos atacado al defensor
                Ejercito ejercitoTrasladar = atacante.getEjercitos().iterator().next();
                defensor.addEjercito(ejercitoTrasladar);
                atacante.removeEjercito(ejercitoTrasladar);
            }
        }
        return mapaValores;
    }

    /**
     * Devuelve el Jugador cuyo turno es ahora
     * @return
     */
    public Jugador getJugadorActual() {
        return this.colaJugadores.peek();
    }
    if (defensor.getNumEjercitos() == 0) {
      defensor.setJugador(atacante.getJugador().get());
      while (ejercitosAtacados > 0 && atacante.getNumEjercitos() > 1) { // Trasladamos el número de ejércitos con los que hemos atacado al defensor
        Ejercito ejercitoTrasladar = atacante.getEjercitos().iterator().next();
        defensor.addEjercito(ejercitoTrasladar);
        atacante.removeEjercito(ejercitoTrasladar);
      }
    }
    return mapaValores;
  }

  /**
   * Devuelve el Jugador cuyo turno es ahora
   * @return
   */
  public Jugador getJugadorActual() {
    return this.colaJugadores.peek();
  }

  public void siguienteTurno() {
    this.colaJugadores.add(this.colaJugadores.poll());
  }
}
