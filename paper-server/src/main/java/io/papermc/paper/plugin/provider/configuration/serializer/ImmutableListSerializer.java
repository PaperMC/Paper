package io.papermc.paper.plugin.provider.configuration.serializer;

import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.util.CheckedConsumer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ImmutableListSerializer extends ImmutableCollectionSerializer<ImmutableList.Builder<?>, List<?>> {

    @Override
    protected Type elementType(Type containerType) throws SerializationException {
        if (!(containerType instanceof ParameterizedType)) {
            throw new SerializationException(containerType, "Raw types are not supported for collections");
        }
        return ((ParameterizedType) containerType).getActualTypeArguments()[0];
    }

    @Override
    protected ImmutableList.Builder<?> createNew(int size) {
        return ImmutableList.builderWithExpectedSize(size);
    }

    @Override
    protected void forEachElement(List<?> collection, CheckedConsumer<Object, SerializationException> action) throws SerializationException {
        for (Object obj : collection) {
            action.accept(obj);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void deserializeSingle(ImmutableList.Builder<?> builder, @Nullable Object deserialized) throws SerializationException {
        if (deserialized == null) {
            return;
        }

        ((ImmutableList.Builder) builder).add(deserialized);
    }
}
