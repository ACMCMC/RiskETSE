package risk.cartasmision;

import risk.Jugador;
import risk.Ejercito.Ejercito;
import risk.Ejercito.EjercitoAmarillo;

public class M41 extends M4 {

    M41(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoAmarillo.class;
    }
    
}
