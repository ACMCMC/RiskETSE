/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

public class Coordenadas implements Comparable {
    
    private int X;
    private int Y;
    
    Coordenadas (int X, int Y) {
        this.X = X;
        this.Y = Y;
    }
    
    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }

    @Override
    public int compareTo(Object o) {
        if ( !o.getClass().equals(this.getClass()) ) { // Para comparar, miramos que sean de la misma clase
            return 0;
        }

        Coordenadas coords = (Coordenadas) o;

        if (this.getY() > coords.getY()) // La coordenada de este objeto está más abajo, así que devolvemos que es mayor
        return 1;

        if (this.getY() < coords.getY()) // La coordenada de este objeto está más arriba, así que devolvemos que es menor
        return -1;

        if (this.getX() > coords.getX()) // La coordenada de este objeto está en el mismo nivel, pero más a la derecha, así que devolvemos que es mayor
        return 1;

        if (this.getX() < coords.getX()) // La coordenada de este objeto está en el mismo nivel, pero más a la izquierda, así que devolvemos que es menor
        return -1;

        return 0; // Las coordenadas son iguales

    }
    
    @Override
    public boolean equals(Object o) {
        if ( !o.getClass().equals(this.getClass()) )
        return false; // No son iguales si son de distinta clase

        if (((Coordenadas) o).getX() == this.getX() && ((Coordenadas) o).getY() == this.getY())
        return true; // Son iguales si tienen las mismas coordenadas

        return false; // No son iguales por defecto
    }

    /**
     * Función de hashCode. Debe devolver el mismo valor para un par (x,y) igual
     */
    @Override
    public int hashCode() {
        return (100*getX() + getY());
    }

}
