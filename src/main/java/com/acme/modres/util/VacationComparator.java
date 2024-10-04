package com.acme.modres.util;

import sun.misc.Unsafe;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VacationComparator {
  static final Unsafe myUnsafe = getUnsafe();
  private static final Logger logger = Logger.getLogger(VacationComparator.class.getName());

  public VacationComparator() {
    logger.log(Level.INFO, "Comparing vacations...");
  }

  private static Unsafe getUnsafe() {
    try {
      return Unsafe.getUnsafe();
    } catch (SecurityException e) {
      logger.log(Level.SEVERE, "Unable to get Unsafe.");
    }
    return null;
  }
}
