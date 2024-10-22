/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import risk.cartas.CambioCartas;
import risk.cartas.CambioCartasFactory;
import risk.cartas.Carta;
import risk.cartasmision.CartaMision;
import risk.ejercito.Ejercito;
import risk.ejercito.EjercitoFactory;
import risk.riskexception.ExcepcionCarta;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.ExcepcionMision;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

/**
 * Representa un jugador de la Partida
 */
public class Jugador {
    private String nombre;
    private RiskColor color;
    private Set<CartaMision> setCartasMision;
    private Set<Carta> setCartasEquipamiento;
    private Set<Ejercito> ejercitosRearme;

    public Jugador(String nombre, RiskColor color) {
        this.setCartasMision = new HashSet<>();
        this.setCartasEquipamiento = new HashSet<>();
        this.ejercitosRearme = new HashSet<>();
        this.setNombre(nombre);
        this.setColor(color);
        this.setEjercitosRearme(0);
    }

    public void addCartaEquipamiento(Carta carta) throws ExcepcionRISK {
        if (this.getCartasEquipamiento().contains(carta)) {
            throw RiskExceptionEnum.CARTA_YA_ASIGNADA.get();
        }
        if (this.getCartasEquipamiento().size()>=6) {
            throw RiskExceptionEnum.COMANDO_NO_PERMITIDO.get();
        }
        this.setCartasEquipamiento.add(carta);
    }

    /**
     * Devuelve uno de los Ejercitos de rearme del Jugador
     */
    public Ejercito getAnyEjercitoDeRearme() {
        return this.ejercitosRearme.iterator().next();
    }

    /**
     * Elimina el Ejercito especificado del conjunto de Ejercitos de rearme de este Jugador
     * @param ejercito
     */
    public void removeEjercitoDeRearme(Ejercito ejercito) {
        this.ejercitosRearme.remove(ejercito);
    }

    /**
     * Añade el número especificado de ejércitos de rearme a este Jugador
     */
    public void addEjercitosRearme(int num) {
        while (num > 0) {
            this.ejercitosRearme.add(EjercitoFactory.getEjercito(this.getColor()));
            num--;
        }
    }

    /**
     * De los ejércitos sin repartir que tiene el jugador, asignar
     * {@code numEjercitos} al país elegido.
     * 
     * @param numEjercitos los ejércitos que se van a asignar
     * @return el número de ejércitos que se han asignado
     */
    public int asignarEjercitosAPais(int numEjercitos, Pais pais) throws ExcepcionJugador {
        if (this.getNumEjercitosRearme() >= numEjercitos) { // Tenemos suficientes ejércitos como para realizar la
                                                              // asignación
            for (int i = 0; i < numEjercitos; i++) {
                Ejercito ejercitoAnadir = this.getAnyEjercitoDeRearme();
                this.removeEjercitoDeRearme(ejercitoAnadir);
                pais.addEjercito(ejercitoAnadir);
            }
            return (numEjercitos);
        } else if (this.getNumEjercitosRearme() > 0) { // No tenemos todos los ejércitos que nos piden, pero sí
            // podemos asignar todos los que quedan
            int ejercitosSinRepartir = this.getNumEjercitosRearme(); // No es realmente necesario, pero me parece
            // buena práctica porque si modificásemos el
            // número de ejércitos sin repartir dentro del
            // for, entonces habría problemas
            for (int i = 0; i < ejercitosSinRepartir; i++) {
                Ejercito ejercitoAnadir = this.getAnyEjercitoDeRearme();
                this.removeEjercitoDeRearme(ejercitoAnadir);
                pais.addEjercito(ejercitoAnadir);
            }
            return (ejercitosSinRepartir);
        } else { // No hay ejércitos disponibles
            throw (ExcepcionJugador) RiskExceptionEnum.EJERCITOS_NO_DISPONIBLES.get();
        }
    }

    /**
     * Devuelve el número de ejércitos de rearme que corresponden a este Jugador
     */
    public int getNumEjercitosRearme() {
        return this.ejercitosRearme.size();
    }

    /**
     * Devuelve las Cartas de equipamiento de este Jugador
     */
    public Set<Carta> getCartasEquipamiento() {
        return this.setCartasEquipamiento;
    }

