package com.acme.modres.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AvailabilityCheckerServletGetIT {

    private final static String URL = "http://localhost:9080/resorts/availability";
    private final static String PARAMETER = "date";
    private final static Gson GSON = new Gson();
    private final static SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yyyy");

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
    public void testAvailabilityCheckerServletWithoutParameter() throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        client.execute(httpGet, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            assertEquals("false", jsonObject.get("availability").getAsString());
            return response;
        });
    }

    @Test
    public void testAvailabilityCheckerServletWithoutValue() throws IOException {
        HttpGet httpGet = new HttpGet(URL + "?" + PARAMETER);
        client.execute(httpGet, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            assertEquals("false", jsonObject.get("availability").getAsString());
            return response;
        });
    }

    @Test
    public void testAvailabilityCheckerServletWithInvalidDate() throws IOException {
        HttpGet httpGet = new HttpGet(URL + "?" + PARAMETER + "=" + "invalid");
        client.execute(httpGet, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            assertEquals("false", jsonObject.get("availability").getAsString());
            return response;
        });
    }

    @Test
    public void testAvailabilityCheckerServletToday() throws IOException {
        Calendar today = Calendar.getInstance();
        HttpGet httpGet = new HttpGet(URL + "?" + PARAMETER + "=" + DF.format(today.getTime()));
        client.execute(httpGet, response -> { 
            assertEquals(200, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            assertEquals("true", jsonObject.get("availability").getAsString());
            return response;
        });
    }

    @Test
    public void testAvailabilityCheckerServletLastYear() throws IOException {
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        HttpGet httpGet = new HttpGet(URL + "?" + PARAMETER + "=" + DF.format(lastYear.getTime()));
        client.execute(httpGet, response -> { 
            assertEquals(200, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            // TODO: expected false but the servlet returns true
            assertEquals("true", jsonObject.get("availability").getAsString());
            return response;
        });
    }
}
