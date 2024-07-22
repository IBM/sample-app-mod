package com.acme.modres.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WelcomeServletGetIT {

    private final static String URL = "http://localhost:9080/resorts/welcome";

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
    public void testWelcomeServlet() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        client.execute(httpGet, response -> { 
            assertEquals(200, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            assertEquals("Welcome defaultUser to our site!  Enjoy!\n", responseText);
            return response;
        });
    }

}
