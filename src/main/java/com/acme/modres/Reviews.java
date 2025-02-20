package com.acme.modres;

import java.io.InputStream;
import java.util.Scanner;

// Assisted by watsonx Code Assistant 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class provides a REST API for accessing customer reviews.
 * 
 * @author Watson Code Assistant
 */
@Path("/v1")
public class Reviews {

  /**
   * Returns a list of customer reviews in JSON format.
   * 
   * @return Response containing a JSON array of reviews
   */
  @GET
  @Path("/reviews")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReviews() {

    System.out.println("Entering getReviews");
    
    InputStream is = getClass().getClassLoader().getResourceAsStream("reviews.json");
    Scanner scanner = new Scanner(is);

    String json = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";

    return Response.ok(json).build();
  }
}
