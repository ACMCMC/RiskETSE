package risk;

import java.util.Random;

public class Dado {
    private int valor;

    Dado() {
        this.valor = new Random().nextInt(6)+1;
    }

    Dado(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }
}
