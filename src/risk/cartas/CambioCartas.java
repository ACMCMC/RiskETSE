package risk.cartas;

import java.util.HashSet;
import java.util.Set;

public class CambioCartas {
    Carta carta1;
    Carta carta2;
    Carta carta3;

    public CambioCartas(Carta carta1, Carta carta2, Carta carta3) {
        this.carta1 = carta1;
        this.carta2 = carta2;
        this.carta3 = carta3;
    }

    public Carta getCarta1() {
        return this.carta1;
    }
    public Carta getCarta2() {
        return this.carta2;
    }
    public Carta getCarta3() {
        return this.carta3;
    }
    public Set<Carta> getSetCartas() {
        Set<Carta> set = new HashSet<>();
        set.add(carta1);
        set.add(carta2);
        set.add(carta3);
        return set;
    }
}
