package risk;

import risk.cartasmision.PaisEvent;
import risk.cartasmision.PaisEventSubscriber;

public class Turno implements PaisEventSubscriber {
    private int numConquistasPaises;
    private Jugador jugador;

    Turno(Jugador jugadorTurno) {
        this.numConquistasPaises = 0;
        this.jugador = jugadorTurno;
    }

    /**
     * Devuelve el Jugador del Turno
     */
    public Jugador getJugador() {
        return this.jugador;
    }

    /**
     * Indica si, durante el Turno, el Jugador ha conquistado al menos un Pais
     */
    public boolean hasJugadorConquistadoPais() {
        return this.numConquistasPaises>0;
    }

    @Override
    public void update(PaisEvent evento) {
        if (evento.getPaisAntes().getJugador().equals(evento.getPaisDespues().getJugador())) {
            if (evento.getPaisDespues().getJugador().equals(jugador)) {
                this.numConquistasPaises++;
            }
        }
    }

}
