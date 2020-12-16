package risk.cartas;

import risk.Pais;

public class DeCamello extends Caballeria {

    DeCamello(Pais pais) {
        super(pais);
    }

    @Override
    public int obtenerRearme() {
        return 2;
    }
    
}
