package controller;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import model.BusMessage;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.Map;
import java.util.UUID;

@RunOnVirtualThread
@Path("api")
public class CommentController {

    @Channel("commands-out")
    Emitter<BusMessage> emitter;

    @Path("comment")
    @GET
    public String getComments() {
        String cid = UUID.randomUUID().toString();
        emitter.send(new BusMessage(cid, "v1:ADD_COMMENT", true,
                Map.of("userId", UUID.randomUUID().toString(), "recipientId", UUID.randomUUID().toString())));

        return "Published, cid=" + cid;
    }
}