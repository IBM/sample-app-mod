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

import sun.misc.Unsafe;

public class ModResortsEnv {
  static final Unsafe myUnsafe = myGetUnsafe();
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

  private String getAdminEndpoint() {
    try {
      Field f = EnvConfig.class.getDeclaredField("adminApiEndpoint");
      long offset = myUnsafe.objectFieldOffset(f);
      return (String) myUnsafe.getObject(envConfig, offset);
    } catch (NoSuchFieldException | SecurityException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Unsafe myGetUnsafe() {
    try {
      Field f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      Unsafe unsafe = (Unsafe) f.get(null);
      return unsafe;
    } catch (NoSuchFieldException ex) {
      return null;
    } catch (IllegalArgumentException ex) {
      return null;
    } catch (IllegalAccessException ex) {
      return null;
    }
  }

  // Test driver for class
  public static void main(String args[]) {
  }
}
