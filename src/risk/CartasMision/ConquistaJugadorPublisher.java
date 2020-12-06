package risk.CartasMision;

import java.util.HashSet;
import java.util.Set;

public class ConquistaJugadorPublisher {
    
    private Set<ConquistaJugadorSubscriber> setSubscribers;

    public ConquistaJugadorPublisher() {
        this.setSubscribers = new HashSet<>();
    }

    /**
     * Añade un suscriptor
     * @param subscriber
     */
    public void subscribe(ConquistaJugadorSubscriber subscriber) {
        this.setSubscribers.add(subscriber);
    }

    /**
     * Elimina un suscriptor
     * @param subscriber
     */
    public void unsubscribe(ConquistaJugadorSubscriber subscriber) {
        this.setSubscribers.remove(subscriber);
    }

    /**
     * Informa a los suscriptores de que ha ocurrido una conquista
     */
    public void updateSubscribers(ConquistaJugadorEvent evento) {
        for (ConquistaJugadorSubscriber sub : setSubscribers) {
            sub.update(evento);
        }
    }

}
