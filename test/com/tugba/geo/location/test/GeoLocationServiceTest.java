package com.tugba.geo.location.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author tugba
 */
public class GeoLocationServiceTest {

    Client client = ClientBuilder.newBuilder().build();
    WebTarget target = client.target("http://localhost:8080/GeoLocations/location/geo/38/38");
    
    @Test
    public void testMethod() {

        Response response = target.request().get();
        String result = response.readEntity(String.class);
        assertEquals(result, "Location: Reşadiye, Doğanşehir, Turkey");      

    }

}
