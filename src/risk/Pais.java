/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Pais {
    private String codigo;
    private String nombreHumano;
    private Continente continente;
    private Jugador jugador;
    private Set<Ejercito> ejercitos; // Los ejércitos que están en este país. Pertenecen al jugador que tiene este
                                     // país.
    private int vecesConquistado;

    Pais(String nombre, String nombreHumano, Continente continente) {
        this.setCodigo(nombre);
        this.setNombreHumano(nombreHumano);
        this.setContinente(continente);
        this.setJugador(null);
        this.ejercitos = new HashSet<>();
        addToContinente();
    }

    /**
     * Añade este Pais a su Continente
     */
    private void addToContinente() {
        continente.addPais(this);
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
     * Devuelve un Set de los Ejercitos de este Pais
     * 
     * @return un HashSet con los Ejercitos del Pais, pero no el HashSet original
     */
    public Set<Ejercito> getEjercitos() {
        return new HashSet<>(this.ejercitos);
    }

    /**
     * Añade un Ejercito al Set de Ejercitos de este Pais
     * 
     * @param ejercito
     */
    public void addEjercito(Ejercito ejercito) {
        this.ejercitos.add(ejercito);
    }

    /**
     * Elimina un Ejercito cualquiera de este Pais
     */
    public void removeEjercito() {
        this.ejercitos.remove(this.ejercitos.stream().findFirst().orElse(null));
    }

    /**
     * Elimina un Ejercito concreto de este Pais
     */
    public void removeEjercito(Ejercito ejercito) {
        this.ejercitos.remove(ejercito);
    }

    /**
     * Devuelve el jugador de este Pais, o null
     * 
     * @return
     */
    public Jugador getJugador() {
        return this.jugador;
    }

    public void setJugador(Jugador jugador) {
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
        this.codigo = codigo;
    }

    private void setContinente(Continente continente) {
        this.continente = continente;
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
        return true;
    }

}
