package risk.cartas;

import risk.Pais;

public abstract class Caballeria extends Carta {

    Caballeria(Pais pais) {
        super(pais);
    }

    @Override
    public Class<?> getClaseCarta() {
        return Caballeria.class;
    }
    
}
