package risk.CartasMision;

import risk.Jugador;
import risk.Mapa;

public class M1 extends CartaMision implements PaisEventSubscriber {

    @Override
    public String getDescripcion() {
        return "Conquistar 24 países de la preferencia del jugador";
    }

    public M1() {
        super();
        Mapa.getMapa().getPaisEventPublisher().subscribe(this);
    }

    @Override
    public void update(PaisEvent evento) {
        Jugador jugadorPais = evento.getPais().getJugador();
        if (jugadorPais.getPaises().size() >= 24) {
            completada = true;
        } else {
            completada = false;
        }
        System.out.println(completada?"Completada":"No completada");
    }

}
