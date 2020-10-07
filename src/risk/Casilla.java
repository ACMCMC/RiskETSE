package risk;

public class Casilla {
    private Coordenadas coordenadas;
    private Pais pais;

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

    @Override
    public String toString() {
        super.toString();
        return("(" + coordenadas.getX() + "," + coordenadas.getY() + "): " + pais.getNombre());
    }
}
