package risk.cartasmision;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        for (PaisEventSubscriber sub : ListSubscribers) {
            sub.update(evento);
        }
    }

}
