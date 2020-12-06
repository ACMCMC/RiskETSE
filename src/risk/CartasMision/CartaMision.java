package risk.CartasMision;

public abstract class CartaMision {

    boolean completada;

    CartaMision() {
        completada = false;
    }

    public abstract String getDescripcion();

    public String getID() {
        return this.getClass().getSimpleName();
    }

    public boolean isCompletada() {
        return completada;
    }
}
