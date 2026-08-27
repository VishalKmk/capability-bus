package controller;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RunOnVirtualThread
@Path("api")
public class Demo {

    @Path("hello2")
    @GET
    public String hello2() {
        return "Hello, World from service Beta";
    }
}