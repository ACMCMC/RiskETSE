package risk.CartasMision;

import java.util.HashSet;
import java.util.Set;

public class ConquistaContinentePublisher {
    
    private Set<ConquistaContinenteSubscriber> setSubscribers;

    public ConquistaContinentePublisher() {
        this.setSubscribers = new HashSet<>();
    }

    /**
     * Añade un suscriptor
     * @param subscriber
     */
    public void subscribe(ConquistaContinenteSubscriber subscriber) {
        this.setSubscribers.add(subscriber);
    }

    /**
     * Elimina un suscriptor
     * @param subscriber
     */
    public void unsubscribe(ConquistaContinenteSubscriber subscriber) {
        this.setSubscribers.remove(subscriber);
    }

    /**
     * Informa a los suscriptores de que ha ocurrido una conquista
     */
    public void updateSubscribers(ConquistaContinenteEvent evento) {
        for (ConquistaContinenteSubscriber sub : setSubscribers) {
            sub.update(evento);
        }
    }

}
