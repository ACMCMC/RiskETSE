/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import risk.cartas.Carta;
import risk.cartas.CartaEquipamientoFactory;
import risk.cartasmision.CartaMision;
import risk.ejercito.Ejercito;
import risk.ejercito.EjercitoFactory;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

/**
 * Representa la partida. Guarda información sobre los jugadores y su estado.
 */
public class Partida {
    private static final Partida partidaSingleton = new Partida();

    private Map<String, Jugador> jugadores;
    private Queue<Jugador> colaJugadores;
    private Turno turnoActual;

    private Partida() {
        this.jugadores = new HashMap<>();
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
        return jugadores.entrySet().stream().map(entry -> {
            return (entry.getValue());
        }).collect(Collectors.toSet());
    }

    public void addJugador(Jugador jugador) throws ExcepcionJugador, ExcepcionGeo {
        if (!Mapa.isMapaCreado()) {
            throw (ExcepcionGeo) RiskExceptionEnum.MAPA_NO_CREADO.get();
        }
        if (this.jugadores.entrySet().stream().anyMatch(jug -> jug.getValue().getColor().equals(jugador.getColor()))) { // Si
                                                                                                                        // existe
                                                                                                                        // un
                                                                                                                        // jugador
                                                                                                                        // con
                                                                                                                        // el
                                                                                                                        // mismo
                                                                                                                        // color,
                                                                                                                        // lanzamos
                                                                                                                        // la
                                                                                                                        // excepción
                                                                                                                        // 114
            throw (ExcepcionJugador) RiskExceptionEnum.COLOR_YA_ASIGNADO.get();
        }
        if (this.jugadores.containsKey(jugador.getNombre())) { // Si el jugador ya existe, lanzamos la excepción 104
            throw (ExcepcionJugador) RiskExceptionEnum.JUGADOR_YA_EXISTE.get();
        }
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
        if (!this.areJugadoresCreados()) {
            throw (ExcepcionJugador) RiskExceptionEnum.JUGADORES_NO_CREADOS.get();
        }
        if (this.jugadores.containsKey(nombre)) {
            return this.jugadores.get(nombre);
        } else {
            throw (ExcepcionJugador) RiskExceptionEnum.JUGADOR_NO_EXISTE.get();
        }
    }

    /**
     * Devuelve el jugador del Color especificado, o null
     * 
     * @param color
     * @return
     */
    public Jugador getJugador(Color color) {
        Optional<Jugador> jugador = this.jugadores.entrySet().stream().map(entry -> {
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
            entry.setEjercitosRearme(50 - (5 * this.jugadores.size())); // 50 - (5 * numJugadores) es una fórmula
            // que hace que para 3 jugadores, tengamos
            // 35 ejércitos, y lo del pdf en general
        });
    }

    /**
     * Realiza un ataque entre dos países con los conjuntos de dados especificados
     * 
     * @param atacante
     * @param defensor
     * @return
     * @throws ExcepcionRISK
     */
    public Map<Pais, Set<Dado>> atacar(Pais atacante, Set<Dado> dadosAtacante, Pais defensor, Set<Dado> dadosDefensor)
            throws ExcepcionRISK {
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

        Map<Pais, Set<Dado>> mapaValores = new HashMap<>();
        Dado dadoAtacante;
        Dado dadoDefensor;
        int ejercitosAtacados = dadosAtacante.size(); // El número de ejércitos con los que ataca el atacante, para después saber
                                   // cuántos hay que poner en el país defensor si es conquistado

        
        mapaValores.put(atacante, dadosAtacante.stream().map(d -> {
            try {return new Dado(d.getValor());} catch (ExcepcionRISK e) {return null;}
        }).collect(Collectors.toSet())); // Copiamos los valores de los Sets para
                                                                                                // devolverlos después
        mapaValores.put(defensor, dadosDefensor.stream().map(d -> {
            try {return new Dado(d.getValor());} catch (ExcepcionRISK e) {return null;}
        }).collect(Collectors.toSet()));

        procesarDadosAtacante(atacante, dadosAtacante);

        while (!dadosAtacante.isEmpty() && !dadosDefensor.isEmpty() && !(defensor.getNumEjercitos() == 0)) {
            dadoAtacante = dadosAtacante.stream().max(Comparator.comparingInt(Dado::getValor)).get();
            dadoDefensor = dadosDefensor.stream().max(Comparator.comparingInt(Dado::getValor)).get();
            dadosAtacante.remove(dadoAtacante);
            dadosDefensor.remove(dadoDefensor);
            if (dadoAtacante.getValor() > dadoDefensor.getValor()) {
                defensor.removeEjercito();
            } else {
                atacante.removeEjercito();
            }
        }
        if (defensor.getNumEjercitos() == 0) {
            defensor.conquistar(atacante.getJugador());
            while (ejercitosAtacados > 0 && atacante.getNumEjercitos() > 1) { // Trasladamos el número de ejércitos con
                                                                              // los que hemos atacado al defensor
                Ejercito ejercitoTrasladar = atacante.getEjercitos().iterator().next();
                defensor.addEjercito(ejercitoTrasladar);
                atacante.removeEjercito(ejercitoTrasladar);
                ejercitosAtacados--;
            }
        }
        return mapaValores;
    }

