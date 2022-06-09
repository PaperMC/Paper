package io.papermc.paper.configuration.mapping;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.spongepowered.configurate.objectmapping.FieldDiscoverer;
import org.spongepowered.configurate.serialize.SerializationException;

import static io.leangen.geantyref.GenericTypeReflector.erase;

final class InnerClassInstanceFactory implements FieldDiscoverer.MutableInstanceFactory<Map<Field, Object>> {

    private final InnerClassInstanceSupplier instanceSupplier;
    private final FieldDiscoverer.MutableInstanceFactory<Map<Field, Object>> fallback;
    private final AnnotatedType targetType;

    InnerClassInstanceFactory(final InnerClassInstanceSupplier instanceSupplier, final FieldDiscoverer.MutableInstanceFactory<Map<Field, Object>> fallback, final AnnotatedType targetType) {
        this.instanceSupplier = instanceSupplier;
        this.fallback = fallback;
        this.targetType = targetType;
    }

    @Override
    public Map<Field, Object> begin() {
        return this.fallback.begin();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void complete(final Object instance, final Map<Field, Object> intermediate) throws SerializationException {
        final Iterator<Map.Entry<Field, Object>> iter = intermediate.entrySet().iterator();
        try {
            while (iter.hasNext()) { // manually merge any mergeable maps
                Map.Entry<Field, Object> entry = iter.next();
                if (entry.getKey().isAnnotationPresent(MergeMap.class) && Map.class.isAssignableFrom(entry.getKey().getType()) && intermediate.get(entry.getKey()) instanceof Map<?, ?> map) {
                    iter.remove();
                    Map<Object, Object> existingMap = (Map<Object, Object>) entry.getKey().get(instance);
                    if (existingMap != null) {
                        existingMap.putAll(map);
                    } else {
                        entry.getKey().set(instance, entry.getValue());
                    }
                }
            }
        } catch (final IllegalAccessException e) {
            throw new SerializationException(this.targetType.getType(), e);
        }
        this.fallback.complete(instance, intermediate);
    }

    @Override
    public Object complete(final Map<Field, Object> intermediate) throws SerializationException {
        final Object targetInstance = Objects.requireNonNull(this.instanceSupplier.instanceMap().get(erase(this.targetType.getType())), () -> this.targetType.getType() + " must already have an instance created");
        this.complete(targetInstance, intermediate);
        return targetInstance;
    }

    @Override
    public boolean canCreateInstances() {
        return this.fallback.canCreateInstances();
    }
}
