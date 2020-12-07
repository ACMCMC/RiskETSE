package risk.cartasmision;

import java.util.ArrayList;
import java.util.List;

import risk.Continente;
import risk.Jugador;
import risk.Mapa;
import risk.riskexception.ExcepcionGeo;

public class M31 extends M3 {

    public M31(Jugador jugador) {
        super(jugador);
    }

    @Override
    List<Continente> getContinentesAConquistarParaCompletarMision() {
        List<Continente> listaContinentes = new ArrayList<>();
        try {
            listaContinentes.add(Mapa.getMapa().getContinente("Asia"));
        } catch (ExcepcionGeo e1) {
        }
        try {
            listaContinentes.add(Mapa.getMapa().getContinente("AméricaSur"));
        } catch (ExcepcionGeo e) {
        }
        return listaContinentes;
    }
    
}
