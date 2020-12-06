package risk.CartasMision;

import java.util.HashSet;
import java.util.Set;

public class ConquistaPaisPublisher {
    
    private Set<ConquistaPaisSubscriber> setSubscribers;

    public ConquistaPaisPublisher() {
        this.setSubscribers = new HashSet<>();
    }

    /**
     * Añade un suscriptor
     * @param subscriber
     */
    public void subscribe(ConquistaPaisSubscriber subscriber) {
        this.setSubscribers.add(subscriber);
    }

    /**
     * Elimina un suscriptor
     * @param subscriber
     */
    public void unsubscribe(ConquistaPaisSubscriber subscriber) {
        this.setSubscribers.remove(subscriber);
    }

    /**
     * Informa a los suscriptores de que ha ocurrido una conquista
     */
    public void updateSubscribers(ConquistaPaisEvent evento) {
        for (ConquistaPaisSubscriber sub : setSubscribers) {
            sub.update(evento);
        }
    }

}
