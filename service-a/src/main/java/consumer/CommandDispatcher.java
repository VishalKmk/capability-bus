package consumer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.BusMessage;
import model.Comment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@ApplicationScoped
public class CommandDispatcher {

    @Channel("replies-out")
    Emitter<BusMessage> replyEmitter;

    private final Map<String, Function<BusMessage, Object>> registry = Map.of(
            "v1:ADD_COMMENT", this::handleAddComment
    );

    @Incoming("commands-in")
    public void dispatch(BusMessage message) {
        Function<BusMessage, Object> handler = registry.get(message.capability());
        if (handler == null) {
            System.out.println("No handler for capability: " + message.capability());
            return;
        }

        try {
            Object result = handler.apply(message);
            if (message.needReply()) {
                replyEmitter.send(new BusMessage(message.cid(), message.capability(), false, result));
            }
        } catch (Exception e) {
            System.out.println("Handler failed for capability " + message.capability() + ", cid=" + message.cid() + ": " + e.getMessage());
        }
    }

    @Transactional
    Object handleAddComment(BusMessage message) {
        @SuppressWarnings("unchecked")
        Map<String, String> data = (Map<String, String>) message.payload();

        Comment comment = new Comment();
        comment.userId = UUID.fromString(data.get("userId"));
        comment.recipientId = UUID.fromString(data.get("recipientId"));
        comment.createdAt = Instant.now();
        comment.persist();

        System.out.println("Persisted comment id=" + comment.id);
        return Map.of("status", "success", "commentId", comment.id);
    }
}