package risk;

import risk.cartas.Carta;
import risk.cartas.CartaEquipamientoFactory;
import risk.cartasmision.PaisEvent;
import risk.cartasmision.PaisEventSubscriber;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

/**
 * Representa un turno de la Partida.
 */
public class Turno implements PaisEventSubscriber {
    private int numConquistasPaises;
    private boolean hasJugadorCambiadoCarta;
    private Jugador jugador;

    Turno(Jugador jugadorTurno) {
        this.numConquistasPaises = 0;
        this.hasJugadorCambiadoCarta = false;
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
        if (!evento.getPaisAntes().getJugador().equals(evento.getPaisDespues().getJugador())) {
            if (evento.getPaisDespues().getJugador().equals(jugador)) {
                this.numConquistasPaises++;
            }
        }
    }

    /**
     * Asigna una Carta de equipamiento al Jugador de este Turno
     */
    public Carta asignarCartaEquipamiento(String idCarta) throws ExcepcionRISK {
        if (!this.hasJugadorConquistadoPais()) {
            throw RiskExceptionEnum.COMANDO_NO_PERMITIDO.get();
        }
        Carta cartaEquipamiento = CartaEquipamientoFactory.get(idCarta, Mapa.getMapa());
        if (this.hasJugadorCambiadoCarta==true) {
            throw RiskExceptionEnum.COMANDO_NO_PERMITIDO.get();
        }
        this.getJugador().addCartaEquipamiento(cartaEquipamiento);
        this.hasJugadorCambiadoCarta = true;
        return cartaEquipamiento;
    }

}
