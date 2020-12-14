package risk.ejercito;

import risk.Dado;

public class EjercitoVioleta extends EjercitoCompuesto {

    //Se suma una unidad al dado con menor valor. Si solo se tira un dado, no se suma nada

    @Override
    public Dado[] ataque(Dado[] dados) {
        Dado dadoMenor = dados[0];

        for (Dado dado : dados) {
            if (dado.getValor() < dadoMenor.getValor()) {
                dadoMenor = dado;
            }
        }

        int nuevoValor = dadoMenor.getValor();

        if(dados.length>1){
            nuevoValor = nuevoValor + 1;
        }

        Dado dadoNuevo = new Dado(nuevoValor);

        for (int i = 0; i < dados.length; i++) {
            if (dados[i].equals(dadoMenor)) {
                dados[i] = dadoNuevo;
            }
        }

        return dados;
    }

}
