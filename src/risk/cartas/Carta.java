package risk.cartas;

import risk.Pais;

/**
 * Clase abstracta base para las Cartas de equipamiento
 */
public abstract class Carta {
    Pais paisCarta;
    Carta(Pais pais) {
        this.paisCarta = pais;
    }

    public String getNombre() {
        return this.getTipo()+"&"+this.getPais().getCodigo();
    }

    public Pais getPais() {
        return this.paisCarta;
    }

    public String getTipo() {
        return this.getClass().getSimpleName();
    }

    public abstract int obtenerRearme();

    public abstract Class<?> getClaseCarta();

    @Override
    public boolean equals(Object other) {
        if (other==null) {
            return false;
        }
        if (!(other instanceof Carta)) {
            return false;
        }
        if (!(((Carta) other).getNombre().equals(this.getNombre()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.getNombre().hashCode();
    }
}
