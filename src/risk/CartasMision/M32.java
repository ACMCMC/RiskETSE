package risk.cartasmision;

import java.util.ArrayList;
import java.util.List;

import risk.Continente;
import risk.Jugador;
import risk.Mapa;
import risk.riskexception.ExcepcionGeo;

public class M32 extends M3 {
    public M32(Jugador jugador) {
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
            listaContinentes.add(Mapa.getMapa().getContinente("África"));
        } catch (ExcepcionGeo e) {
        }
        return listaContinentes;
    }
}
