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
import risk.RiskException.ExcepcionJugador;
import risk.RiskException.RiskException;
import risk.RiskException.RiskExceptionEnum;
import risk.RiskException.RiskExceptionFactory;

public class Partida {
    private static final Partida partidaSingleton = new Partida();

    private Map<String, Jugador> jugadores;
    private Queue<Jugador> colaJugadores;
    private Map<Jugador, CartaMision> misionesJugadores;

    private Partida() {
        this.jugadores = new HashMap<>();
        this.misionesJugadores = new HashMap<>();
        this.colaJugadores = new LinkedList<>();
    }

    public static Partida getPartida() {
        return (partidaSingleton);
    }

    /**
     * Devuelve una copia del Set de los Jugadores
     * 
     * @return
     */
    public Set<Jugador> getJugadores() {
        return jugadores.entrySet().parallelStream().map(entry -> {
            return (entry.getValue());
        }).collect(Collectors.toSet());
    }

    public void addJugador(Jugador jugador) {
        this.jugadores.put(jugador.getNombre(), jugador);
        this.colaJugadores.add(jugador);
    }

    /**
     * Devuelve un Jugador con el nombre especificado
     * 
     * @param nombre
     * @return
     */
    public Jugador getJugador(String nombre) throws ExcepcionJugador {
        if (this.jugadores.containsKey(nombre)) {
            return this.jugadores.get(nombre);
        } else {
            throw (ExcepcionJugador) RiskExceptionEnum.JUGADOR_NO_EXISTE.get();
        }
    }

    /**
     * Asigna una carta de misión a un Jugador, que tiene que estar registrado
     * 
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
     * 
     * @param color
     * @return
     */
    public Jugador getJugador(Color color) {
        Optional<Jugador> jugador = this.jugadores.entrySet().parallelStream().map(entry -> {
            return (entry.getValue());
        }).filter(jug -> {
            return (jug.getColor().equals(color));
        }).findFirst();
        if (jugador.isPresent()) {
            return jugador.get();
        } else {
            return null;
        }
    }

    /**
     * Asigna el número de ejércitos sin repartir adecuado a cada jugador, de
     * acuerdo con la especificación del proyecto
     */
    public void asignarEjercitosSinRepartir() {
        this.getJugadores().forEach(entry -> {
            entry.setEjercitosSinRepartir(50 - (5 * this.jugadores.size())); // 50 - (5 * numJugadores) es una fórmula
                                                                             // que hace que para 3 jugadores, tengamos
                                                                             // 35 ejércitos, y lo del pdf en general
        });
    }

    public Map<Pais, Set<Integer>> atacar(Pais atacante, Pais defensor) throws RiskException {

        if (atacante.getNumEjercitos() <= 1) {
            throw RiskExceptionEnum.NO_HAY_EJERCITOS_SUFICIENTES.get();
        }
        if (!Mapa.getMapa().getFrontera(atacante, defensor).isPresent()) {
            throw RiskExceptionEnum.PAISES_NO_SON_FRONTERA.get();
        }
        if (defensor.getJugador().equals(Partida.getPartida().getJugadorActual())) {
            throw RiskExceptionEnum.PAIS_PERTENECE_JUGADOR.get();
        }
        if (!atacante.getJugador().equals(Partida.getPartida().getJugadorActual())) {
            throw RiskExceptionEnum.PAIS_NO_PERTENECE_JUGADOR.get();
        }

        Set<Integer> valoresDadosAtacante = new HashSet<>();
        Set<Integer> valoresDadosDefensor = new HashSet<>();
        int numDadosAtacante;
        int numDadosDefensor;
        int valorAtacante;
        int valorDefensor;
        int ejercitosAtacados = 0; // El número de ejércitos con los que ataca el atacante, para después saber
                                   // cuántos hay que poner en el país defensor si es conquistado
        Map<Pais, Set<Integer>> mapaValores = new HashMap<>();
        Random rand = new Random();

        numDadosAtacante = atacante.getNumEjercitos() > 3 ? 3 : atacante.getNumEjercitos(); // La fórmula del PDF
        numDadosDefensor = defensor.getNumEjercitos() == 1 ? 1 : 2; // La fórmula del PDF

        // Generamos Sets de números que inicialmente están a 0
        Collections.addAll(valoresDadosAtacante, new Integer[numDadosAtacante]);
        Collections.addAll(valoresDadosDefensor, new Integer[numDadosDefensor]);

        // Asignamos valores a los números de los Sets
        valoresDadosAtacante = valoresDadosAtacante.stream().map(num -> rand.nextInt(6) + 1)
                .collect(Collectors.toSet());
        valoresDadosDefensor = valoresDadosDefensor.stream().map(num -> rand.nextInt(6) + 1)
                .collect(Collectors.toSet());

        mapaValores.put(atacante, new HashSet<Integer>(valoresDadosAtacante)); // Copiamos los valores de los Sets para
                                                                               // devolverlos después
        mapaValores.put(defensor, new HashSet<Integer>(valoresDadosDefensor));

        while (!valoresDadosAtacante.isEmpty() && !valoresDadosDefensor.isEmpty()
                && !(defensor.getNumEjercitos() == 0)) {
            valorAtacante = Collections.max(valoresDadosAtacante);
            valorDefensor = Collections.max(valoresDadosDefensor);
            valoresDadosAtacante.remove(valorAtacante);
            valoresDadosDefensor.remove(valorDefensor);
            if (valorAtacante > valorDefensor) {
                defensor.removeEjercito();
                ejercitosAtacados++;
            } else {
                atacante.removeEjercito();
            }
        }
        if (defensor.getNumEjercitos() == 0) {
            defensor.setJugador(atacante.getJugador());
            while (ejercitosAtacados > 0 && atacante.getNumEjercitos() > 1) { // Trasladamos el número de ejércitos con
                                                                              // los que hemos atacado al defensor
                Ejercito ejercitoTrasladar = atacante.getEjercitos().iterator().next();
                defensor.addEjercito(ejercitoTrasladar);
                atacante.removeEjercito(ejercitoTrasladar);
            }
        }
        return mapaValores;
    }

    /**
     * Devuelve el Jugador cuyo turno es ahora
     * 
     * @return
     */
    public Jugador getJugadorActual() {
        return this.colaJugadores.peek();
    }

    public void siguienteTurno() {
        this.colaJugadores.add(this.colaJugadores.poll());
    }
}
