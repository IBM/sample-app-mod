package com.acme.modres.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogoutServletGetIT {

    private final static String URL = "http://localhost:9080/resorts/logout";

    private CloseableHttpClient client;

    @BeforeEach
    public void setup() {
        client = HttpClientBuilder.create().build();
    }

    @AfterEach
    public void teardown() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  
    @Test
    public void testLogoutServlet() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        client.execute(httpGet, response -> { 
            assertEquals(200, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            assertTrue(responseText.contains("LOG IN TO MODRESORTS"));
            return response;
        });
    }

}
