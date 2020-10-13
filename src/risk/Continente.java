/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Clase Continente. Almacena el codigo, su color, y los países asociados al continente.
 */
public class Continente {
    private Color color;
    private String codigo;
    private String nombreHumano;
    private Map<String, Pais> paises;

    /**
     * Crea un nuevo Continente, sin países asignados
     * @param codigo
     * @param color
     */
    public Continente(String codigo, String nombreHumano) {
        this.setColor(Color.INDEFINIDO);
        this.setCodigo(codigo);
        this.setNombreHumano(nombreHumano);
        this.paises = new HashMap<>();
    }

    /**
     * Crea un nuevo Continente, sin países asignados
     * @param codigo
     * @param color
     */
    public Continente(String codigo, String nombreHumano, Color color) {
        this.setColor(color);
        this.setCodigo(codigo);
        this.setNombreHumano(nombreHumano);
        this.paises = new HashMap<>();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    private void setNombreHumano(String nombreHumano) {
        this.nombreHumano = nombreHumano;
    }

    /**
     * Devuelve el Color de este continente
     * @return Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Devuelve el codigo del continente
     * @return String codigo
     */
    public String getCodigo() {
        return this.codigo;
    }

    /**
     * Devuelve el nombre humano del continente
     * @return String codigo
     */
    public String getNombreHumano() {
        return this.nombreHumano;
    }

    /**
     * Añade un Pais a este continente
     * @param pais
     */
    public void addPais(Pais pais) {
        this.paises.put(pais.getCodigo(), pais);
    }

    /**
     * Devuelve el Pais de codigo especificado
     * @param codigo
     * @return el Pais, o NULL
     */
    public Pais getPais(String codigo) {
        return this.paises.get(codigo);
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
