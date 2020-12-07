package risk.cartasmision;

import risk.Color;
import risk.Partida;
import risk.riskexception.ExcepcionGeo;

public abstract class M4 extends CartaMision implements PaisEventSubscriber {

    
    M4() {
        super();
        Color colorEjercito;
        try {
            colorEjercito = Color.getColorByString(getClaseEjercito().getSimpleName());
        } catch (ExcepcionGeo e) {
            colorEjercito = null;
        }
        jugador = Partida.getPartida().getJugador(colorEjercito);
    }

    abstract Class<?> getClaseEjercito();

    @Override
    public String getDescripcion() {
        return "Destruir el ejército " + getClaseEjercito().getSimpleName().toUpperCase();
    }

    @Override
    public void update(PaisEvent evento) {
        if (jugador.getPaises().isEmpty())
            completada = true;
        else
            completada = false;
    }

}
