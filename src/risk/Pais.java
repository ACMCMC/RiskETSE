/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.HashSet;
import java.util.Set;

import risk.cartasmision.PaisEvent;
import risk.ejercito.Ejercito;
import risk.riskexception.ExcepcionJugador;
import risk.riskexception.RiskExceptionEnum;

/**
 * Representa un país dentro de un Continente
 */
public class Pais implements Cloneable {
    private String codigo;
    private String nombreHumano;
    private Continente continente;
    private Jugador jugador;
    private Set<Ejercito> ejercitos; // Los ejércitos que están en este país. Pertenecen al jugador que tiene este
                                     // país.
    private int vecesConquistado;

    /**
     * Crea un nuevo Pais
     */
    Pais(String nombre, String nombreHumano, Continente continente) {
        this.codigo = nombre;
        this.nombreHumano = nombreHumano;
        this.continente = continente;
        this.jugador = null;
        this.ejercitos = new HashSet<>();
        this.vecesConquistado = -1;
    }

    /**
     * Devuelve el número de ejércitos que hay en este Pais
     * 
     * @return
     */
    public int getNumEjercitos() {
        return this.ejercitos.size();
    }

    /**
     * Indica si hay algun Ejercito en este Pais
     */
    public boolean hasEjercitos() {
        return !this.ejercitos.isEmpty();
    }

    /**
     * Devuelve un Set de los Ejercitos de este Pais
     * 
     * @return un HashSet con los Ejercitos del Pais, pero no el HashSet original
     */
    public Set<Ejercito> getEjercitos() {
        return new HashSet<>(this.ejercitos);
    }

    /**
     * Devuelve cualquier ejército de los que tiene el Pais
     * @return
     */
    public Ejercito getAnyEjercito() throws ExcepcionJugador {
        if (this.ejercitos.isEmpty()) {
            throw (ExcepcionJugador) RiskExceptionEnum.NO_HAY_EJERCITOS_SUFICIENTES.get();
        }
        return this.getEjercitos().iterator().next();
    }

    /**
     * Añade un Ejercito al Set de Ejercitos de este Pais
     * 
     * @param ejercito
     */
    public void addEjercito(Ejercito ejercito) {
        PaisEvent evento = new PaisEvent();
        evento.setPaisAntes(this);
        if (this.ejercitos.stream().anyMatch(e -> !e.getClass().equals(ejercito.getClass()))) { // Si el tipo ejército que añadimos no coincide con el de alguno de los que ya hay, lanzamos una excepción (Por ejemplo, si intento añadir un EjercitoVerde a un Pais que tenga ya un EjercitoRojo)
            throw new IllegalArgumentException("Se ha intentado añadir un ejército del tipo incorrecto al país");
        }
        this.ejercitos.add(ejercito);
        evento.setPaisDespues(this);
        notificarCambioPais(evento);
    }

    /**
     * Elimina un Ejercito cualquiera de este Pais
     */
    public void removeEjercito() {
        PaisEvent evento = new PaisEvent();
        evento.setPaisAntes(this);
        this.ejercitos.remove(this.ejercitos.stream().findFirst().orElse(null));
        evento.setPaisDespues(this);
        notificarCambioPais(evento);
    }
    
    /**
     * Elimina un Ejercito concreto de este Pais
     */
    public void removeEjercito(Ejercito ejercito) {
        PaisEvent evento = new PaisEvent();
        evento.setPaisAntes(this);
        this.ejercitos.remove(ejercito);
        evento.setPaisDespues(this);
        notificarCambioPais(evento);
    }

    /**
     * Devuelve el jugador de este Pais, o null si no hay jugador
     * 
     * @return
     */
    public Jugador getJugador() {
        return this.jugador;
    }

    /**
     * Hace que el jugador especificado conquiste este país. Los ejércitos que hay en el país continúan intactos (Si se ejecuta esta función directamente, sería como si los ejércitos se transfiriesen de un jugador a otro)
     * @param conquistador
     */
    public void conquistar(Jugador conquistador) {
        if (!this.getEjercitos().isEmpty()) {
            throw new IllegalStateException("El país que se intenta conquistar aún tiene ejércitos");
        }
        PaisEvent evento = new PaisEvent();
        evento.setPaisAntes(this);
        this.setJugador(conquistador);
        this.vecesConquistado++;
        evento.setPaisDespues(this);
        notificarCambioPais(evento);
    }

    private void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Devuelve el nombre humano del país
     * 
     * @return
     */
    public String getNombreHumano() {
        return this.nombreHumano;
    }

    private void setNombreHumano(String nombreHumano) {
        this.nombreHumano = nombreHumano;
    }

    /**
     * Devuelve el código del país
     * 
     * @return
     */
    public String getCodigo() {
        return this.codigo;
    }

    private void setCodigo(String codigo) {
        PaisEvent evento = new PaisEvent();
        evento.setPaisAntes(this);
        this.codigo = codigo;
        evento.setPaisDespues(this);
        notificarCambioPais(evento);
    }

    private void setContinente(Continente continente) {
        PaisEvent evento = new PaisEvent();
        evento.setPaisAntes(this);
        this.continente = continente;
        evento.setPaisDespues(this);
        notificarCambioPais(evento);
    }

    /**
     * Devuelve el Continente asociado a este país
     * 
     * @return
     */
    public Continente getContinente() {
        return this.continente;
    }

    /**
     * Devuelve el número de veces que el Pais ha sido conquistado
     * 
     * @return
     */
    public int getNumVecesConquistado() {
        return this.vecesConquistado;
    }

    public void notificarCambioPais(PaisEvent evento) {
        Mapa.getMapa().getPaisEventPublisher().updateSubscribers(evento);
    }

    @Override
    public String toString() {
        return getCodigo();
    }

    @Override
    public boolean equals(Object pais) {
        if (this == pais) {
            return true;
        }
        if (pais == null) {
            return false;
        }
        if (getClass() != pais.getClass()) {
            return false;
        }
        final Pais other = (Pais) pais;
        if (!this.getCodigo().equals(other.getCodigo())) {
            return false;
        }
        if (!this.getContinente().equals(other.getContinente())) {
            return false;
        }
        if (!this.getNombreHumano().equals(other.getNombreHumano())) {
            return false;
        }
        if (!this.getJugador().equals(other.getJugador())) {
            return false;
        }
        if (this.getNumVecesConquistado()!=other.getNumVecesConquistado()) {
            return false;
        }
        if (!this.getEjercitos().containsAll(other.getEjercitos())) {
            return false;
        }
        return true;
    }

    @Override
    public Pais clone() {
        Pais clonedPais = new Pais(this.getCodigo(), this.getNombreHumano(), this.getContinente());
        clonedPais.setJugador(this.getJugador());
        clonedPais.ejercitos = new HashSet<>(this.getEjercitos());
        clonedPais.vecesConquistado = this.getNumVecesConquistado();
        return clonedPais;
    }

}
