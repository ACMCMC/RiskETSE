package risk.cartasmision;

import risk.Jugador;
import risk.ejercito.EjercitoAzul;

public class M42 extends M4 {

    M42(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoAzul.class;
    }
    
}
