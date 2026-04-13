package io.papermc.paper.configuration.serializer.collection.map;

import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeFactory;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@SuppressWarnings("rawtypes")
public abstract class FastutilMapSerializer<M extends Map<?, ?>> implements TypeSerializer.Annotated<M> {

    private final Function<? super Map, ? extends M> factory;

    protected FastutilMapSerializer(final Function<? super Map, ? extends M> factory) {
        this.factory = factory;
    }

    @Override
    public M deserialize(final AnnotatedType annotatedType, final ConfigurationNode node) throws SerializationException {
        final Map map = (Map) node.get(this.createAnnotatedMapType((AnnotatedParameterizedType) annotatedType));
        return this.factory.apply(map == null ? Collections.emptyMap() : map);
    }

    @Override
    public void serialize(final AnnotatedType annotatedType, final @Nullable M obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null || obj.isEmpty()) {
            node.raw(null);
        } else {
            final AnnotatedType baseMapType = this.createAnnotatedMapType((AnnotatedParameterizedType) annotatedType);
            node.set(baseMapType, obj);
        }
    }

    private AnnotatedType createAnnotatedMapType(final AnnotatedParameterizedType type) {
        final Type baseType = this.createBaseMapType((ParameterizedType) type.getType());
        return GenericTypeReflector.annotate(baseType, type.getAnnotations());
    }

    protected abstract Type createBaseMapType(final ParameterizedType type);

    public static final class SomethingToPrimitive<M extends Map<?, ?>> extends FastutilMapSerializer<M> {

        private final Type primitiveType;

        public SomethingToPrimitive(final Function<Map, ? extends M> factory, final Type primitiveType) {
            super(factory);
            this.primitiveType = primitiveType;
        }

        @Override
        protected Type createBaseMapType(final ParameterizedType type) {
            return TypeFactory.parameterizedClass(Map.class, type.getActualTypeArguments()[0], GenericTypeReflector.box(this.primitiveType));
        }
    }

    public static final class PrimitiveToSomething<M extends Map<?, ?>> extends FastutilMapSerializer<M> {

        private final Type primitiveType;

        public PrimitiveToSomething(final Function<Map, ? extends M> factory, final Type primitiveType) {
            super(factory);
            this.primitiveType = primitiveType;
        }

        @Override
        protected Type createBaseMapType(final ParameterizedType type) {
            return TypeFactory.parameterizedClass(Map.class, GenericTypeReflector.box(this.primitiveType), type.getActualTypeArguments()[0]);
        }
    }

    public static final class SomethingToSomething<M extends Map<?, ?>> extends FastutilMapSerializer<M> {

        public SomethingToSomething(final Function<? super Map, ? extends M> factory) {
            super(factory);
        }

        @Override
        protected Type createBaseMapType(final ParameterizedType type) {
            return TypeFactory.parameterizedClass(Map.class, type.getActualTypeArguments()[0], type.getActualTypeArguments()[1]);
        }
    }
}
