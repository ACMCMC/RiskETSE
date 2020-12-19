package risk.cartas;

import risk.Pais;

public abstract class Artilleria extends Carta {

    Artilleria(Pais pais) {
        super(pais);
    }

    @Override
    public Class<?> getClaseCarta() {
        return Artilleria.class;
    }
    
}
