package org.bukkit.support;

import java.lang.reflect.Field;

public class Util {
    /*
    public static <T> T getInternalState(Object object, String fieldName) {
        return getInternalState(object.getClass(), object, fieldName);
    }
    */

    @SuppressWarnings("unchecked")
    public static <T> T getInternalState(Class<?> clazz, Object object, String fieldName) {
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            throw new RuntimeException("Not allowed to access " + clazz, e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find field " + fieldName, e);
        }

        field.setAccessible(true);
        try {
            return (T) field.get(object);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        throw new RuntimeException("Unable to get internal value");
    }
}