    /**
     * Aplica el método atacar() de las clases de Ejercito en los dados
     */
    private void procesarDadosAtacante(Pais atacante, Set<Dado> dadosAtacante) {
        Ejercito ejercitoAtacante = EjercitoFactory.getEjercito(atacante.getJugador().getColor());

        Dado arrayDadosAtacanteProcesado[] = ejercitoAtacante.ataque((Dado[]) dadosAtacante.toArray(new Dado[0]));

        dadosAtacante.clear();
        Collections.addAll(dadosAtacante, arrayDadosAtacanteProcesado);
    }

    public Map<Pais, Set<Dado>> atacar(Pais atacante, Pais defensor) throws ExcepcionRISK {

        Set<Dado> dadosAtacante = new HashSet<>();
        Set<Dado> dadosDefensor = new HashSet<>();
        int numDadosAtacante;
        int numDadosDefensor;

        numDadosAtacante = atacante.getNumEjercitos() > 3 ? 3 : atacante.getNumEjercitos(); // La fórmula del PDF
        numDadosDefensor = defensor.getNumEjercitos() == 1 ? 1 : 2; // La fórmula del PDF

        // Generamos Sets de dados
        for (int i = 0; i < numDadosAtacante; i++) {
            dadosAtacante.add(new Dado());
        }
        for (int i = 0; i < numDadosDefensor; i++) {
            dadosDefensor.add(new Dado());
        }

        return atacar(atacante, dadosAtacante, defensor, dadosDefensor);
    }

    public void rearmar(Pais paisOrigen, Pais paisDestino, int numEjercitos) throws ExcepcionGeo, ExcepcionJugador {
        if (!(paisOrigen.getNumEjercitos()>1)) {
            throw (ExcepcionJugador) RiskExceptionEnum.NO_HAY_EJERCITOS_SUFICIENTES.get();
        }
        if (!Mapa.getMapa().getFrontera(paisOrigen, paisDestino).isPresent()) {
            throw (ExcepcionGeo) RiskExceptionEnum.PAISES_NO_SON_FRONTERA.get();
        }
        if (!Partida.getPartida().getJugadorActual().equals(paisOrigen.getJugador())) {
            throw (ExcepcionJugador) RiskExceptionEnum.PAIS_NO_PERTENECE_JUGADOR.get();
        }
        if (!Partida.getPartida().getJugadorActual().equals(paisDestino.getJugador())) {
            throw (ExcepcionJugador) RiskExceptionEnum.PAIS_NO_PERTENECE_JUGADOR.get();
        }

        if (paisOrigen.getNumEjercitos()>numEjercitos) { // El país de origen tiene todos los ejércitos que queremos transferir. Pasamos ese número de ejércitos.
            while (numEjercitos > 0) {
                Ejercito ejercitoTransferir;
                ejercitoTransferir = paisOrigen.getAnyEjercito();
                paisDestino.addEjercito(ejercitoTransferir);
                paisOrigen.removeEjercito(ejercitoTransferir);
                numEjercitos--;
            }
        } else { // El país de origen no tiene todos los ejércitos que queremos pasar. Pasamos todos sus ejércitos, menos 1
            while (paisOrigen.getNumEjercitos()>1) {
                Ejercito ejercitoTransferir;
                ejercitoTransferir = paisOrigen.getAnyEjercito();
                paisDestino.addEjercito(ejercitoTransferir);
                paisOrigen.removeEjercito(ejercitoTransferir);
            }
        }
    }

