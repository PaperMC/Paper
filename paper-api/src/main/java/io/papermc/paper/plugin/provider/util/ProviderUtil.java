package io.papermc.paper.plugin.provider.util;

import com.destroystokyo.paper.util.SneakyThrow;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InaccessibleObjectException;

/**
 * An <b>internal</b> utility type that holds logic for loading a provider-like type from a classloaders.
 * Provides, at least in the context of this utility, define themselves as implementations of a specific parent
 * interface/type, e.g. {@link org.bukkit.plugin.java.JavaPlugin} and implement a no-args constructor.
 */
@NullMarked
@ApiStatus.Internal
public final class ProviderUtil {

    /**
     * Loads the class found at the provided fully qualified class name from the passed classloader, creates a new
     * instance of it using the no-args constructor, that should exist as per this method contract, and casts it to the
     * provided parent type.
     *
     * @param clazz     the fully qualified name of the class to load
     * @param classType the parent type that the created object found at the {@code clazz} name should be cast to
     * @param loader    the loader from which the class should be loaded
     * @param <T>       the generic type of the parent class the created object will be cast to
     * @return the object instantiated from the class found at the provided FQN, cast to the parent type
     */
    public static <T> T loadClass(final String clazz, final Class<T> classType, final ClassLoader loader) {
        return loadClass(clazz, classType, loader, null);
    }

    /**
     * Loads the class found at the provided fully qualified class name from the passed classloader, creates a new
     * instance of it using the no-args constructor, that should exist as per this method contract, and casts it to the
     * provided parent type.
     *
     * @param clazz     the fully qualified name of the class to load
     * @param classType the parent type that the created object found at the {@code clazz} name should be cast to
     * @param loader    the loader from which the class should be loaded
     * @param onError   a runnable that is executed before any unknown exception is raised through a sneaky throw.
     * @param <T>       the generic type of the parent class the created object will be cast to
     * @return the object instantiated from the class found at the provided fully qualified class name, cast to the
     * parent type
     */
    public static <T> T loadClass(final String clazz, final Class<T> classType, final ClassLoader loader, final @Nullable Runnable onError) {
        try {
            final T clazzInstance;

            try {
                final Class<?> jarClass = Class.forName(clazz, true, loader);

                final Class<? extends T> pluginClass;
                try {
                    pluginClass = jarClass.asSubclass(classType);
                } catch (final ClassCastException ex) {
                    throw new ClassCastException("class '%s' does not extend '%s'".formatted(clazz, classType));
                }

                final Constructor<? extends T> constructor = pluginClass.getDeclaredConstructor();
                try {
                    constructor.setAccessible(true); // Allow non-public constructors
                } catch (final InaccessibleObjectException | SecurityException ex) {
                    throw new RuntimeException("Inaccessible constructor");
                }

                clazzInstance = constructor.newInstance();
            } catch (final IllegalAccessException exception) {
                throw new RuntimeException("No public constructor");
            } catch (final InstantiationException exception) {
                throw new RuntimeException("Abnormal class instantiation", exception);
            }

            return clazzInstance;
        } catch (final Throwable e) {
            if (onError != null) {
                onError.run();
            }
            SneakyThrow.sneaky(e);
        }

        throw new AssertionError(); // Shouldn't happen
    }

}
