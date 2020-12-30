package risk.cartasmision;

import risk.RiskColor;
import risk.Partida;

public abstract class M4 extends CartaMision {
    
    M4() {
        super();
        jugador = Partida.getPartida().getJugador(getColor());
    }

    abstract RiskColor getColor();

    @Override
    public String getDescripcion() {
        return "Destruir el ejército " + getColor().getNombre().toUpperCase();
    }

    @Override
    public void update(PaisEvent evento) {
        if (jugador.getPaises().isEmpty())
            completada = true;
        else
            completada = false;
    }

}