    /**
     * Devuelve el Jugador cuyo turno es ahora
     * 
     * @return
     */
    public Jugador getJugadorActual() {
        if (this.turnoActual!=null) {
            return this.turnoActual.getJugador();
        } else {
            return this.colaJugadores.peek();
        }
    }

    /**
     * Avanza el juego un turno
     */
    public void siguienteTurno() {
        if (turnoActual!=null) {
            Mapa.getMapa().getPaisEventPublisher().unsubscribe(turnoActual);
        }
        this.colaJugadores.add(this.colaJugadores.poll());
        this.turnoActual = new Turno(this.colaJugadores.peek());
        Mapa.getMapa().getPaisEventPublisher().subscribe(turnoActual);
        getJugadorActual().recalcularEjercitosRearme();
    }
    
    /**
     * Registra los manejadores de eventos del juego
     */
    public void iniciarJuego() {
        Mapa.getMapa().getPaisEventPublisher().subscribe(this.getTurnoActual());
        getJugadorActual().recalcularEjercitosRearme();
    }
    
    /**
     * Avanza el Turno de reparto (no recalcula el número de ejércitos que le tocan al jugador)
     */
    public void siguienteTurnoDeReparto() {
        this.colaJugadores.add(this.colaJugadores.poll());
    }

    /**
     * Asigna una CartaMision a un Jugador
     */
    public void asignarMisionAJugador(CartaMision cartaMision, Jugador jugador) throws ExcepcionRISK {
        if (!areJugadoresCreados()) {
            throw RiskExceptionEnum.JUGADORES_NO_CREADOS.get();
        }
        if (this.getJugadores().stream().anyMatch(j -> j.hasMision(cartaMision))) {
            throw RiskExceptionEnum.MISION_YA_ASIGNADA.get();
        }
        jugador.addCartaMision(cartaMision);
    }

    private Turno getTurnoActual() {
        if (this.turnoActual==null) {
            this.turnoActual = new Turno(this.colaJugadores.peek());
        }
        return this.turnoActual;
    }

    /**
     * Asigna una Carta de equipamiento al Jugador del Turno actual
     */
    public Carta asignarCartaEquipamiento(String idCarta) throws ExcepcionRISK {
        return this.getTurnoActual().asignarCartaEquipamiento(idCarta);
    }

    /**
     * Reparte el número seleccionado de Ejercitos al Pais
     */
    public int repartirEjercitos(int numero, Pais pais) throws ExcepcionJugador {
        if (!getJugadorActual().hasEjercitosSinRepartir()) {
            throw (ExcepcionJugador) RiskExceptionEnum.EJERCITOS_NO_DISPONIBLES.get();
        }
        if (!getJugadorActual().equals(pais.getJugador())) { // Si el país no pertenece al jugador actual
            throw (ExcepcionJugador) RiskExceptionEnum.PAIS_NO_PERTENECE_JUGADOR.get();
        }
        return pais.getJugador().asignarEjercitosAPais(numero, pais);
    }

    public int rearmarEjercitos(int numero, Pais pais) throws ExcepcionJugador {
        if (!getJugadorActual().equals(pais.getJugador())) { // Si el país no pertenece al jugador actual
            throw (ExcepcionJugador) RiskExceptionEnum.PAIS_NO_PERTENECE_JUGADOR.get();
        }
        return pais.getJugador().asignarEjercitosAPais(numero, pais);
    }

    /**
     * Indica si los jugadores han sido creados o no
     */
    public boolean areJugadoresCreados() {
        return (this.jugadores.size() >= 3);
    }

