package client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("beta/api")
@RegisterRestClient(configKey = "beta")
public interface BetaClient {

    @GET
    @Path("/hello2")
    String helloFromBeta();
}
