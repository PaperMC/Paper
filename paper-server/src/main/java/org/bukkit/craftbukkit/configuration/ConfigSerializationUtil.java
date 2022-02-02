package org.bukkit.craftbukkit.configuration;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Utilities related to the serialization and deserialization of {@link ConfigurationSerializable}s.
 */
public final class ConfigSerializationUtil {

    public static String getString(Map<?, ?> map, String key, boolean nullable) {
        return getObject(String.class, map, key, nullable);
    }

    public static UUID getUuid(Map<?, ?> map, String key, boolean nullable) {
        String uuidString = ConfigSerializationUtil.getString(map, key, nullable);
        if (uuidString == null) return null;
        return UUID.fromString(uuidString);
    }

    public static <T> T getObject(Class<T> clazz, Map<?, ?> map, String key, boolean nullable) {
        final Object object = map.get(key);
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        if (object == null) {
            if (!nullable) {
                throw new NoSuchElementException(map + " does not contain " + key);
            }
            return null;
        }
        throw new IllegalArgumentException(key + "(" + object + ") is not a valid " + clazz);
    }

    private ConfigSerializationUtil() {
    }
}
