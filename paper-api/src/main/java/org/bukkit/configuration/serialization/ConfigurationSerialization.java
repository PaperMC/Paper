package org.bukkit.configuration.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.banner.Pattern;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * Utility class for storing and retrieving classes for {@link Configuration}.
 */
public class ConfigurationSerialization {
    public static final String SERIALIZED_TYPE_KEY = "==";
    private final Class<? extends ConfigurationSerializable> clazz;
    private static Map<String, Class<? extends ConfigurationSerializable>> aliases = new HashMap<String, Class<? extends ConfigurationSerializable>>();

    static {
        registerClass(Vector.class);
        registerClass(BlockVector.class);
        registerClass(ItemStack.class);
        registerClass(Color.class);
        registerClass(PotionEffect.class);
        registerClass(FireworkEffect.class);
        registerClass(Pattern.class);
        registerClass(Location.class);
    }

    protected ConfigurationSerialization(Class<? extends ConfigurationSerializable> clazz) {
        this.clazz = clazz;
    }

    protected Method getMethod(String name, boolean isStatic) {
        try {
            Method method = clazz.getDeclaredMethod(name, Map.class);

            if (!ConfigurationSerializable.class.isAssignableFrom(method.getReturnType())) {
                return null;
            }
            if (Modifier.isStatic(method.getModifiers()) != isStatic) {
                return null;
            }

            return method;
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (SecurityException ex) {
            return null;
        }
    }

    protected Constructor<? extends ConfigurationSerializable> getConstructor() {
        try {
            return clazz.getConstructor(Map.class);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (SecurityException ex) {
            return null;
        }
    }

    protected ConfigurationSerializable deserializeViaMethod(Method method, Map<String, ?> args) {
        try {
            ConfigurationSerializable result = (ConfigurationSerializable) method.invoke(null, args);

            if (result == null) {
                Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization: method returned null");
            } else {
                return result;
            }
        } catch (Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(
                    Level.SEVERE,
                    "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    protected ConfigurationSerializable deserializeViaCtor(Constructor<? extends ConfigurationSerializable> ctor, Map<String, ?> args) {
        try {
            return ctor.newInstance(args);
        } catch (Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(
                    Level.SEVERE,
                    "Could not call constructor '" + ctor.toString() + "' of " + clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    public ConfigurationSerializable deserialize(Map<String, ?> args) {
        Validate.notNull(args, "Args must not be null");

        ConfigurationSerializable result = null;
        Method method = null;

        if (result == null) {
            method = getMethod("deserialize", true);

            if (method != null) {
                result = deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            method = getMethod("valueOf", true);

            if (method != null) {
                result = deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            Constructor<? extends ConfigurationSerializable> constructor = getConstructor();

            if (constructor != null) {
                result = deserializeViaCtor(constructor, args);
            }
        }

        return result;
    }

    /**
     * Attempts to deserialize the given arguments into a new instance of the
     * given class.
     * <p>
     * The class must implement {@link ConfigurationSerializable}, including
     * the extra methods as specified in the javadoc of
     * ConfigurationSerializable.
     * <p>
     * If a new instance could not be made, an example being the class not
     * fully implementing the interface, null will be returned.
     *
     * @param args Arguments for deserialization
     * @param clazz Class to deserialize into
     * @return New instance of the specified class
     */
    public static ConfigurationSerializable deserializeObject(Map<String, ?> args, Class<? extends ConfigurationSerializable> clazz) {
        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    /**
     * Attempts to deserialize the given arguments into a new instance of the
     * given class.
     * <p>
     * The class must implement {@link ConfigurationSerializable}, including
     * the extra methods as specified in the javadoc of
     * ConfigurationSerializable.
     * <p>
     * If a new instance could not be made, an example being the class not
     * fully implementing the interface, null will be returned.
     *
     * @param args Arguments for deserialization
     * @return New instance of the specified class
     */
    public static ConfigurationSerializable deserializeObject(Map<String, ?> args) {
        Class<? extends ConfigurationSerializable> clazz = null;

        if (args.containsKey(SERIALIZED_TYPE_KEY)) {
            try {
                String alias = (String) args.get(SERIALIZED_TYPE_KEY);

                if (alias == null) {
                    throw new IllegalArgumentException("Cannot have null alias");
                }
                clazz = getClassByAlias(alias);
                if (clazz == null) {
                    throw new IllegalArgumentException("Specified class does not exist ('" + alias + "')");
                }
            } catch (ClassCastException ex) {
                ex.fillInStackTrace();
                throw ex;
            }
        } else {
            throw new IllegalArgumentException("Args doesn't contain type key ('" + SERIALIZED_TYPE_KEY + "')");
        }

        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    /**
     * Registers the given {@link ConfigurationSerializable} class by its
     * alias
     *
     * @param clazz Class to register
     */
    public static void registerClass(Class<? extends ConfigurationSerializable> clazz) {
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate == null) {
            registerClass(clazz, getAlias(clazz));
            registerClass(clazz, clazz.getName());
        }
    }

    /**
     * Registers the given alias to the specified {@link
     * ConfigurationSerializable} class
     *
     * @param clazz Class to register
     * @param alias Alias to register as
     * @see SerializableAs
     */
    public static void registerClass(Class<? extends ConfigurationSerializable> clazz, String alias) {
        aliases.put(alias, clazz);
    }

    /**
     * Unregisters the specified alias to a {@link ConfigurationSerializable}
     *
     * @param alias Alias to unregister
     */
    public static void unregisterClass(String alias) {
        aliases.remove(alias);
    }

    /**
     * Unregisters any aliases for the specified {@link
     * ConfigurationSerializable} class
     *
     * @param clazz Class to unregister
     */
    public static void unregisterClass(Class<? extends ConfigurationSerializable> clazz) {
        while (aliases.values().remove(clazz)) {
            ;
        }
    }

    /**
     * Attempts to get a registered {@link ConfigurationSerializable} class by
     * its alias
     *
     * @param alias Alias of the serializable
     * @return Registered class, or null if not found
     */
    public static Class<? extends ConfigurationSerializable> getClassByAlias(String alias) {
        return aliases.get(alias);
    }

    /**
     * Gets the correct alias for the given {@link ConfigurationSerializable}
     * class
     *
     * @param clazz Class to get alias for
     * @return Alias to use for the class
     */
    public static String getAlias(Class<? extends ConfigurationSerializable> clazz) {
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate != null) {
            if ((delegate.value() == null) || (delegate.value() == clazz)) {
                delegate = null;
            } else {
                return getAlias(delegate.value());
            }
        }

        if (delegate == null) {
            SerializableAs alias = clazz.getAnnotation(SerializableAs.class);

            if ((alias != null) && (alias.value() != null)) {
                return alias.value();
            }
        }

        return clazz.getName();
    }
}
