package risk.cartas;

import risk.Pais;

public class DeCaballo extends Caballeria {

    DeCaballo(Pais pais) {
        super(pais);
    }

    @Override
    public int obtenerRearme() {
        return 3;
    }
    
}
