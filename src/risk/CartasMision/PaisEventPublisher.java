package risk.CartasMision;

import java.util.HashSet;
import java.util.Set;

public class PaisEventPublisher {
    
    private Set<PaisEventSubscriber> setSubscribers;

    public PaisEventPublisher() {
        this.setSubscribers = new HashSet<>();
    }

    /**
     * Añade un suscriptor
     * @param subscriber
     */
    public void subscribe(PaisEventSubscriber subscriber) {
        this.setSubscribers.add(subscriber);
    }

    /**
     * Elimina un suscriptor
     * @param subscriber
     */
    public void unsubscribe(PaisEventSubscriber subscriber) {
        this.setSubscribers.remove(subscriber);
    }

    /**
     * Informa a los suscriptores de que ha ocurrido una conquista
     */
    public void updateSubscribers(PaisEvent evento) {
        for (PaisEventSubscriber sub : setSubscribers) {
            sub.update(evento);
        }
    }

}
