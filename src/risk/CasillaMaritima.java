package risk;

public class CasillaMaritima extends Casilla {

    CasillaMaritima(Coordenadas coordenadas) {
        super(coordenadas);
    }

    CasillaMaritima(Coordenadas coordenadas, BordeCasilla tipoBorde) {
        super(coordenadas, tipoBorde);
    }

    @Override
    public String toString() {
        return ("Casilla marítima -> (" + this.getCoordenadas().getX() + "," + this.getCoordenadas().getY() + ")");
    }

    public boolean esMaritima() {
        return true;
    }
}
