package risk.CartasMision;

import risk.Jugador;
import risk.Pais;

public class ConquistaPaisEvent implements ConquistaEvent {
    private Pais pais;
    private Jugador conquistado;
    private Jugador conquistador;

    public ConquistaPaisEvent(Pais pais, Jugador conquistado, Jugador conquistador) {
        this.pais = pais;
        this.conquistado = conquistado;
        this.conquistador = conquistador;
    }

    /**
     * Devuelve el Pais que se ha conquistado
     */
    public Pais getPais() {
        return this.pais;
    }

    @Override
    public Jugador getConquistador() {
        return this.conquistador;
    }

    @Override
    public Jugador getConquistado() {
        return this.conquistado;
    }
}
