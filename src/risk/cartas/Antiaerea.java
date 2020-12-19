package risk.cartas;

import risk.Pais;

public class Antiaerea extends Artilleria {

    Antiaerea(Pais pais) {
        super(pais);
    }

    @Override
    public int obtenerRearme() {
        return 3;
    }
    
}
