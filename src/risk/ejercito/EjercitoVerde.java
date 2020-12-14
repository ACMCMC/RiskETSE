package risk.ejercito;

import risk.Dado;

public class EjercitoVerde extends EjercitoCompuesto {

    //Se suma una unidad al dado con mayor valor. Si solo se tira un dado, no se suma nada

    @Override
    public Dado[] ataque(Dado[] dados) {
        Dado dadoMayor = dados[0];

        for (Dado dado : dados) {
            if (dado.getValor() > dadoMayor.getValor()) {
                dadoMayor = dado;
            }
        }

        int nuevoValor = dadoMayor.getValor();

        if(dados.length>1){
            nuevoValor = nuevoValor + 1;
        }

        Dado dadoNuevo = new Dado(nuevoValor);

        for (int i = 0; i < dados.length; i++) {
            if (dados[i].equals(dadoMayor)) {
                dados[i] = dadoNuevo;
            }
        }

        return dados;
    }

}