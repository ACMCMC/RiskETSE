package risk.cartasmision;

import java.util.HashSet;
import java.util.Set;

import risk.Jugador;
import risk.riskexception.ExcepcionMision;
import risk.riskexception.RiskExceptionEnum;

/**
 * Clase auxiliar que genera Cartas de Misión
 */
public abstract class CartaMisionFactory {

    public static CartaMision build(String idMision, Jugador jugadorActual) throws ExcepcionMision {
        switch (idMision.toUpperCase()) {
            case "M1":
                return new M1(jugadorActual);
            case "M2":
                return new M2(jugadorActual);
            case "M31":
                return new M31(jugadorActual);
            case "M32":
                return new M32(jugadorActual);
            case "M33":
                return new M33(jugadorActual);
            case "M34":
                return new M34(jugadorActual);
            case "M41":
                return new M41();
            case "M42":
                return new M42();
            case "M43":
                return new M43();
            case "M44":
                return new M44();
            case "M45":
                return new M45();
            case "M46":
                return new M46();
            default:
                throw (ExcepcionMision) RiskExceptionEnum.MISION_NO_EXISTE.get();
        }
    }

    public static Set<Class<? extends CartaMision>> getAll() {
        Set<Class<? extends CartaMision>> cartasMision = new HashSet<>();
        cartasMision.add(M1.class);
        cartasMision.add(M2.class);
        cartasMision.add(M31.class);
        cartasMision.add(M32.class);
        cartasMision.add(M33.class);
        cartasMision.add(M34.class);
        cartasMision.add(M41.class);
        cartasMision.add(M42.class);
        cartasMision.add(M43.class);
        cartasMision.add(M44.class);
        cartasMision.add(M45.class);
        cartasMision.add(M46.class);
        return cartasMision;
    }
}
