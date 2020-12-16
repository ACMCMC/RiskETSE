package risk.cartas;

import risk.Pais;

public class DeCampanha extends Artilleria {

    DeCampanha(Pais pais) {
        super(pais);
    }

    @Override
    public int obtenerRearme() {
        return 4;
    }
    
}
