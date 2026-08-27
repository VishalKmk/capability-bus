package consumer;

import jakarta.enterprise.context.ApplicationScoped;
import model.BusMessage;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.Map;
import java.util.function.Consumer;

@ApplicationScoped
public class CommandDispatcher {

    @Channel("replies-out")
    Emitter<BusMessage> replyEmitter;

    private final Map<String, Consumer<BusMessage>> registry = Map.of(
            "v1:ADD_LIKE", this::handleAddLike
    );

    @Incoming("commands-in")
    public void dispatch(BusMessage message) {
        Consumer<BusMessage> handler = registry.get(message.capability());
        if (handler == null) {
            System.out.println("No handler for capability: " + message.capability());
            return;
        }
        handler.accept(message);
    }

    private void handleAddLike(BusMessage message) {
        System.out.println("Handling ADD_LIKE: " + message.payload());

        if (message.needReply()) {
            BusMessage reply = new BusMessage(
                    message.cid(),          // same correlation ID
                    message.capability(),   // carry through what this replies to
                    false,                  // a reply never itself needs a reply
                    Map.of("status", "success")
            );
            replyEmitter.send(reply);
        }
    }
}