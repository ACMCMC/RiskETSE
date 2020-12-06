package risk.CartasMision;

import risk.Continente;
import risk.Jugador;

public class ConquistaJugadorEvent implements ConquistaEvent {

    private Jugador conquistado;
    private Jugador conquistador;

    public ConquistaJugadorEvent(Jugador conquistador, Jugador conquistado) {
        this.conquistador = conquistador;
        this.conquistado = conquistado;
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
