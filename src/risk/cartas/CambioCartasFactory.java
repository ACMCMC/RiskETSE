package risk.cartas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import risk.riskexception.ExcepcionCarta;
import risk.riskexception.RiskExceptionEnum;

/**
 * Busca la configuración de cambio de cartas óptima, y la devuelve
 */
public class CambioCartasFactory {
    private Set<Carta> cartasOriginales;
    public CambioCartasFactory(Set<Carta> cartasOriginales) throws ExcepcionCarta {
        if (cartasOriginales.size()<3) {
            throw (ExcepcionCarta) RiskExceptionEnum.NO_HAY_CARTAS_SUFICIENTES.get();
        }
        this.cartasOriginales = cartasOriginales;
    }

    /**
     * Calcula cuál es la mejor combinación posible de cambio de Cartas de equipamiento que puede hacer este Jugador, usando un algoritmo de ramificación y poda.
     */
    public CambioCartas getBestCambioCartas() {
        int beneficioCartaMax = cartasOriginales.stream().mapToInt(Carta::obtenerRearme).max().orElse(0);
        List<Nodo> listaNodosVivos = new ArrayList<>();
        
        Nodo nodoInicio = new Nodo(new HashSet<>(), beneficioCartaMax);
        Nodo bestCambio = nodoInicio;
        
        listaNodosVivos.add(nodoInicio);
        int cotaPoda = nodoInicio.getCotaInferior();
        
        while (!listaNodosVivos.isEmpty()) {
            Nodo nodoActual = seleccionar(listaNodosVivos);
            listaNodosVivos.remove(nodoActual);
            if (nodoActual.getCotaSuperior() > cotaPoda) {
                for (Nodo hijo : generarHijos(nodoActual, cartasOriginales)) {
                    if (solucion(hijo) && (hijo.getBeneficioEstimado()>bestCambio.getBeneficioEstimado())) {
                        bestCambio = hijo;
                        cotaPoda = Integer.max(cotaPoda, hijo.getBeneficioEstimado());
                    } else if (!solucion(hijo) && (hijo.getCotaSuperior() > cotaPoda)) {
                        listaNodosVivos.add(hijo);
                        cotaPoda = Integer.max(cotaPoda, hijo.getCotaInferior());
                    } else {
                        // No hacemos nada, se poda el nodo hijo
                    }
                }
            }
        }

        Iterator<Carta> iteratorCartas = bestCambio.getSetCartas().iterator();
        Carta carta1 = iteratorCartas.next();
        Carta carta2 = iteratorCartas.next();
        Carta carta3 = iteratorCartas.next();

        CambioCartas resultado = new CambioCartas(carta1, carta2, carta3);
        
        return resultado;
    }
    
    private boolean solucion(Nodo nodo) {
        return nodo.getNivel()==3;
    }
    
    private class Nodo {
        Set<Carta> cartasNodo;
        int cotaSuperior;
        Nodo(Set<Carta> cartas, int beneficioCartaMax) {
            this.cartasNodo = cartas;
            this.cotaSuperior = (3-this.getNivel())*beneficioCartaMax; // Suponemos las restantes asignaciones con el máximo global
        }
        Set<Carta> getSetCartas() {
            return this.cartasNodo;
        }
        int getNivel() {
            return this.getSetCartas().size();
        }
        int getCotaInferior() {
            return this.getSetCartas().stream().mapToInt(c -> c.obtenerRearme()).sum();
        }
        int getCotaSuperior(){
            return this.cotaSuperior;
        }
        int getBeneficioEstimado() {
            return ((this.getCotaSuperior())+(this.getCotaInferior()))/2;
        }
    }
    
    private Set<Nodo> generarHijos(Nodo nodoPadre, Set<Carta> cartasOriginales) {
        Set<Nodo> hijos = new HashSet<>();
        int beneficioCartaMax = cartasOriginales.stream().mapToInt(Carta::obtenerRearme).max().orElse(0);
        
        for (Carta carta : cartasOriginales) {
            if (!nodoPadre.getSetCartas().contains(carta)) {
                boolean esCartaQueSePuedeAnadir = false; // Solo se puede añadir la carta si tiene el mismo tipo que todas las anteriores, o es de un tipo distinto (para al final tres cartas de distinto tipo, o del mismo, pero no dos del mismo y una diferente)
                esCartaQueSePuedeAnadir = nodoPadre.getSetCartas().stream().allMatch(c -> c.getClaseCarta().equals(carta.getClaseCarta())); // Aquí miramos si la carta tiene el mismo tipo que todas las demás
                esCartaQueSePuedeAnadir = esCartaQueSePuedeAnadir || nodoPadre.getSetCartas().stream().allMatch(c -> !c.getClaseCarta().equals(carta.getClaseCarta())); // Si no, miramos si las cartas tendrían todas distinto tipo
                if (esCartaQueSePuedeAnadir) {
                    Set<Carta> cartasHijo = new HashSet<>();
                    cartasHijo.addAll(nodoPadre.getSetCartas());
                    Nodo nodoHijo = new Nodo(cartasHijo, beneficioCartaMax);
                    hijos.add(nodoHijo);
                }
            }
        }

        return hijos;
    }

    private Nodo seleccionar(List<Nodo> listaNodosVivos) {
        return listaNodosVivos.stream().max(Comparator.comparing(Nodo::getBeneficioEstimado)).orElse(null);
    }
}
