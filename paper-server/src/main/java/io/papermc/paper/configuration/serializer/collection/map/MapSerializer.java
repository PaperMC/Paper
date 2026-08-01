package io.papermc.paper.configuration.serializer.collection.map;

import com.mojang.logging.LogUtils;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import static java.util.Objects.requireNonNull;

/**
 * Map serializer that does not throw errors on individual entry serialization failures.
 */
public class MapSerializer implements TypeSerializer.Annotated<Map<?, ?>> {

    public static final TypeToken<Map<?, ?>> TYPE = new TypeToken<Map<?, ?>>() {};

    private static final Logger LOGGER = LogUtils.getClassLogger();

    private final boolean clearInvalids;
    private final TypeSerializer<Map<?, ?>> fallback;

    public MapSerializer(final boolean clearInvalids) {
        this.clearInvalids = clearInvalids;
        this.fallback = requireNonNull(TypeSerializerCollection.defaults().get(TYPE), "Could not find default Map<?, ?> serializer");
    }

    @Override
    public Map<?, ?> deserialize(final AnnotatedType annotatedType, final ConfigurationNode node) throws SerializationException {
        if (annotatedType.isAnnotationPresent(ThrowExceptions.class)) {
            return this.fallback.deserialize(annotatedType, node);
        }
        final Map<Object, Object> map = new LinkedHashMap<>();
        final Type type = annotatedType.getType();
        if (node.isMap()) {
            if (!(annotatedType instanceof final AnnotatedParameterizedType annotatedParameterizedType)) {
                throw new SerializationException(type, "Raw types are not supported for collections");
            }
            if (annotatedParameterizedType.getAnnotatedActualTypeArguments().length != 2) {
                throw new SerializationException(type, "Map expected two type arguments!");
            }
            final AnnotatedType key = annotatedParameterizedType.getAnnotatedActualTypeArguments()[0];
            final AnnotatedType value = annotatedParameterizedType.getAnnotatedActualTypeArguments()[1];
            final TypeSerializer<?> keySerializer = node.options().serializers().get(key);
            final TypeSerializer<?> valueSerializer = node.options().serializers().get(value);
            if (keySerializer == null) {
                throw new SerializationException(type, "No type serializer available for key type " + key);
            }
            if (valueSerializer == null) {
                throw new SerializationException(type, "No type serializer available for value type " + value);
            }
            final boolean writeKeyBack = key.isAnnotationPresent(WriteKeyBack.class);

            final BasicConfigurationNode keyNode = BasicConfigurationNode.root(node.options());
            final Set<Object> keysToClear = new HashSet<>();
            for (final Map.Entry<Object, ? extends ConfigurationNode> ent : node.childrenMap().entrySet()) {
                final Object deserializedKey = this.deserialize(key.getType(), keySerializer, "key", keyNode.set(ent.getKey()), node.path());
                final Object deserializedValue = this.deserialize(value.getType(), valueSerializer, "value", ent.getValue(), ent.getValue().path());
                if (deserializedKey == null || deserializedValue == null) {
                    continue;
                }
                if (writeKeyBack) {
                    if (this.serialize(key.getType(), keySerializer, deserializedKey, "key", keyNode, node.path()) && !ent.getKey().equals(requireNonNull(keyNode.raw(), "Key must not be null!"))) {
                        keysToClear.add(ent.getKey());
                    }
                }
                map.put(deserializedKey, deserializedValue);
            }
            if (writeKeyBack) { // supports cleaning keys which deserialize to the same value
                for (final Object keyToClear : keysToClear) {
                    node.node(keyToClear).raw(null);
                }
            }
        }
        return map;
    }

    private @Nullable Object deserialize(final Type type, final TypeSerializer<?> serializer, final String mapPart, final ConfigurationNode node, final NodePath path) {
        try {
            return serializer.deserialize(type, node);
        } catch (final SerializationException ex) {
            ex.initPath(node::path);
            LOGGER.error("Could not deserialize {} {} into {} at {}: {}", mapPart, node.raw(), type, path, ex.rawMessage());
        }
        return null;
    }

    @Override
    public void serialize(final AnnotatedType annotatedType, final @Nullable Map<?, ?> obj, final ConfigurationNode node) throws SerializationException {
        if (annotatedType.isAnnotationPresent(ThrowExceptions.class)) {
            this.fallback.serialize(annotatedType, obj, node);
            return;
        }
        final Type type = annotatedType.getType();
        if (!(annotatedType instanceof final AnnotatedParameterizedType annotatedParameterizedType)) {
            throw new SerializationException(type, "Raw types are not supported for collections");
        }
        if (annotatedParameterizedType.getAnnotatedActualTypeArguments().length != 2) {
            throw new SerializationException(type, "Map expected two type arguments!");
        }
        final AnnotatedType key = annotatedParameterizedType.getAnnotatedActualTypeArguments()[0];
        final AnnotatedType value = annotatedParameterizedType.getAnnotatedActualTypeArguments()[1];
        final TypeSerializer<?> keySerializer = node.options().serializers().get(key);
        final TypeSerializer<?> valueSerializer = node.options().serializers().get(value);

        if (keySerializer == null) {
            throw new SerializationException(type, "No type serializer available for key type " + key);
        }

        if (valueSerializer == null) {
            throw new SerializationException(type, "No type serializer available for value type " + value);
        }

        if (obj == null || obj.isEmpty()) {
            node.set(Collections.emptyMap());
        } else {
            final Set<Object> unvisitedKeys;
            if (node.empty()) {
                node.raw(Collections.emptyMap());
                unvisitedKeys = new HashSet<>();
            } else {
                unvisitedKeys = new HashSet<>(node.childrenMap().keySet());
            }
            final BasicConfigurationNode keyNode = BasicConfigurationNode.root(node.options());
            for (final Map.Entry<?, ?> ent : obj.entrySet()) {
                if (!this.serialize(key.getType(), keySerializer, ent.getKey(), "key", keyNode, node.path())) {
                    continue;
                }
                final Object keyObj = requireNonNull(keyNode.raw(), "Key must not be null!");
                final ConfigurationNode child = node.node(keyObj);
                this.serialize(value.getType(), valueSerializer, ent.getValue(), "value", child, child.path());
                unvisitedKeys.remove(keyObj);
            }
            if (this.clearInvalids) {
                for (final Object unusedChild : unvisitedKeys) {
                    node.removeChild(unusedChild);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean serialize(final Type type, final TypeSerializer serializer, final Object object, final String mapPart, final ConfigurationNode node, final NodePath path) {
        try {
            serializer.serialize(type, object, node);
            return true;
        } catch (final SerializationException ex) {
            ex.initPath(node::path);
            LOGGER.error("Could not serialize {} {} from {} at {}: {}", mapPart, object, type, path, ex.rawMessage());
        }
        return false;
    }

    @Override
    public @Nullable Map<?, ?> emptyValue(final AnnotatedType specificType, final ConfigurationOptions options) {
        if (specificType.isAnnotationPresent(ThrowExceptions.class)) {
            return this.fallback.emptyValue(specificType, options);
        }
        return new LinkedHashMap<>();
    }

}
