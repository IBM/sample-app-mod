package com.acme.modres.util;

/**
 * Sample class with a private field that will be accessed using Unsafe.
 * This represents sensitive data in the ModResorts application.
 */
public class ModResortsSecretData {
    
  private String token = "mod-resorts-secret-key-12345";
  
  private int maxReservationLimit = 100;
  
  public ModResortsSecretData() {
  }
  
  public String getPublicInfo() {
      return "This is public information";
  }
}

