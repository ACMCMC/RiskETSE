package risk;

public class Casilla {
    private Coordenadas coordenadas;
    private Pais pais; // Si es una casilla marítima, pais es null

    Casilla(Coordenadas coordenadas) {
        setCoordenadas(coordenadas);
        setPais(null); // Esta casilla no tiene asociado un país, es una casilla marítima
    }

    Casilla(Coordenadas coordenadas, Pais pais) {
        setCoordenadas(coordenadas);
        setPais(pais);
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
        super.toString();
        return("(" + coordenadas.getX() + "," + coordenadas.getY() + "): " + pais.getNombre());
    }
}
