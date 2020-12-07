package risk.cartasmision;

import risk.Jugador;
import risk.Mapa;

public class M2 extends CartaMision implements PaisEventSubscriber {

    public M2(Jugador jugador) {
        super(jugador);
        Mapa.getMapa().getPaisEventPublisher().subscribe(this);
    }

    @Override
    public String getDescripcion() {
        return "Conquistar 18 países de la preferencia del jugador con un mínimo de dos ejércitos";
    }

    @Override
    public void update(PaisEvent evento) {
        if (calcularNumeroPaisesJugadorConMinimo2Ejercitos(getJugador()) >= 18) {
            completada = true;
        } else {
            completada = false;
        }
    }

    private int calcularNumeroPaisesJugadorConMinimo2Ejercitos(Jugador jugador) {
        return ((int) jugador.getPaises().stream().filter(p -> p.getNumEjercitos()>=2).count());
    }
    
}
