package risk.cartasmision;

import risk.Jugador;
import risk.ejercito.EjercitoVerde;

public class M45 extends M4 {

    M45(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoVerde.class;
    }
    
}
