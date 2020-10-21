/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Pais {
    private String codigo;
    private String nombreHumano;
    private Continente continente;
    private Jugador jugador;
    private int numEjercitos; // El número de ejércitos que están en este país. Pertenecen al jugador que tiene este país.

    Pais(String nombre, String nombreHumano, Continente continente) {
        this.setCodigo(nombre);
        this.setNombreHumano(nombreHumano);
        this.setContinente(continente);
        this.setJugador(null);
        this.setNumEjercitos(0);
    }

    /**
     * Devuelve el número de ejércitos que hay en este Pais
     * @return
     */
    public int getNumEjercitos() {
        return this.numEjercitos;
    }

    /**
     * Establece el número de ejércitos que hay en este Pais
     * @param numEjercitos
     */
    public void setNumEjercitos(int numEjercitos) {
        this.numEjercitos = numEjercitos;
    }

    public Jugador getJugador() {
        return this.jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Devuelve el nombre humano del país
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
     * @return
     */
    public Continente getContinente() {
        return this.continente;
    }

    @Override
    public String toString() {
        return getCodigo();
    }
    
    @Override
    public boolean equals(Object pais){
        if (this==pais){
            return true;
        }
        if (pais==null){
            return false;
        }
        if(getClass() != pais.getClass()){
            return false;
        }
        final Pais other = (Pais) pais;
        if(!this.getCodigo().equals(other.getCodigo())){
            return false;
        }
        if(!this.getContinente().equals(other.getContinente())){
            return false;
        }
        if(!this.getNombreHumano().equals(other.getNombreHumano())){
            return false;
        }
        if(!this.getJugador().equals(other.getJugador())) {
            return false;
        }
        return true;
    }

}
