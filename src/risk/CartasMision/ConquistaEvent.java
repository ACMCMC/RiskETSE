package risk.CartasMision;

import risk.Jugador;

public interface ConquistaEvent {
    /**
     * Devuelve el Jugador que realiza la conquista
     * @return
     */
    public Jugador getConquistador();

    /**
     * Devuelve el Jugador que es conquistado
     */
    public Jugador getConquistado();
}