    /**
     * Indica si las misiones han sido asignadas o no (todos los jugadores tienen una misión)
     */
    public boolean areMisionesAsignadas() {
        return (this.getJugadores().stream().allMatch(j -> !j.getCartasMision().isEmpty()));
    }

    /**
     * Indica si todos los jugadores han repartido sus ejércitos o no
     */
    public boolean areEjercitosRepartidos() {
        return (this.getJugadores().stream().allMatch(j -> !j.hasEjercitosSinRepartir()));
    }

    /**
     * Una tupla que representa Continentes, Jugadores, el numero de paises de ese
     * jugador dentro del continente, y el porcentaje de países del continente que
     * posee ese jugador
     */
    private static class TuplaContinenteJugadorPorcentaje {
        final Continente c;
        final Jugador j;
        final int n;
        final float p;

        TuplaContinenteJugadorPorcentaje(Continente c, Jugador j, int n, float p) {
            this.c = c;
            this.j = j;
            this.n = n;
            this.p = p;
        }

        Continente getContinente() {
            return c;
        }

        Jugador getJugador() {
            return j;
        }

        int getNumPaises() {
            return n;
        }

        float getPorcentaje() {
            return p;
        }
    }

    public void repartirEjercitos() throws ExcepcionGeo {
        /*
         * R1 Si inicialmente existe un continente en el que más del 50% de los países
         * están ocupados por un mismo jugador, entonces en cada país se colocará
         * automáticamente el siguiente número de ejércitos de dicho jugador: #ejercitos
         * = ejercitos_disponibles/(factor_division ∗ numero_paises_ocupados) donde
         * factor_división es 1,5 si el continente es Oceanía o América del Sur y 1 para
         * el resto de los continentes.
         */

        Set<TuplaContinenteJugadorPorcentaje> tuplas = obtenerTuplasContinenteJugadorPorcentaje();
        if (!aplicarReglasPorcentajes(tuplas, tupla -> tupla.getPorcentaje() >= 0.5, tupla -> {
            try {
                if (tupla.getContinente().equals(Mapa.getMapa().getContinente("Oceanía"))
                        || tupla.getContinente().equals(Mapa.getMapa().getContinente("AméricaSur"))) {
                    return new Float(1.5);
                }
            } catch (ExcepcionGeo e) {
            }
            return new Float(1);
        })) {
            aplicarReglasPorcentajes(tuplas, tupla -> tupla.getPorcentaje() >= 0.25 && tupla.getPorcentaje() < 0.5,
                    tupla -> new Float(2));
        }

        Map<Jugador, List<TuplaContinenteJugadorPorcentaje>> tuplasJugs = tuplas.stream()
                .collect(Collectors.groupingBy(TuplaContinenteJugadorPorcentaje::getJugador));
        // Un Map que relaciona Jugadores con sus tuplas
        tuplasJugs.entrySet().stream()
                .filter(entry -> entry.getValue().stream().allMatch(tupla -> tupla.getPorcentaje() < 0.25))
                // Nos quedamos solo con las entradas del Map en las que todas las tuplas dicen
                // que el jugador tiene menos del 25% del porcentaje (es decir, solo nos
                // quedamos con los jugadores que tienen todos los porcentajes de menos del 25%)
                .forEach(entrada -> { // Por cada jugador...
                    entrada.getValue().forEach(tupla -> {
                        // Por cada tupla (continente, porque el jugador va a ser el mismo en cada
                        // iteración de este bucle)...
                        int numEjercitos = (Partida.getPartida().getJugadores().size() < 5 ? 2 : 3)
                                * tupla.getNumPaises();
                        // La regla del PDF de cuántos países hay que asignar (R7)
                        tupla.getContinente().getPaises().stream()
                                .filter(pais -> pais.getJugador().equals(tupla.getJugador())).forEach(pais -> {
                                    try {
                                        tupla.getJugador().asignarEjercitosAPais(numEjercitos, pais);
                                    } catch (ExcepcionJugador e) {
                                    }
                                });
                        // Le asignamos numEjercitos a todos los países del jugador, en ese continente
                    });
                });

        /*
         * Si después de haber aplicado la regla R7 aún queda ejércitos disponibles,
         * entonces se colocará 1 ejército en cada uno de los países que tienen un único
         * ejército, priorizando aquellos países que pertenecen a continentes con menos
         * países frontera.
         */
        asignar1EjercitoAPaisesCon1Ejercito(Mapa.getMapa().getContinentes());
    }

