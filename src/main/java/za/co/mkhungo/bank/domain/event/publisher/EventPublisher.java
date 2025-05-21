package za.co.mkhungo.bank.domain.event.publisher;

/**
 * @author Noxolo.Mkhungo
 */
public interface EventPublisher<T>{
    void publish(T event);
}
