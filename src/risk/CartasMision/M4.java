package risk.cartasmision;

import risk.Jugador;
import risk.Mapa;

public abstract class M4 extends CartaMision implements PaisEventSubscriber {

    M4(Jugador jugador) {
        super(jugador);
        Mapa.getMapa().getPaisEventPublisher().subscribe(this);
    }

    abstract Class<?> getClaseEjercito();

    @Override
    public String getDescripcion() {
        return "Destruir el ejército " + getClaseEjercito().getSimpleName().toUpperCase();
    }

    @Override
    public void update(PaisEvent evento) {
        // TODO Auto-generated method stub

    }

}
