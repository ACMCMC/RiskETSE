package risk;

@Deprecated
public class Ejercito {
    private final Pais pais; // El país de este ejército nunca va a variar

    Ejercito(Pais pais) {
        this.pais = pais;
    }

    /**
     * Devuelve el Pais asociado a este Ejercito
     * @return
     */
    public Pais getPais() {
        return this.pais;
    }
}
