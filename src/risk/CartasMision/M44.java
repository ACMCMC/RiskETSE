package risk.cartasmision;

import risk.Jugador;
import risk.Ejercito.Ejercito;
import risk.Ejercito.EjercitoAmarillo;
import risk.Ejercito.EjercitoRojo;

public class M44 extends M4 {

    M44(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoRojo.class;
    }
    
}
