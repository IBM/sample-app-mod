package com.acme.modres.util;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.misc.Unsafe;


public class Partners {
  static final Unsafe myUnsafe = myGetUnsafe();
  private static final Logger logger = Logger.getLogger(Partners.class.getName());
  private List<String> urls;
  private List<URL> partnerWebsites;

  public Partners() {
    logger.log(Level.INFO, "Modresorts partners");
    init();
  }

  private List<URL> getPartnerWebsites() {
    return partnerWebsites;
  }

  private List<URL> initPartnerWebsites() {
    List<URL> partnerUrls = new ArrayList<>();
    try {
      for (String s : urls) {
        URL url = new URL(s);
        System.out.println("Valid partner URL: " + s.toString());
        partnerUrls.add(url);
      }
    } catch (MalformedURLException e) {  
        myUnsafe.throwException(new MalformedURLException("Invalid partner url"));
    }
    return partnerUrls;
  }

  private void init() {
    urls = new ArrayList<>();
    urls.add("http://example1.com");
    urls.add("http://example2.com");
    urls.add("http://example3.com");
    urls.add("http://example4.com");

    partnerWebsites = initPartnerWebsites();
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
    Partners p = new Partners();
    List<URL> websites = p.getPartnerWebsites();
    for (URL u : websites) {
      System.out.println(u);
    }
  }
}
