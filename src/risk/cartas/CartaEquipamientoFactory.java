package risk.cartas;

import risk.Mapa;
import risk.Pais;
import risk.riskexception.ExcepcionCarta;
import risk.riskexception.ExcepcionGeo;
import risk.riskexception.RiskExceptionEnum;

/**
 * Clase auxiliar que genera Cartas de equipamiento según los requisitos especificados
 */
public abstract class CartaEquipamientoFactory {
    public static Carta get(String tipo, Mapa mapa) throws ExcepcionCarta {
        String partes[] = tipo.split("&"); // Por ejemplo, DeCampanha&Alaksa
        if (partes.length != 2) {
            throw (ExcepcionCarta) RiskExceptionEnum.IDENTIFICADOR_INCORRECTO.get();
        }
        Pais pais;
        try {
            pais = mapa.getPais(partes[1]);
        } catch (ExcepcionGeo e) {
            throw (ExcepcionCarta) RiskExceptionEnum.IDENTIFICADOR_INCORRECTO.get();
        }
        switch (partes[0]) {
            case "Antiaerea":
            return new Antiaerea(pais);
            case "Fusilero":
            return new Fusilero(pais);
            case "Granadero":
            return new Granadero(pais);
            case "DeCampanha":
            return new DeCampanha(pais);
            case "DeCamello":
            return new DeCamello(pais);
            case "DeCaballo":
            return new DeCaballo(pais);
            default:
            throw (ExcepcionCarta) RiskExceptionEnum.IDENTIFICADOR_INCORRECTO.get();
        }
    }
}