    /**
     * Devuelve el número de ejércitos de rearme que corresponden a este Jugador, por tener una Carta de equipamiento concreta y poseer el Pais que indica la Carta
     */
    public int getNumEjercitosRearmeAsociadosACartaPorPoseerPaisDeCarta(Carta carta) throws ExcepcionCarta {
        if (!this.getCartasEquipamiento().contains(carta)) {
            throw (ExcepcionCarta) RiskExceptionEnum.CARTAS_NO_PERTENECEN_JUGADOR.get();
        }
        if (carta.getPais().getJugador().equals(this)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Asigna el número de ejércitos de rearme especificados a este jugador
     */
    public void setEjercitosRearme(int num) {
        this.ejercitosRearme.clear();
        while (num > 0) {
            this.ejercitosRearme.add(EjercitoFactory.getEjercito(this.getColor()));
            num--;
        }
    }

    /**
     * Devuelve un Set de los Paises de este Jugador
     */
    public Set<Pais> getPaises() {
        Set<Pais> paisesJugador = Mapa.getMapa().getPaises().stream().filter(pais -> {
            return (pais.getJugador() != null ? pais.getJugador().equals(this) : false);
        }).collect(Collectors.toSet());
        return paisesJugador;
    }

    /**
     * Devuelve el total de ejércitos de este Jugador, buscando por todos sus países
     */
    public int getTotalEjercitos() {
        int totalEjercitos = this.getPaises().stream().reduce(0, (accum, pais) -> {
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

    public RiskColor getColor() {
        return this.color;
    }

    private void setColor(RiskColor color) {
        this.color = color;
    }

    /**
     * Devuelve un Set de los Continentes en que este Jugador es el único presente
     */
    public Set<Continente> getContinentesOcupadosExcusivamentePorJugador() {
        return this.getPaises().stream().map(p -> p.getContinente()).distinct()
                .filter(c -> c.getPaises().stream().allMatch(p -> p.getJugador().equals(this)))
                .collect(Collectors.toSet());
    }

    /**
     * Calcula el número de ejércitos de rearmar que corresponde inicialmente en este turno a este Jugador, sin perjuicio de que se pueda incrementar al cambiar cartas
     */
    public int calcularNumEjercitosRearmar() {
        int numEjercitosRearmar = this.getPaises().size() / 3; // El jugador recibe el número de ejércitos que es el
                                                               // resultado de dividir el número de países que
                                                               // pertenecen al jugador entre 3
        numEjercitosRearmar += getContinentesOcupadosExcusivamentePorJugador().stream()
                .mapToInt(Continente::getNumEjercitosRepartirCuandoOcupadoExcusivamentePorJugador).sum();
                // Si todos los países de un continente pertenecen a dicho jugador, recibe el número de ejércitos indicados en la Tabla 4
        
        return numEjercitosRearmar;
    }

    /**
     * Pone el número de Ejercitos de rearme del Jugador al que debiera ser
     */
    public void recalcularEjercitosRearme() {
        this.setEjercitosRearme(this.calcularNumEjercitosRearmar());
    }

    public Carta getCartaEquipamiento(String idCarta) throws ExcepcionCarta {
        Optional<Carta> carta = this.getCartasEquipamiento().stream().filter(c -> c.getNombre().equals(idCarta)).findFirst();
        if (!carta.isPresent()) {
            throw (ExcepcionCarta) RiskExceptionEnum.CARTAS_NO_PERTENECEN_JUGADOR.get();
        }
        return carta.get();
    }

    /**
     * Devuelve la mejor configuración de cambio de cartas para este Jugador
     */
    public CambioCartas realizarCambioOptimoDeCartasDeEquipamiento() throws ExcepcionCarta {
        CambioCartasFactory cambioCartasFactory = new CambioCartasFactory(this.getCartasEquipamiento(), this);
        CambioCartas cambioOptimo = cambioCartasFactory.getBestCambioCartas();
        this.cambiarCartasEquipamiento(cambioOptimo);
        return cambioOptimo;
    }

    /**
     * Realiza el cambio de tres Cartas de Equipamiento
     */
    public void cambiarCartasEquipamiento(CambioCartas cambioCartas) throws ExcepcionCarta {
        if (cambioCartas.getSetCartas().stream().anyMatch(c -> !this.hasCartaEquipamiento(c))) {
            throw (ExcepcionCarta) RiskExceptionEnum.CARTAS_NO_PERTENECEN_JUGADOR.get();
        }
        this.addEjercitosRearme(cambioCartas.getValorCambio());
        cambioCartas.getSetCartas().forEach(this::removeCartaEquipamiento);
    }

    /**
     * Le quita la Carta de equipamiento especificada a este Jugador
     */
    public void removeCartaEquipamiento(Carta carta) {
        this.setCartasEquipamiento.remove(carta);
    }
    
    /**
     * Le quita la Carta de misión especificada a este Jugador
     */
    public void removeMision(CartaMision m) {
        this.setCartasMision.remove(m);
    }

    /**
     * Devuelve TRUE si el Jugador ha completado alguna de sus misiones
     */
    public boolean jugadorHaCompletadoMision() {
        return this.setCartasMision.stream().allMatch(m -> m.isCompletada());
    }

    /**
     * Añade una CartaMision a este Jugador. Solo se permite añadir una misión.
     * 
     * @param cartaMision
     */
    public void addCartaMision(CartaMision cartaMision) throws ExcepcionMision {
        if (!this.setCartasMision.isEmpty()) {
            throw (ExcepcionMision) RiskExceptionEnum.JUGADOR_YA_TIENE_MISION.get();
        }
        this.setCartasMision.add(cartaMision);
        Mapa.getMapa().getPaisEventPublisher().subscribe(cartaMision);
    }

    /**
     * Devuelve el Set de CartaMision del Jugador
     */
    public Set<CartaMision> getCartasMision() {
        return this.setCartasMision;
    }

    /**
     * Devuelve una CartaMision del Jugador
     */
    public CartaMision getCartaMision() {
        return this.setCartasMision.iterator().next();
    }

    /**
     * Devuelve {@code true} si el jugador tiene la CartaMision
     * 
     * @param cartaMision
     */
    public boolean hasMision(CartaMision cartaMision) {
        return this.setCartasMision.contains(cartaMision);
    }

    /**
     * Devuelve {@code true} si el jugador tiene la Carta
     * 
     * @param carta
     */
    public boolean hasCartaEquipamiento(Carta carta) {
        return this.setCartasEquipamiento.contains(carta);
    }

    /**
     * Devuelve {@code true} si el jugador tiene ejércitos sin repartir
     */
    public boolean hasEjercitosSinRepartir() {
        return !ejercitosRearme.isEmpty();
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
