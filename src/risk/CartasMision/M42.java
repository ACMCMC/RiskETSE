package risk.cartasmision;

import risk.Jugador;
import risk.ejercito.EjercitoAzul;

public class M42 extends M4 {

    @Override
    Class<?> getClaseEjercito() {
        return EjercitoAzul.class;
    }
    
}
