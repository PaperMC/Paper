package io.papermc.paper.configuration.mapping;

import io.papermc.paper.configuration.ConfigurationPart;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.util.CheckedFunction;
import org.spongepowered.configurate.util.CheckedSupplier;

import static io.leangen.geantyref.GenericTypeReflector.erase;

/**
 * This instance factory handles creating non-static inner classes by tracking all instances of objects that extend
 * {@link ConfigurationPart}. Only 1 instance of each {@link ConfigurationPart} should be present for each instance
 * of the field discoverer this is used in.
 */
final class InnerClassInstanceSupplier implements CheckedFunction<AnnotatedType, @Nullable Supplier<Object>, SerializationException> {

    private final Map<Class<?>, Object> instanceMap = new HashMap<>();
    private final Map<Class<?>, Object> initialOverrides;

    /**
     * @param initialOverrides map of types to objects to preload the config objects with.
     */
    InnerClassInstanceSupplier(final Map<Class<?>, Object> initialOverrides) {
        this.initialOverrides = initialOverrides;
    }

    @Override
    public Supplier<Object> apply(final AnnotatedType target) throws SerializationException {
        final Class<?> type = erase(target.getType());
        if (this.initialOverrides.containsKey(type)) {
            this.instanceMap.put(type, this.initialOverrides.get(type));
            return () -> this.initialOverrides.get(type);
        }
        if (ConfigurationPart.class.isAssignableFrom(type) && !this.instanceMap.containsKey(type)) {
            try {
                final Constructor<?> constructor;
                final CheckedSupplier<Object, ReflectiveOperationException> instanceSupplier;
                if (type.getEnclosingClass() != null && !Modifier.isStatic(type.getModifiers())) {
                    final Object instance = this.instanceMap.get(type.getEnclosingClass());
                    if (instance == null) {
                        throw new SerializationException("Cannot create a new instance of an inner class " + type.getName() + " without an instance of its enclosing class " + type.getEnclosingClass().getName());
                    }
                    constructor = type.getDeclaredConstructor(type.getEnclosingClass());
                    instanceSupplier = () -> constructor.newInstance(instance);
                } else {
                    constructor = type.getDeclaredConstructor();
                    instanceSupplier = constructor::newInstance;
                }
                constructor.setAccessible(true);
                final Object instance = instanceSupplier.get();
                this.instanceMap.put(type, instance);
                return () -> instance;
            } catch (final ReflectiveOperationException e) {
                throw new SerializationException(ConfigurationPart.class, target + " must be a valid ConfigurationPart", e);
            }
        } else {
            throw new SerializationException(target + " must be a valid ConfigurationPart");
        }
    }

    Map<Class<?>, Object> instanceMap() {
        return this.instanceMap;
    }

}
