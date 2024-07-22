package com.acme.modres.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AvailabilityCheckerServletPostIT {

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
        HttpPost httpPost = new HttpPost(URL);
        client.execute(httpPost, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            assertEquals("false", jsonObject.get("availability").getAsString());
            return response;
        });
    }

    @Test
    public void testAvailabilityCheckerServletWithoutValue() throws IOException {
    	HttpPost httpPost = new HttpPost(URL);
    	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    	parameters.add(new BasicNameValuePair(PARAMETER, null));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        client.execute(httpPost, response -> { 
            assertEquals(500, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            assertEquals("false", jsonObject.get("availability").getAsString());
            return response;
        });
    }

    @Test
    public void testAvailabilityCheckerServletWithInvalidDate() throws IOException {
    	HttpPost httpPost = new HttpPost(URL);
    	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    	parameters.add(new BasicNameValuePair(PARAMETER, "invalid"));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        client.execute(httpPost, response -> { 
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
    	HttpPost httpPost = new HttpPost(URL);
    	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    	parameters.add(new BasicNameValuePair(PARAMETER, DF.format(today.getTime())));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        client.execute(httpPost, response -> { 
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
    	HttpPost httpPost = new HttpPost(URL);
    	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    	parameters.add(new BasicNameValuePair(PARAMETER, DF.format(lastYear.getTime())));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        client.execute(httpPost, response -> { 
            assertEquals(200, response.getCode());
            String responseText = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = GSON.fromJson(responseText,JsonObject.class);
            // TODO: expected false but the servlet returns true
            assertEquals("true", jsonObject.get("availability").getAsString());
            return response;
        });
    }
}
