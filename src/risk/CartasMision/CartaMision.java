package risk.cartasmision;

import risk.Jugador;

public abstract class CartaMision {

    boolean completada;
    Jugador jugador;

    CartaMision(Jugador jugador) {
        this();
        this.jugador = jugador;
    }
    
    CartaMision() {
        completada = false;
    }

    Jugador getJugador() {
        return this.jugador;
    }

    public abstract String getDescripcion();

    public String getID() {
        return this.getClass().getSimpleName();
    }

    public boolean isCompletada() {
        return completada;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof CartaMision))
            return false;
        if (!((CartaMision) other).getID().equals(this.getID())) // Si el ID de esta misión es el mismo
            return false;
        if (!((CartaMision) other).getJugador().equals(this.getJugador())) // Si el jugador de esta misión es el mismo
            return false;
        return true;
    }
}
