package io.papermc.paper.configuration.mapping;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.spongepowered.configurate.objectmapping.FieldDiscoverer;
import org.spongepowered.configurate.serialize.SerializationException;

import static io.leangen.geantyref.GenericTypeReflector.erase;

final class InnerClassInstanceFactory implements FieldDiscoverer.MutableInstanceFactory<Map<Field, DeserializedFieldInfo<?>>> {

    private final InnerClassInstanceSupplier instanceSupplier;
    private final AnnotatedType targetType;

    InnerClassInstanceFactory(
        final InnerClassInstanceSupplier instanceSupplier,
        final AnnotatedType targetType
    ) {
        this.instanceSupplier = instanceSupplier;
        this.targetType = targetType;
    }

    @Override
    public Map<Field, DeserializedFieldInfo<?>> begin() {
        return new LinkedHashMap<>();
    }

    @Override
    public void complete(final Object instance, final Map<Field, DeserializedFieldInfo<?>> intermediate) throws SerializationException {
        // modeled off of native ObjectFieldDiscoverer
        for (final Map.Entry<Field, DeserializedFieldInfo<?>> entry : intermediate.entrySet()) {
            final boolean hasProcessor = entry.getValue().processor() != null;
            try {
                final Object valueInField = entry.getKey().get(instance);
                if (entry.getValue().deserializedValue() instanceof InnerClassFieldDiscoverer.ImplicitProvider(final Supplier<Object> provider)) {
                    Object newFieldValue = provider.get();
                    if (hasProcessor) {
                        newFieldValue = entry.getValue().runProcessor(valueInField, newFieldValue);
                    }
                    if (newFieldValue != null) {
                        if (valueInField == null) {
                            entry.getKey().set(instance, newFieldValue);
                        }
                    }
                } else {
                    Object newFieldValue = entry.getValue().deserializedValue();
                    if (hasProcessor) {
                        newFieldValue = entry.getValue().runProcessor(valueInField, newFieldValue);
                    }
                    entry.getKey().set(instance, newFieldValue);
                }
            } catch (final IllegalAccessException e) {
                throw new SerializationException(this.targetType.getType(), e);
            } catch (final SerializationException ex) {
                ex.initType(entry.getValue().fieldType());

            }
        }
    }

    @Override
    public Object complete(final Map<Field, DeserializedFieldInfo<?>> intermediate) throws SerializationException {
        final Object targetInstance = Objects.requireNonNull(this.instanceSupplier.instanceMap().get(erase(this.targetType.getType())), () -> this.targetType.getType() + " must already have an instance created");
        this.complete(targetInstance, intermediate);
        return targetInstance;
    }

    @Override
    public boolean canCreateInstances() {
        return true;
    }
}
