package risk.CartasMision;

import java.util.ArrayList;
import java.util.List;

import risk.Continente;
import risk.Mapa;
import risk.RiskException.ExcepcionGeo;

public class M32 extends M3 {
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
