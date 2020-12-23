package risk.cartasmision;

import risk.Pais;

/**
 * Representa un cambio en las propiedades de un país
 */
public class PaisEvent {
    private Pais paisAntes;
    private Pais paisDespues;

    public PaisEvent() {
    }

    /**
     * Devuelve el Pais que se ha conquistado
     */
    public Pais getPaisDespues() {
        return this.paisDespues;
    }

    /**
     * Establece el estado posterior al evento del Pais que lo origina
     */
    public void setPaisDespues(Pais paisDespues) {
        this.paisDespues = paisDespues.clone();
    }

    /**
     * Establece el estado anterior al evento del Pais que origina el evento
     * @param paisAntes
     */
    public void setPaisAntes(Pais paisAntes) {
        this.paisAntes = paisAntes.clone();
    }

    /**
     * Devuelve el Pais que se ha conquistado
     */
    public Pais getPaisAntes() {
        return this.paisAntes;
    }
}
