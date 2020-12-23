package risk;

import java.util.HashSet;
import java.util.Set;

import risk.riskexception.ExcepcionPropia;
import risk.riskexception.RiskExceptionEnum;

public class DadoFactory {
    public static Set<Dado> getDadosFromString(String stringDados) throws ExcepcionPropia {
        Set<Dado> setRetorno = new HashSet<>();
        String dados[] = stringDados.split("x");
        for (int i = 0; i < dados.length; i++) {
            int numeroDado;
            Dado dadoActual;
            try {
                numeroDado = Integer.parseInt(dados[i]);
            } catch (NumberFormatException e) {
                throw (ExcepcionPropia) RiskExceptionEnum.FORMATO_DADOS_INCORRECTO.get();
            }
            dadoActual = new Dado(numeroDado);
            setRetorno.add(dadoActual);
        }
        return setRetorno;
    }
}
