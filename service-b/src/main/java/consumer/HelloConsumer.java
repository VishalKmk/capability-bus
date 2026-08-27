package consumer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class HelloConsumer {

    @Incoming("hello-events-in")
    public void consume(String message) {
        System.out.println("Service B received: " + message);
    }
}