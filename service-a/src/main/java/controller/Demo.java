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
public class Demo {

    @Channel("commands-out")
    Emitter<BusMessage> emitter;

    @Path("hello")
    @GET
    public String hello() throws InterruptedException {
        String cid = UUID.randomUUID().toString();
        emitter.send(new BusMessage(cid, "v1:ADD_LIKE", true,
                Map.of("userId", "u1", "recipientId", "u2")));

        return "Published, cid=" + cid;
    }
}