package risk.cartasmision;

import java.util.ArrayList;
import java.util.List;

import risk.Continente;
import risk.Jugador;
import risk.Mapa;
import risk.RiskException.ExcepcionGeo;

public class M33 extends M3 {
    public M33(Jugador jugador) {
        super(jugador);
    }

    @Override
    List<Continente> getContinentesAConquistarParaCompletarMision() {
        List<Continente> listaContinentes = new ArrayList<>();
        try {
            listaContinentes.add(Mapa.getMapa().getContinente("AméricaNorte"));
        } catch (ExcepcionGeo e1) {
        }
        try {
            listaContinentes.add(Mapa.getMapa().getContinente("África"));
        } catch (ExcepcionGeo e) {
        }
        return listaContinentes;
    }
}
