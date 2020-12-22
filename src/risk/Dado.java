package risk;

import java.util.Random;

import risk.riskexception.ExcepcionPropia;
import risk.riskexception.ExcepcionRISK;
import risk.riskexception.RiskExceptionEnum;

/**
 * Representa un dado para usarlo al atacar.
 */
public class Dado {
    private int valor;

    public Dado() {
        this.valor = new Random().nextInt(6)+1;
    }

    public Dado(int valor) throws ExcepcionPropia {
        this.valor = valor;
        if (valor > 6 || valor < 1) {
            throw (ExcepcionPropia) RiskExceptionEnum.VALOR_DADOS_INCORRECTO.get();
        }
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
