package com.acme.modres.util;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

/**
 * Demonstrates using sun.misc.Unsafe to access private fields in ModResorts application.
 */
public class ModResortsUnsafeFieldAccessor {
    
    private static final Unsafe unsafe;
    
    static {
        try {
            // Get the Unsafe instance via reflection
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get Unsafe instance", e);
        }
    }
    
    /**
     * Demonstrates accessing a private String field using Unsafe.
     */
    public static String getPrivateStringField(Object target, String fieldName) throws Exception {
        // Get the field from the class
        Field field = target.getClass().getDeclaredField(fieldName);
        
        // Get the memory offset of the field
        long fieldOffset = unsafe.objectFieldOffset(field);
        
        // Read the field value directly from memory using Unsafe
        Object value = unsafe.getObject(target, fieldOffset);
        
        return (String) value;
    }
}
