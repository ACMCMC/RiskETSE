package risk.cartasmision;

/**
 * Representa a un suscriptor al PaisEventPublisher
 */
public interface PaisEventSubscriber {
    void update(PaisEvent evento);
}
