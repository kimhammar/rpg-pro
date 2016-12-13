import creatureService.http.RestHttp;

import javax.inject.Inject;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import creatureService.entities.Creature;
import shared.response.RestResponse;

/**
 * Created by kimha on 12/10/16.
 */
@RunWith(Arquillian.class)
public class CreatureTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(RestHttp.class, RestResponse.class, Creature.class)
                .addAsResource("META-INF/persistence.xml",
                        "META-INF/persistence.xml");
    }

    @Inject
    RestResponse res;


    @Test
    public void testGetAllCharacters() {
        Response response = ClientBuilder.newClient()
                .target("http://127.0.0.1:8080/rpgpro/character")
                .request(MediaType.APPLICATION_JSON).header(
                        "Authorization", "Basic " +
                                java.util.Base64.getEncoder().
                                        encodeToString("root:root".getBytes()))
                .get();

        res = response.readEntity(RestResponse.class);

        assertEquals("test can get repsonse", 200, res.getStatus());
        assertNotEquals("Test data not null", null, res.getData());
    }



    @Test
    public void testCreateCreature() {
        Creature creature = new Creature();
        creature.setName("Svampen");
        Response response = ClientBuilder.newClient()
                .target("http://127.0.0.1:8080/rpgpro/character/store")
                .queryParam("creatureName", "bollen")
                .request(MediaType.APPLICATION_JSON).header(
                        "Authorization", "Basic " +
                                java.util.Base64.getEncoder().
                                        encodeToString("root:root".getBytes()))
                .post(null);

        res = response.readEntity(RestResponse.class);

        assertEquals("Test create  a new creature returns correct status code", 201, res.getStatus());

    }



    @Test
    public void testDeleteCreature_01() {
        Response response = ClientBuilder.newClient()
                .target("http://127.0.0.1:8080/rpgpro/character/delete/111")
                .request(MediaType.APPLICATION_JSON).header(
                        "Authorization", "Basic " +
                                java.util.Base64.getEncoder().
                                        encodeToString("root:root".getBytes()))
                .delete();

        res = response.readEntity(RestResponse.class);
        assertEquals("Check correct status code if no such resource", Response.Status.NO_CONTENT.getStatusCode(), res.getStatus());
    }

    @Ignore
    @Test
    public void testDeleteCreature_02() {
        Response response = ClientBuilder.newClient()
                .target("http://127.0.0.1:8080/rpgpro/character/delete/1")
                .request(MediaType.APPLICATION_JSON).header(
                        "Authorization", "Basic " +
                                java.util.Base64.getEncoder().
                                        encodeToString("root:root".getBytes()))
                .delete();

        res = response.readEntity(RestResponse.class);
//        Map<String,Object> map = res.getData();

        assertEquals("Test possible to delete a resource", 200, res.getStatus());
    }

    @Test
    public void testPermissionDenied() {
        Response response = ClientBuilder.newClient()
                .target("http://127.0.0.1:8080/rpgpro/character/")
                .request(MediaType.APPLICATION_JSON).header(
                        "Authorization", "Basic " +
                                java.util.Base64.getEncoder().
                                        encodeToString("wrong:wrong".getBytes()))
                .delete();


        assertEquals("Test not authenticated", 401, response.getStatus());
    }

    @Test
    public void testPermissionForPlayerDeniedWhenDelete() {
        Response response = ClientBuilder.newClient()
                .target("http://127.0.0.1:8080/rpgpro/character/delete/1")
                .request(MediaType.APPLICATION_JSON).header(
                        "Authorization", "Basic " +
                                java.util.Base64.getEncoder().
                                        encodeToString("super:super".getBytes()))
                .delete();

        assertEquals("Test access forbidden", 403, response.getStatus());
    }

    @Test
    public void testPermissionAllowedForPlayerToRead() {
        Response response = ClientBuilder.newClient()
                .target("http://127.0.0.1:8080/rpgpro/character/")
                .request(MediaType.APPLICATION_JSON).header(
                        "Authorization", "Basic " +
                                java.util.Base64.getEncoder().
                                        encodeToString("super:super".getBytes()))
                .get();
        assertEquals("Test access allowed ", 200, response.getStatus());
    }

}
