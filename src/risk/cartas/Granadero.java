package risk.cartas;

import risk.Pais;

public class Granadero extends Infanteria {

    Granadero(Pais pais) {
        super(pais);
    }

    @Override
    public int obtenerRearme() {
        return 1;
    }
    
}
