package risk.cartasmision;

import risk.Pais;

public class PaisEvent {
    private Pais pais;

    public PaisEvent(Pais pais) {
        this.pais = pais;
    }

    /**
     * Devuelve el Pais que se ha conquistado
     */
    public Pais getPais() {
        return this.pais;
    }
}
