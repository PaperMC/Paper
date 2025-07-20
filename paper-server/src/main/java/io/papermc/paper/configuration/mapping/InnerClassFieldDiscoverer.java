package io.papermc.paper.configuration.mapping;

import io.leangen.geantyref.GenericTypeReflector;
import io.papermc.paper.configuration.ConfigurationPart;
import io.papermc.paper.configuration.WorldConfiguration;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.FieldDiscoverer;
import org.spongepowered.configurate.serialize.SerializationException;

import static io.leangen.geantyref.GenericTypeReflector.erase;
import static java.util.Objects.requireNonNullElseGet;

public final class InnerClassFieldDiscoverer implements FieldDiscoverer<Map<Field, DeserializedFieldInfo<?>>> {

    private final InnerClassInstanceSupplier instanceSupplier;
    private final FieldDiscoverer<Map<Field, Object>> delegate;
    private final Map<Class<? extends Annotation>, List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>>> fieldProcessors;

    private InnerClassFieldDiscoverer(final InnerClassInstanceSupplier instanceSupplier, final FieldDiscoverer<Map<Field, Object>> delegate, final Map<Class<? extends Annotation>, List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>>> fieldProcessors) {
        this.instanceSupplier = instanceSupplier;
        this.delegate = delegate;
        this.fieldProcessors = fieldProcessors;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public @Nullable <V> InstanceFactory<Map<Field, DeserializedFieldInfo<?>>> discover(final AnnotatedType target, final FieldCollector<Map<Field, DeserializedFieldInfo<?>>, V> collector) throws SerializationException {
        final Class<?> clazz = erase(target.getType());
        if (ConfigurationPart.class.isAssignableFrom(clazz)) {
            // we don't care about the returned instance factory, we just need to call it to collect fields
            final Object dummyInstance = this.delegate.<V>discover(target, (name, fieldType, container, deserializer, serializer) -> {
                if (!erase(fieldType.getType()).equals(clazz.getEnclosingClass())) { // don't collect synth fields for inner classes
                    FieldProcessor<?> processor = null;
                    processor: for (final Annotation annotation : container.getAnnotations()) {
                        final List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>> definitions = this.fieldProcessors.get(annotation.annotationType());
                        if (definitions != null) {
                            for (final Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>> def : definitions) {
                                if (GenericTypeReflector.isSuperType(def.type().getType(), GenericTypeReflector.box(fieldType.getType()))) {
                                    processor = ((FieldProcessor.Factory)def.factory()).make(annotation, fieldType);
                                    break processor;
                                }
                            }
                        }
                    }
                    final FieldProcessor<?> finalProcessor = processor;
                    collector.accept(
                        name,
                        fieldType,
                        container,
                        (intermediate, newValue, implicitInitializer) -> {
                            // we only create this map to collect the field, it should only ever have 1 entry
                            final SequencedMap<Field, Object> map = new LinkedHashMap<>();
                            deserializer.accept(map, newValue, implicitInitializer);
                            final Object deserializedValue;
                            deserializedValue = requireNonNullElseGet(newValue, () -> new ImplicitProvider(implicitInitializer));
                            intermediate.put(map.firstEntry().getKey(), new DeserializedFieldInfo<>(fieldType, deserializedValue, finalProcessor));
                        },
                        serializer
                    );
                }
            });
            if (dummyInstance == null) {
                return null;
            }
            return new InnerClassInstanceFactory(this.instanceSupplier, target);
        }
        return null;
    }

    record ImplicitProvider(Supplier<Object> provider) {
    }

    @SuppressWarnings("unchecked")
    private static InnerClassFieldDiscoverer create(final Map<Class<?>, Object> overrides, final List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>> fieldProcessors) {
        final Map<Class<? extends Annotation>, List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>>> processors = new LinkedHashMap<>();
        for (final Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>> definition : fieldProcessors) {
            processors.computeIfAbsent(definition.annotation(), k -> new ArrayList<>()).add(definition);
        }
        final InnerClassInstanceSupplier instanceSupplier = new InnerClassInstanceSupplier(overrides);
        return new InnerClassFieldDiscoverer(instanceSupplier, (FieldDiscoverer<Map<Field, Object>>) FieldDiscoverer.object(instanceSupplier), processors);
    }

    public static FieldDiscoverer<?> worldConfig(final WorldConfiguration worldConfiguration, final List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>> fieldProcessors) {
        final Map<Class<?>, Object> overrides = Map.of(
            WorldConfiguration.class, worldConfiguration
        );
        return create(overrides, fieldProcessors);
    }

    public static FieldDiscoverer<?> globalConfig(final List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>> fieldProcessors) {
        return create(Collections.emptyMap(), fieldProcessors);
    }
}
