package risk.CartasMision;

import java.util.List;
import java.util.Set;

import risk.Continente;
import risk.Jugador;
import risk.Mapa;

public abstract class M3 extends CartaMision implements PaisEventSubscriber {
    abstract List<Continente> getContinentesAConquistarParaCompletarMision();

    public M3() {
        Mapa.getMapa().getPaisEventPublisher().subscribe(this);
    }

    @Override
    public String getDescripcion() {
        return "Conquistar " + getContinentesAConquistarParaCompletarMision().get(0).getNombreHumano() + " y " + getContinentesAConquistarParaCompletarMision().get(1).getNombreHumano(); // "Conquistar [Continente 1] y [Continente 2]"
    }

    @Override
    public void update(PaisEvent evento) {
        Jugador jugadorPais = evento.getPais().getJugador();
        if (hasJugadorConquistadoContinentes(jugadorPais, getContinentesAConquistarParaCompletarMision())) {
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
