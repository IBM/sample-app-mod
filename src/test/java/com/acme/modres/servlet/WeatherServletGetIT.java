package com.acme.modres.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WeatherServletGetIT {

    private final static String URL = "http://localhost:9080/resorts/weather";
    private final static String PARAMETER = "selectedCity";
    private final static Gson GSON = new Gson();

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
    public void testWeatherServletWithoutParameters() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        client.execute(httpGet, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            assertTrue(responseText.contains("City is not defined"));
            return response;
        });
    }

    @Test
    public void testWeatherServletWithoutValue() throws IOException {
        HttpGet httpGet = new HttpGet(URL + "?" + PARAMETER);
        client.execute(httpGet, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            assertTrue(responseText.contains("City is not defined"));
            return response;
        });
    }

    @Test
    public void testWeatherServletWithInvalidCity() throws IOException {
        HttpGet httpGet = new HttpGet(URL + "?" + PARAMETER + "=" + "InvalidCity");
        client.execute(httpGet, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            assertTrue(responseText.contains("City is invalid."));
            return response;
        });
    }

    @Test
    public void testWeatherServletParis() throws IOException {
        HttpGet httpGet = new HttpGet(URL + "?" + PARAMETER + "=Paris");
        client.execute(httpGet, response -> { 
            assertEquals(200, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            assertNotNull(jsonObject.get("location"));
            assertNotNull(jsonObject.get("current_observation"));
            assertNotNull(jsonObject.get("forecast"));
            String city = jsonObject.get("location").getAsJsonObject()
                    .get("city").getAsString();
            assertEquals("Paris", city);
            return response;
        });
    }

}
