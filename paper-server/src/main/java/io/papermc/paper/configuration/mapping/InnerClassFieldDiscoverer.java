package io.papermc.paper.configuration.mapping;

import io.papermc.paper.configuration.ConfigurationPart;
import io.papermc.paper.configuration.WorldConfiguration;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.FieldDiscoverer;
import org.spongepowered.configurate.serialize.SerializationException;

import static io.leangen.geantyref.GenericTypeReflector.erase;

public final class InnerClassFieldDiscoverer implements FieldDiscoverer<Map<Field, Object>> {

    private final InnerClassInstanceSupplier instanceSupplier;
    private final FieldDiscoverer<Map<Field, Object>> delegate;

    @SuppressWarnings("unchecked")
    public InnerClassFieldDiscoverer(final Map<Class<?>, Object> initialOverrides) {
        this.instanceSupplier = new InnerClassInstanceSupplier(initialOverrides);
        this.delegate = (FieldDiscoverer<Map<Field, Object>>) FieldDiscoverer.object(this.instanceSupplier);
    }

    @Override
    public @Nullable <V> InstanceFactory<Map<Field, Object>> discover(final AnnotatedType target, final FieldCollector<Map<Field, Object>, V> collector) throws SerializationException {
        final Class<?> clazz = erase(target.getType());
        if (ConfigurationPart.class.isAssignableFrom(clazz)) {
            final FieldDiscoverer.@Nullable InstanceFactory<Map<Field, Object>> instanceFactoryDelegate = this.delegate.<V>discover(target, (name, type, annotations, deserializer, serializer) -> {
                if (!erase(type.getType()).equals(clazz.getEnclosingClass())) { // don't collect synth fields for inner classes
                    collector.accept(name, type, annotations, deserializer, serializer);
                }
            });
            if (instanceFactoryDelegate instanceof MutableInstanceFactory<Map<Field, Object>> mutableInstanceFactoryDelegate) {
                return new InnerClassInstanceFactory(this.instanceSupplier, mutableInstanceFactoryDelegate, target);
            }
        }
        return null;
    }

    public static FieldDiscoverer<?> worldConfig(WorldConfiguration worldConfiguration) {
        final Map<Class<?>, Object> overrides = Map.of(
            WorldConfiguration.class, worldConfiguration
        );
        return new InnerClassFieldDiscoverer(overrides);
    }

    public static FieldDiscoverer<?> globalConfig() {
        return new InnerClassFieldDiscoverer(Collections.emptyMap());
    }
}
