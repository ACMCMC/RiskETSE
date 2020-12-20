package risk;

import java.util.Random;

/**
 * Representa un dado para usarlo al atacar.
 */
public class Dado {
    private int valor;

    public Dado() {
        this.valor = new Random().nextInt(6)+1;
    }

    public Dado(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }

    public void setValor(int newValor) {
        this.valor = newValor;
    }

    @Override
    public String toString() {
        return Integer.toString(valor);
    }
}
