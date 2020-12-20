package risk.cartas;

import risk.Pais;

public abstract class Infanteria extends Carta {

    Infanteria(Pais pais) {
        super(pais);
    }

    @Override
    public Class<?> getClaseCarta() {
        return Infanteria.class;
    }
    
}
