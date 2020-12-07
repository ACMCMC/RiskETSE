package risk.cartasmision;

import risk.Jugador;
import risk.ejercito.Ejercito;
import risk.ejercito.EjercitoAmarillo;
import risk.ejercito.EjercitoRojo;

public class M44 extends M4 {

    M44(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoRojo.class;
    }
    
}