    private void asignar1EjercitoAPaisesCon1Ejercito(Set<Continente> setContinentes) {
        PriorityQueue<Continente> colaAsignar = new PriorityQueue<>(new Comparator<Continente>() {

            @Override
            public int compare(Continente o1, Continente o2) {
                int numFronterasO1 = Mapa.getMapa().getNumFronterasIntercontinentales(o1);
                int numFronterasO2 = Mapa.getMapa().getNumFronterasIntercontinentales(o2);
                return (numFronterasO1 == numFronterasO2 ? 0 : numFronterasO1 > numFronterasO2 ? -1 : 1);
            }

        }.reversed());
        colaAsignar.addAll(setContinentes); // Ponemos los continentes en una cola de prioridad, ordenándolos por los
        // que tengan menos fronteras
        Stream.generate(colaAsignar::poll);
        colaAsignar.stream().sorted(colaAsignar.comparator()).forEach(continente -> {
            continente.getPaises().stream().filter(pais -> pais.getEjercitos().size() == 1).forEach(pais -> {
                try {
                    pais.getJugador().asignarEjercitosAPais(1, pais);
                } catch (ExcepcionJugador e) {
                }
            });
        });
    }

    private boolean aplicarReglasPorcentajes(Set<TuplaContinenteJugadorPorcentaje> setTuplas,
            Predicate<TuplaContinenteJugadorPorcentaje> predicadoFiltrado,
            Function<TuplaContinenteJugadorPorcentaje, Float> factorDivision) {

        Set<TuplaContinenteJugadorPorcentaje> tuplasFiltradas = setTuplas.stream().filter(predicadoFiltrado)
                .collect(Collectors.toSet()); // Aplicamos el predicado de filtrado a las tuplas

        if (tuplasFiltradas.isEmpty()) {
            return false; // Ninguna tupla ha cumplido las condiciones, así que no se ha aplicado la
            // regla. Devolvemos false.
        }

        tuplasFiltradas.forEach(tupla -> {
            int numEjercitos = (int) Math.round(((float) tupla.getJugador().getNumEjercitosRearme())
                    / (factorDivision.apply(tupla) * (float) tupla.getNumPaises())); // Calculo el número de ejércitos
                                                                                     // que hay que asignar a cada uno
                                                                                     // de los países
            tupla.getJugador().getPaises().stream().filter(pais -> pais.getContinente().equals(tupla.getContinente()))
                    .forEach(pais -> {
                        try {
                            tupla.getJugador().asignarEjercitosAPais(numEjercitos, pais);
                        } catch (ExcepcionJugador e) {
                        }
                    });
        });

        if (tuplasFiltradas.size() > 1) { // Si hay varios continentes que cumplen la condición... (R2)

            float procentajeMaximo = tuplasFiltradas.stream()
                    .max(Comparator.comparing(TuplaContinenteJugadorPorcentaje::getPorcentaje)).get().getPorcentaje();
            // Obtenemos el porcentaje máximo de las tuplas que resultaron del filtrado, ya
            // que sólo vamos a aplicar la regla en un continente

            tuplasFiltradas = tuplasFiltradas.stream().filter(tupla -> {
                return (Float.compare(tupla.getPorcentaje(), procentajeMaximo) == 0);
            }).collect(Collectors.toSet());
            // Ahora sólo nos quedan las tuplas de continentes con jugadores que cumplen el
            // predicado de filtrado y están empatadas entre sí (puede haber varios
            // jugadores dentro de la misma tupla)

            Continente continenteMenosFronteras = tuplasFiltradas.stream()
                    .min(new Comparator<TuplaContinenteJugadorPorcentaje>() {
                        @Override
                        public int compare(TuplaContinenteJugadorPorcentaje o1, TuplaContinenteJugadorPorcentaje o2) {
                            int numFronterasO1 = Mapa.getMapa().getNumFronterasIntercontinentales(o1.getContinente());
                            int numFronterasO2 = Mapa.getMapa().getNumFronterasIntercontinentales(o2.getContinente());
                            return (numFronterasO1 == numFronterasO2 ? 0 : numFronterasO1 > numFronterasO2 ? -1 : 1);
                        }
                    }.reversed()).get().getContinente();
            // Buscamos una de las tuplas con continente con menos fronteras, y nos quedamos
            // con ese Continente

            tuplasFiltradas = tuplasFiltradas.stream()
                    .filter(tupla -> tupla.getContinente().equals(continenteMenosFronteras))
                    .collect(Collectors.toSet());
            // Ahora sólo nos quedan las tuplas de continentes con jugadores que cumplen el
            // predicado de filtrado y están empatadas entre sí (puede haber varios
            // jugadores dentro de la misma tupla, incluso si no hay continentes empatados
            // entre sí), y de los posibles continentes empatados, el que tiene menos
            // fronteras.

            tuplasFiltradas.forEach(tupla -> {
                int numEjercitos = (int) Math.round(((float) tupla.getJugador().getNumEjercitosRearme())
                        / (factorDivision.apply(tupla) * (float) tupla.getNumPaises())); // Calculo el número de
                                                                                         // ejércitos que hay que
                                                                                         // asignar a cada uno de los
                                                                                         // países
                tupla.getJugador().getPaises().stream()
                        .filter(pais -> pais.getContinente().equals(tupla.getContinente())).forEach(pais -> {
                            try {
                                tupla.getJugador().asignarEjercitosAPais(numEjercitos, pais);
                            } catch (ExcepcionJugador e) {
                            }
                        });
            });
        }

        asignar1EjercitoAPaisesCon1Ejercito(Mapa.getMapa().getContinentes());

        return true; // Sí se ha aplicado la regla
    }

