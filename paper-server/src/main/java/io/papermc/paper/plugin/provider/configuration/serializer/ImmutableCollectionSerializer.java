package io.papermc.paper.plugin.provider.configuration.serializer;

import com.google.common.collect.ImmutableCollection;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.util.CheckedConsumer;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class ImmutableCollectionSerializer<B extends ImmutableCollection.Builder<?>, T extends Collection<?>> implements TypeSerializer<T> {

    protected ImmutableCollectionSerializer() {
    }

    @Override
    public final T deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final Type entryType = this.elementType(type);
        final @Nullable TypeSerializer<?> entrySerial = node.options().serializers().get(entryType);
        if (entrySerial == null) {
            throw new SerializationException(node, entryType, "No applicable type serializer for type");
        }

        if (node.isList()) {
            final List<? extends ConfigurationNode> values = node.childrenList();
            final B builder = this.createNew(values.size());
            for (ConfigurationNode value : values) {
                try {
                    this.deserializeSingle(builder, entrySerial.deserialize(entryType, value));
                } catch (final SerializationException ex) {
                    ex.initPath(value::path);
                    throw ex;
                }
            }
            return (T) builder.build();
        } else {
            final @Nullable Object unwrappedVal = node.raw();
            if (unwrappedVal != null) {
                final B builder = this.createNew(1);
                this.deserializeSingle(builder, entrySerial.deserialize(entryType, node));
                return (T) builder.build();
            }
        }
        return this.emptyValue(type, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public final void serialize(final Type type, final @Nullable T obj, final ConfigurationNode node) throws SerializationException {
        final Type entryType = this.elementType(type);
        final @Nullable TypeSerializer entrySerial = node.options().serializers().get(entryType);
        if (entrySerial == null) {
            throw new SerializationException(node, entryType, "No applicable type serializer for type");
        }

        node.raw(Collections.emptyList());
        if (obj != null) {
            this.forEachElement(obj, el -> {
                final ConfigurationNode child = node.appendListNode();
                try {
                    entrySerial.serialize(entryType, el, child);
                } catch (final SerializationException ex) {
                    ex.initPath(child::path);
                    throw ex;
                }
            });
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public @Nullable T emptyValue(final Type specificType, final ConfigurationOptions options) {
        return (T) this.createNew(0).build();
    }

    protected abstract Type elementType(Type containerType) throws SerializationException;

    protected abstract B createNew(int size);

    protected abstract void forEachElement(T collection, CheckedConsumer<Object, SerializationException> action) throws SerializationException;

    protected abstract void deserializeSingle(B builder, @Nullable Object deserialized) throws SerializationException;

}
