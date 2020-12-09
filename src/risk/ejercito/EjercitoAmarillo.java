package risk.ejercito;

import risk.Dado;

public class EjercitoAmarillo extends EjercitoBase {

    @Override
    public Dado[] ataque(Dado[] dados) {
        Dado dadoMayor = dados[0];

        for (Dado dado : dados) {
            if (dado.getValor() > dadoMayor.getValor()) {
                dadoMayor = dado;
            }
        }

        int nuevoValor = dadoMayor.getValor() + 2;
        Dado dadoNuevo = new Dado(nuevoValor);

        for (int i = 0; i < dados.length; i++) {
            if (dados[i].equals(dadoMayor)) {
                dados[i] = dadoNuevo;
            }
        }

        return dados;
    }
    
}
