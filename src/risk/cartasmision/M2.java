package risk.cartasmision;

import risk.Jugador;

public class M2 extends CartaMision {

    public M2(Jugador jugador) {
        super(jugador);
    }

    @Override
    public String getDescripcion() {
        return "Conquistar 18 países de la preferencia del jugador con un mínimo de dos ejércitos";
    }

    @Override
    public void update(PaisEvent evento) {
        if (calcularNumeroPaisesJugadorConMinimo2Ejercitos(getJugador()) >= 18) {
            completada = true;
        } else {
            completada = false;
        }
    }

    private int calcularNumeroPaisesJugadorConMinimo2Ejercitos(Jugador jugador) {
        return ((int) jugador.getPaises().stream().filter(p -> p.getNumEjercitos()>=2).count());
    }
    
}
