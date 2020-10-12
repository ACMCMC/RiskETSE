package risk;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa una frontera entre dos países (A y B). El orden de los países no importa.
 */
public class Frontera {
    private Pais paisA;
    private Pais paisB;

    Frontera (Pais paisA, Pais paisB) {
        this.setPaises(paisA, paisB);
    }

    /**
     * Devuelve el país A
     * @return
     */
    public Pais getPaisA() {
        return this.paisA;
    }

    /**
     * Devuelve el país B
     * @return
     */
    public Pais getPaisB() {
        return this.paisB;
    }

    /**
     * Devuelve un Set de los dos países
     * @return
     */
    public Set<Pais> getPaises() {
        Set<Pais> paises = new HashSet<>();
        paises.add(this.getPaisA());
        paises.add(this.getPaisB());
        return paises;
    }

    /**
     * Método setter para establecer los países de esta Frontera
     * @param paisA
     * @param paisB
     */
    private void setPaises(Pais paisA, Pais paisB) {
        this.paisA = paisA;
        this.paisB = paisB;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(Frontera.class)) {
            return false; // No son objetos iguales si son de distinta clase
        }

        Frontera fronteraIgual = (Frontera) obj;

        if (fronteraIgual.getPaises().equals(this.getPaises())) {
            return true; // Si los elementos de los dos Sets son los mismos, entonces las fronteras son iguales
        }
        
        return false; // Si no se cumple lo anterior, entonces no es la misma frontera
    }

    @Override
    public int hashCode() {
        return getPaisA().hashCode() * getPaisB().hashCode();
    }

    @Override
    public String toString() {
        return getPaisA().getCodigo() + " - " + getPaisB().getCodigo();
    }
}
