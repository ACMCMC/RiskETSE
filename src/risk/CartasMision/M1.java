package risk.cartasmision;

import risk.Jugador;

public class M1 extends CartaMision {

    @Override
    public String getDescripcion() {
        return "Conquistar 24 países de la preferencia del jugador";
    }

    public M1(Jugador jugador) {
        super(jugador);
    }

    @Override
    public void update(PaisEvent evento) {
        if (getJugador().getPaises().size() >= 24) {
            completada = true;
        } else {
            completada = false;
        }
        System.out.println(completada?"Completada":"No completada");
    }

}
