package risk.CartasMision;

public interface CartaMision {

    public String getDescripcion();

    public MisionListener getListener();

    public default String getID() {
        return this.getClass().getSimpleName();
    }
}
