package consumer;

import jakarta.enterprise.context.ApplicationScoped;
import model.BusMessage;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ReplyConsumer {

    @Incoming("replies-in")
    public void handle(BusMessage reply) {
        System.out.println("Beta received reply for cid=" + reply.cid() + ": " + reply.payload());
    }
}