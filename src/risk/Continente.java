package risk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Clase Continente. Almacena el nombre, su color, y los países asociados al continente.
 */
public class Continente {
    private Color color;
    private String nombre;
    private Map<String, Pais> paises;

    /**
     * Crea un nuevo Continente, sin países asignados
     * @param nombre
     * @param color
     */
    public Continente(String nombre, Color color) {
        this.setColor(color);
        this.setNombre(nombre);
        this.paises = new HashMap<>();
    }

    private void setColor(Color color) {
        this.color = color;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el Color de este continente
     * @return Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Devuelve el nombre del continente
     * @return String nombre
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Añade un Pais a este continente
     * @param pais
     */
    public void addPais(Pais pais) {
        this.paises.put(pais.getNombre(), pais);
    }

    /**
     * Devuelve el Pais de nombre especificado
     * @param nombre
     * @return el Pais, o NULL
     */
    public Pais getPais(String nombre) {
        return this.paises.get(nombre);
    }

    /**
     * Devuelve un Set de todos los países
     * @return Set<Pais>
     */
    public Set<Pais> getPaises() {
        Set<Pais> setPaises = new HashSet<>();
        this.paises.entrySet().forEach((Entry<String, Pais> entry) -> {setPaises.add(entry.getValue());});
        return setPaises;
    }
}
