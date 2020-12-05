/**
 * @author Aldán Creo Mariño, Hugo Gómez Sabucedo
 */

package risk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase Continente. Almacena el codigo, su color, y los países asociados al
 * continente.
 */
public class Continente {
    private Color color;
    private String codigo;
    private String nombreHumano;
    private int numEjercitosRepartirCuandoOcupadoExcusivamentePorJugador;
    private Map<String, Pais> paises;

    /**
     * Crea un nuevo Continente, sin países asignados
     * 
     * @param codigo
     * @param nombreHumano
     */
    public Continente(String codigo, String nombreHumano) {
        this.setColor(Color.INDEFINIDO);
        this.setCodigo(codigo);
        this.setNombreHumano(nombreHumano);
        this.paises = new HashMap<>();
    }

    /**
     * Crea un nuevo Continente, sin países asignados
     * 
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
     * Establece el número de ejércitos emperejado a este continente, que se le dará a un jugador cuando ocupe de forma exclusiva este continente
     * @param numEjercitosRepartirCuandoOcupadoExcusivamentePorJugador
     */
    public void setNumEjercitosRepartirCuandoOcupadoExcusivamentePorJugador(int numEjercitosRepartirCuandoOcupadoExcusivamentePorJugador) {
        this.numEjercitosRepartirCuandoOcupadoExcusivamentePorJugador = numEjercitosRepartirCuandoOcupadoExcusivamentePorJugador;
    }
    
    /**
     * Devuelve el número de ejércitos emperejado a este continente, que se le dará a un jugador cuando ocupe de forma exclusiva este continente
     * @param numEjercitosRepartirCuandoOcupadoExcusivamentePorJugador
     */
    public int getNumEjercitosRepartirCuandoOcupadoExcusivamentePorJugador() {
        // TODO: Esto se podría implementar con un archivo aparte
        switch (this.getCodigo()) {
            case "Asia":
            return 7;
            case "África":
            return 3;
            case "AméricaNorte":
            return 5;
            case "AméricaSur":
            return 2;
            case "Oceanía":
            return 2;
            case "Europa":
            return 5;
            default:
            return 0;
        }
    }

    /**
     * Devuelve el Color de este continente
     * 
     * @return Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Devuelve el codigo del continente
     * 
     * @return String codigo
     */
    public String getCodigo() {
        return this.codigo;
    }

    /**
     * Devuelve el nombre humano del continente
     * 
     * @return String codigo
     */
    public String getNombreHumano() {
        return this.nombreHumano;
    }

    /**
     * Añade un Pais a este continente
     * 
     * @param pais
     */
    public void addPais(Pais pais) {
        this.paises.put(pais.getCodigo(), pais);
    }

    /**
     * Devuelve el Pais de codigo especificado
     * 
     * @param codigo
     * @return el Pais, o NULL
     */
    public Pais getPais(String codigo) {
        return this.paises.get(codigo);
    }

    /**
     * Devuelve un Set de los jugadores que tienen al menos un país en este
     * continente
     * 
     * @return
     */
    public Set<Jugador> getJugadores() {
        return this.getPaises().stream().map(pais -> pais.getJugador()).filter(j -> j!=null).collect(Collectors.toSet());
    }

    /**
     * Devuelve un Set de todos los países
     */
    public Set<Pais> getPaises() {
        Set<Pais> setPaises = new HashSet<>();
        this.paises.entrySet().forEach((Entry<String, Pais> entry) -> {
            setPaises.add(entry.getValue());
        });
        return setPaises;
    }

    @Override
    public boolean equals(Object continente) {
        if (this == continente) {
            return true;
        }
        if (continente == null) {
            return false;
        }
        if (getClass() != continente.getClass()) {
            return false;
        }
        final Continente other = (Continente) continente;
        if (!this.getCodigo().equals(other.getCodigo())) {
            return false;
        }
        if (!this.getNombreHumano().equals(other.getNombreHumano())) {
            return false;
        }
        if (!this.getColor().equals(other.getColor())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNombreHumano();
    }
}
