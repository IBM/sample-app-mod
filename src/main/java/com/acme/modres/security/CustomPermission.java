package com.acme.modres.security;

import java.security.BasicPermission;

public class CustomPermission extends BasicPermission {
  private static final long serialVersionUID = 1;

  public CustomPermission(String name) {
    super(name);
  }

  public CustomPermission(String name, String actions) {
    super(name, actions);
  }
}
