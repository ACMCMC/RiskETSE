package risk.cartasmision;

import risk.Jugador;
import risk.ejercito.Ejercito;
import risk.ejercito.EjercitoAmarillo;
import risk.ejercito.EjercitoCyan;

public class M43 extends M4 {

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoCyan.class;
    }
    
}
