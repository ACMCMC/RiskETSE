package risk.cartasmision;

import risk.Jugador;
import risk.Ejercito.EjercitoVioleta;

public class M46 extends M4 {

    M46(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoVioleta.class;
    }
    
}
