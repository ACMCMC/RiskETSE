/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

/**
 * Una casilla del Mapa que tiene asociado un Pais
 */
public class CasillaPais extends Casilla {

    private final Pais pais; // Nunca puede ser NULL

    CasillaPais(Coordenadas coordenadas, Pais pais) {
        super(coordenadas);
        this.pais = pais;
    }

    CasillaPais(Coordenadas coordenadas, BordeCasilla tipoBorde, Pais pais) {
        super(coordenadas, tipoBorde);
        this.pais = pais;
    }

    public Pais getPais() {
        return this.pais;
    }

    @Override
    public String toString() {
        return ("Casilla del país: " + this.getPais().getCodigo() + " -> (" + this.getCoordenadas().getX() + ","
                + this.getCoordenadas().getY() + ")");
    }

    @Override
    public boolean equals(Object casilla) {
        if (!super.equals(casilla)) {
            return false; // Si el equals() de Casilla nos dice que es distinta, ya no comprobamos el país
        }
        if (!(casilla instanceof CasillaPais)) { // Casilla nos dice que son iguales, comprobamos si es una CasillaPais
            return false;
        }
        if (!this.getPais().equals(((CasillaPais) casilla).getPais())) {
            return false; // Si los países son distintos, no son iguales
        }
        return true; // Los países son los mismos
    }

    public boolean esMaritima() {
        return false;
    }
}
