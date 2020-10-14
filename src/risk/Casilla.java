/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Casilla {

    private Jugador jugador;
    private Coordenadas coordenadas;
    private Pais pais; // Si es una casilla marítima, pais es null
    private BordeCasilla borde; // Parámetro auxiliar para pintar los bordes

    /**
     * Representa los bordes de la casilla. Se usa después para representar el mapa
    */
    enum BordeCasilla {
        TOP,
        VERTICAL,
        VERTICAL_LEFT,
        LEFT_TOP,
        LEFT_BOTTOM,
        LEFT_BOTTOM_HORIZONTAL,
        HORIZONTAL,
        NONE
    }

    Casilla(Coordenadas coordenadas) {
        setCoordenadas(coordenadas);
        setBorde(BordeCasilla.NONE);
        setPais(null); // Esta casilla no tiene asociado un país, es una casilla marítima
    }
    
    Casilla(Coordenadas coordenadas, BordeCasilla tipoBorde) {
        setCoordenadas(coordenadas);
        setBorde(tipoBorde);
        setPais(null); // Esta casilla no tiene asociado un país, es una casilla marítima
    }
    
    Casilla(Coordenadas coordenadas, Pais pais) {
        setCoordenadas(coordenadas);
        setBorde(BordeCasilla.NONE);
        setPais(pais);
    }

    private void setBorde(BordeCasilla borde) {
        this.borde = borde;
    }

    public BordeCasilla getBorde() {
        return this.borde;
    }

    public Jugador getJugador() {
        return this.jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Coordenadas getCoordenadas() {
        return this.coordenadas;
    }

    private void setCoordenadas(Coordenadas coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Pais getPais() {
        return this.pais;
    }

    private void setPais(Pais pais) {
        this.pais = pais;
    }

    public boolean esMaritima() {
        return (this.pais == null);
    }

    @Override
    public String toString() {
        if (this.esMaritima()) {
            return("Casilla marítima -> (" + this.getCoordenadas().getX() + "," + this.getCoordenadas().getY() + ")");
        } else {
            return("Casilla del país: " + this.getPais().getCodigo() + " -> (" + this.getCoordenadas().getX() + "," + this.getCoordenadas().getY() + ")");
        }
    }
}
