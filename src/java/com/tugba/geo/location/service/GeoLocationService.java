package com.tugba.geo.location.service;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author tugba
 */
@Path("geo")
public class GeoLocationService {

    private static String activeService = "opencagedata.com"; // aktif servisi default olarak opencagedata yaptım. Burda 
    //
    @GET
    public String getActiveService() {
        return activeService;
    }

    @PUT
    public String updateActiveService(String service) {
        if ("oc".equals(service)) {
            activeService = "opencagedata.com"; 
            return activeService;
        } else if ("ps".equals(service)) {
            activeService = "positionstack.com";
            return activeService;
        } else {
            return "invalid input";
        }
    }

    @GET
    @Path("/{lat}/{lon}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLocation(@PathParam("lat") String lat, @PathParam("lon") String lon) {
        String location = "not found";
        
        if ("opencagedata.com".equals(activeService)) {
            // opencagedata.com
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target("https://api.opencagedata.com/geocode/v1/json?q=" + lat + "+" + lon + "&key=efd71112ce0042f8ad40e44ef97eb032");
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            String responseData = response.readEntity(String.class); //Response'u String olarak alındı. String içinden "results" ordan formatted ı bul demek sıkıntılı olurdu 
            JSONObject jsonResponseData = new JSONObject(responseData); // Bu yüzden String olarak aldıgımızı JsonObject objesine cevrildi.Artık Json formatındayım.
            JSONArray results = jsonResponseData.getJSONArray("results");//Neyi almak istediğimi yazdım. Result array olarak aldık . 
            location = results.getJSONObject(0).getString("formatted");//array in içinden de formatted ı aldık. Result arraydi.getJsonobject(0) result oluyor burda. onunda formatted degerini aldık.

            System.out.println("formatted " + location);
        } else {
            // positionstack.com
            Client client = ClientBuilder.newClient();
            WebTarget webTarget = client.target("http://api.positionstack.com/v1/reverse?access_key=b6a803d1d3675851d75677c149e271eb&query=" + lat + "," + lon);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            String responseData = response.readEntity(String.class);
            JSONObject jsonResponseData = new JSONObject(responseData);
            JSONArray results = jsonResponseData.getJSONArray("data");
            location = results.getJSONObject(0).getString("label");

            System.out.println("label " + location);
        }

        return "Location: " + location;
    }

}
