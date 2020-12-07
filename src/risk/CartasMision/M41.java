package risk.cartasmision;

import risk.Jugador;
import risk.ejercito.Ejercito;
import risk.ejercito.EjercitoAmarillo;

public class M41 extends M4 {

    M41(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoAmarillo.class;
    }
    
}
