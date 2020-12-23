package risk.cartas;

import java.util.HashSet;
import java.util.Set;

import risk.Jugador;
import risk.riskexception.ExcepcionCarta;
import risk.riskexception.RiskExceptionEnum;

/**
 * Representa una mano de tres Cartas de equipamiento, que van a ser cambiadas
 */
public class CambioCartas {
    Carta carta1;
    Carta carta2;
    Carta carta3;
    Jugador jugador;

    public CambioCartas(Carta carta1, Carta carta2, Carta carta3, Jugador jugador) throws ExcepcionCarta {
        this.carta1 = carta1;
        this.carta2 = carta2;
        this.carta3 = carta3;
        this.jugador = jugador;
        if (!isCombinacionCartasValida()) {
            throw (ExcepcionCarta) RiskExceptionEnum.NO_HAY_CONFIG_CAMBIO.get();
        }
    }

    public Carta getCarta1() {
        return this.carta1;
    }
    public Carta getCarta2() {
        return this.carta2;
    }
    public Carta getCarta3() {
        return this.carta3;
    }
    public Set<Carta> getSetCartas() {
        Set<Carta> set = new HashSet<>();
        set.add(carta1);
        set.add(carta2);
        set.add(carta3);
        return set;
    }

    public int getValorCambio() throws ExcepcionCarta {
        int valorCambio = getValorColectivoCambio();
        valorCambio += getValorSumadoDeCartas();
        valorCambio += getValorAsociadoACartasPorJugadorPoseerPaisDeCarta();
        return valorCambio;
    }

    private int getValorSumadoDeCartas() {
        return this.getSetCartas().stream().mapToInt(Carta::obtenerRearme).sum();
    }

    private int getValorAsociadoACartasPorJugadorPoseerPaisDeCarta() throws ExcepcionCarta {
        int valorAsociadoACartasPorJugadorPoseerPaisDeCarta = 0;
        for (Carta c: this.getSetCartas()) {
            valorAsociadoACartasPorJugadorPoseerPaisDeCarta += jugador.getNumEjercitosRearmeAsociadosACartaPorPoseerPaisDeCarta(c);
        }
        return valorAsociadoACartasPorJugadorPoseerPaisDeCarta;
    }

    private boolean isCombinacionCartasValida() {
        if (getSetCartas().stream().allMatch(c -> c.getClaseCarta().equals(this.getCarta1().getClaseCarta()))) {
            return true; // Si todas las cartas son de la misma clase
        }
        if (!this.getCarta1().getClaseCarta().equals(this.getCarta2().getClaseCarta()) && !this.getCarta2().getClaseCarta().equals(this.getCarta3().getClaseCarta()) && !this.getCarta3().getClaseCarta().equals(this.getCarta1().getClaseCarta())) {
            return true;
        }
        return false;
    }

    private int getValorColectivoCambio() {
        if (this.getSetCartas().stream().allMatch(c -> c.getClaseCarta().equals(this.getCarta1().getClaseCarta()))) {
            // Si todas las cartas son de la misma clase, entonces miramos de qué clase son. Nos da igual la carta concreta que miremos, porque son todas iguales
            if (carta1.getClaseCarta().equals(Infanteria.class)) {
                return 6;
            } else if (carta1.getClaseCarta().equals(Caballeria.class)) {
                return 8;
            } else if (carta1.getClaseCarta().equals(Artilleria.class)) {
                return 10;
            } else {
                throw new IllegalArgumentException("No se reconoce el tipo de carta");
            }
        } else { // Las tres cartas tienen que ser distintas, porque una condición del constructor es esa
            return 12;
        }
    }
}
