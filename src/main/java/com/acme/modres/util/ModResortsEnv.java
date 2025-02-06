package com.acme.modres.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.acme.common.EnvConfig;

public class ModResortsEnv {
  private static final Logger logger = Logger.getLogger(ModResortsEnv.class.getName());
  private EnvConfig envConfig;

  public ModResortsEnv() {
    logger.log(Level.INFO, "Modresorts environment configuration details");
    envConfig = new EnvConfig();
  }

  public void reset() {
    String adminEndpoint = getAdminEndpoint();
    URL url = null;
    try {
      url = URI.create(adminEndpoint).toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) url.openConnection();
      HttpURLConnection http = (HttpURLConnection) con;
      http.setRequestMethod("POST");
      http.setDoOutput(true);
      BufferedWriter httpRequestBodyWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
      httpRequestBodyWriter.write("reset=true");
      httpRequestBodyWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getAdminEndpoint() {
    try {
      Field f = EnvConfig.class.getDeclaredField("adminApiEndpoint");
      f.setAccessible(true);
      return (String) f.get(envConfig);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Test driver for class
  public static void main(String args[]) {
  }
}
