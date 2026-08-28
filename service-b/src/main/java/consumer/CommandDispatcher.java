package consumer;

import jakarta.enterprise.context.ApplicationScoped;
import model.BusMessage;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class CommandDispatcher {

    @Channel("replies-out")
    Emitter<BusMessage> replyEmitter;

    private final Map<String, Function<BusMessage, Object>> registry = Map.of(
            "v1:ADD_LIKE", this::handleAddLike
    );

    @Incoming("commands-in")
    public void dispatch(BusMessage message) {
        Function<BusMessage, Object> handler = registry.get(message.capability());
        if (handler == null) {
            System.out.println("No handler for capability: " + message.capability());
            return;
        }

        Object result = handler.apply(message);

        if (message.needReply()) {
            replyEmitter.send(new BusMessage(
                    message.cid(),
                    message.capability(),
                    false,
                    result
            ));
        }
    }

    private Object handleAddLike(BusMessage message) {
        System.out.println("Handling ADD_LIKE: " + message.payload());
        return Map.of("status", "success");
    }
}