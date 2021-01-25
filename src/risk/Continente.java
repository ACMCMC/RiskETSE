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

import risk.cartasmision.PaisEvent;

/**
 * Clase Continente. Almacena el codigo, su color, y los países asociados al
 * continente.
 */
public class Continente {
    private RiskColor color;
    private String codigo;
    private String nombreHumano;

    /**
     * Crea un nuevo Continente, sin países asignados
     * 
     * @param codigo
     * @param nombreHumano
     */
    public Continente(String codigo, String nombreHumano) {
        this.setColor(RiskColor.INDEFINIDO);
        this.setCodigo(codigo);
        this.setNombreHumano(nombreHumano);
    }

    /**
     * Crea un nuevo Continente, sin países asignados
     * 
     * @param codigo
     * @param color
     */
    public Continente(String codigo, String nombreHumano, RiskColor color) {
        this.setColor(color);
        this.setCodigo(codigo);
        this.setNombreHumano(nombreHumano);
    }

    public void setColor(RiskColor color) {
        this.color = color;
        notificarCambiosPaises();
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
        notificarCambiosPaises();
    }

    public void setNombreHumano(String nombreHumano) {
        this.nombreHumano = nombreHumano;
        notificarCambiosPaises();
    }
    
    /**
     * Devuelve el número de ejércitos emperejado a este continente, que se le dará a un jugador cuando ocupe de forma exclusiva este continente
     */
    public int getNumEjercitosRepartirCuandoOcupadoExcusivamentePorJugador() {
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
    public RiskColor getColor() {
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
     * Devuelve el Pais de codigo especificado
     * 
     * @param codigo
     * @return el Pais, o NULL
     */
    public Pais getPais(String codigo) {
        return this.getPaises().stream().filter(p-> p.getCodigo().equals(codigo)).findFirst().orElse(null);
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
        return Mapa.getMapa().getPaises().stream().filter(p -> this.equals(p.getContinente())).collect(Collectors.toSet());
    }

    private void notificarCambiosPaises() {
        for (Pais p : getPaises()) {
            PaisEvent evento = new PaisEvent();
            evento.setPaisAntes(p);
            evento.setPaisDespues(p);
            p.notificarCambioPais(evento);
        }
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
        if (this.getCodigo()!=null) {
            if (!this.getCodigo().equals(other.getCodigo())) {
                return false;
            }
        } else {
            if (other.getCodigo()!=null) {
                return false;
            }
        }
        if (this.getNombreHumano()!=null) {
            if (!this.getNombreHumano().equals(other.getNombreHumano())) {
                return false;
            }
        } else {
            if (other.getNombreHumano()!=null) {
                return false;
            }
        }
        if (this.getColor()!=null) {
            if (!this.getColor().equals(other.getColor())) {
                return false;
            }
        } else {
            if (other.getColor()!=null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNombreHumano();
    }
}
