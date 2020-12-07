package risk.cartasmision;

import java.util.List;
import risk.Continente;
import risk.Jugador;

public abstract class M3 extends CartaMision {
    abstract List<Continente> getContinentesAConquistarParaCompletarMision();

    public M3(Jugador jugador) {
        super(jugador);
    }

    @Override
    public String getDescripcion() {
        return "Conquistar " + getContinentesAConquistarParaCompletarMision().get(0).getNombreHumano() + " y " + getContinentesAConquistarParaCompletarMision().get(1).getNombreHumano(); // "Conquistar [Continente 1] y [Continente 2]"
    }

    @Override
    public void update(PaisEvent evento) {
        if (hasJugadorConquistadoContinentes(getJugador(), getContinentesAConquistarParaCompletarMision())) {
            completada = true;
        } else {
            completada = false;
        }
    }

    boolean hasJugadorConquistadoContinentes(Jugador jugador, List<Continente> continentes) {
        return continentes.stream().allMatch(c -> {
            return (c.getJugadores().size()==1 && c.getJugadores().contains(jugador));});
    }

}
