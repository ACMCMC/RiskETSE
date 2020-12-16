package risk.cartas;

import risk.Pais;

public abstract class Carta {
    Pais paisCarta;
    Carta(Pais pais) {
        this.paisCarta = pais;
    }

    public Pais getPais() {
        return this.paisCarta;
    }

    public String getTipo() {
        return this.getClass().getName();
    }

    public abstract int obtenerRearme();
}
