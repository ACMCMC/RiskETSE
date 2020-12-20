package risk.cartas;

import risk.Pais;

public class Fusilero extends Infanteria {

    Fusilero(Pais pais) {
        super(pais);
    }

    @Override
    public int obtenerRearme() {
        return 2;
    }
    
}
