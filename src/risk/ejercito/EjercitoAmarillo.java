package risk.ejercito;

import risk.Dado;

public class EjercitoAmarillo extends EjercitoBase {

    //Se suman dos unidades si se tira un único dado

    @Override
    public Dado[] ataque(Dado[] dados) {
        Dado dado1 = dados[0];
        int Valor=dado1.getValor();

        if(dados.length==1){
            Valor=Valor+2;
        }

        dados[0].setValor(Valor);

        return dados;
    }
    
}