    /**
     * Elabora tuplas de la forma (Continente, Jugador, Numero de paises,
     * Porcentaje). Expresan que el jugador tiene un determinado porcentaje de los
     * países del Continente de la tupla
     * 
     * @return un Set de Tuplas
     */
    private Set<TuplaContinenteJugadorPorcentaje> obtenerTuplasContinenteJugadorPorcentaje() {
        Set<TuplaContinenteJugadorPorcentaje> tuplas = Mapa.getMapa().getContinentes().stream()
                .map(continente -> { // Por cada continente...
                    List<Jugador> listaJugadoresContinente = continente.getPaises().stream()
                            .map(pais -> pais.getJugador()).collect(Collectors.toList()); // Elaboro una lista de
                                                                                          // los jugadores de ese
                                                                                          // continente; eso lo
                                                                                          // obtengo a través de
                                                                                          // los países del
                                                                                          // continente
                    Set<TuplaContinenteJugadorPorcentaje> setTuplas = listaJugadoresContinente.stream().distinct()
                            .map(jugador -> {
                                float porcentaje = ((float) Collections.frequency(listaJugadoresContinente, jugador))
                                        / ((float) listaJugadoresContinente.size()); // Este es el porcentaje de los
                                                                                     // países dentro de este continente
                                                                                     // que posee el jugador
                                return (new TuplaContinenteJugadorPorcentaje(continente, jugador,
                                        Collections.frequency(listaJugadoresContinente, jugador), porcentaje)); // Generamos
                                                                                                                // la
                                                                                                                // tupla
                            }).collect(Collectors.toSet()); // Guardamos todas las tuplas de este continente en un mismo
                                                            // Set
                    return (setTuplas); // Devolvemos ese Set
                }).flatMap(Set::stream).collect(Collectors.toSet()); // Hasta aquí teníamos un
                                                                     // Set<Set<TuplaContinenteJugadorPorcentaje>>, ya
                                                                     // que tenemos las tuplas en un Set por cada
                                                                     // Continente. Pero realmente no nos interesa
                                                                     // tenerlas en Sets separados, así que las
                                                                     // convertimos a un único Set
        return tuplas;
    }
}
