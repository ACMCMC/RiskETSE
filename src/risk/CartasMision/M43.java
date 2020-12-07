package risk.cartasmision;

import risk.Jugador;
import risk.Ejercito.Ejercito;
import risk.Ejercito.EjercitoAmarillo;
import risk.Ejercito.EjercitoCyan;

public class M43 extends M4 {

    M43(Jugador jugador) {
        super(jugador);
    }

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoCyan.class;
    }
    
}
