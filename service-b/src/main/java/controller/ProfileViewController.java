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
public class ProfileViewController {

    @Channel("commands-out")
    Emitter<BusMessage> emitter;

    @Path("view")
    @GET
    public String recordView() {
        String cid = UUID.randomUUID().toString();
        emitter.send(new BusMessage(cid, "v1:CACHE_PROFILE_VIEW", false,
                Map.of("profileId", UUID.randomUUID().toString())));
        return "Published, cid=" + cid;
    }
}