package risk.CartasMision;

import risk.Continente;
import risk.Jugador;

public class ConquistaContinenteEvent implements ConquistaEvent {
    private Continente continente;
    private Jugador conquistado;
    private Jugador conquistador;

    public ConquistaContinenteEvent(Continente continente, Jugador conquistador, Jugador conquistado) {
        this.continente = continente;
        this.conquistador = conquistador;
        this.conquistado = conquistado;
    }

    /**
     * Devuelve el Continente que ha sido conquistado
     */
    public Continente getContinente() {
        return this.continente;
    }

    @Override
    public Jugador getConquistador() {
        return conquistador;
    }

    @Override
    public Jugador getConquistado() {
        return this.conquistado;
    }
}
