package risk.cartasmision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Se encarga de publicar eventos PaisEvent
 */
public class PaisEventPublisher {
    
    private List<PaisEventSubscriber> ListSubscribers; // Una lista, y no un conjunto, porque puede haber varios suscriptores iguales

    public PaisEventPublisher() {
        this.ListSubscribers = new ArrayList<>();
    }

    /**
     * Añade un suscriptor
     * @param subscriber
     */
    public void subscribe(PaisEventSubscriber subscriber) {
        this.ListSubscribers.add(subscriber);
    }

    /**
     * Elimina un suscriptor
     * @param subscriber
     */
    public void unsubscribe(PaisEventSubscriber subscriber) {
        this.ListSubscribers.remove(subscriber);
    }

    /**
     * Informa a los suscriptores de que ha ocurrido una conquista
     */
    public void updateSubscribers(PaisEvent evento) {
        List<PaisEventSubscriber> copiaListSubscribers = new ArrayList<>(ListSubscribers);
        copiaListSubscribers.forEach(s -> s.update(evento));
    }

}
