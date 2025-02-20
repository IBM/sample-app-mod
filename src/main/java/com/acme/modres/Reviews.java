package com.acme.modres;

import java.io.InputStream;
import java.util.Scanner;

// Assisted by watsonx Code Assistant 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1")
public class Reviews {
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
