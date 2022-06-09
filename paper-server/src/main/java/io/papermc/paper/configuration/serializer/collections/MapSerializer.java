package io.papermc.paper.configuration.serializer.collections;

import com.mojang.logging.LogUtils;
import io.leangen.geantyref.TypeToken;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
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

    public MapSerializer(boolean clearInvalids) {
        this.clearInvalids = clearInvalids;
        this.fallback = requireNonNull(TypeSerializerCollection.defaults().get(TYPE), "Could not find default Map<?, ?> serializer");
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ThrowExceptions {}

    @Override
    public Map<?, ?> deserialize(AnnotatedType annotatedType, ConfigurationNode node) throws SerializationException {
        if (annotatedType.isAnnotationPresent(ThrowExceptions.class)) {
            return this.fallback.deserialize(annotatedType, node);
        }
        final Map<Object, Object> map = new LinkedHashMap<>();
        final Type type = annotatedType.getType();
        if (node.isMap()) {
            if (!(type instanceof ParameterizedType parameterizedType)) {
                throw new SerializationException(type, "Raw types are not supported for collections");
            }
            if (parameterizedType.getActualTypeArguments().length != 2) {
                throw new SerializationException(type, "Map expected two type arguments!");
            }
            final Type key = parameterizedType.getActualTypeArguments()[0];
            final Type value = parameterizedType.getActualTypeArguments()[1];
            final @Nullable TypeSerializer<?> keySerializer = node.options().serializers().get(key);
            final @Nullable TypeSerializer<?> valueSerializer = node.options().serializers().get(value);
            if (keySerializer == null) {
                throw new SerializationException(type, "No type serializer available for key type " + key);
            }
            if (valueSerializer == null) {
                throw new SerializationException(type, "No type serializer available for value type " + value);
            }

            final BasicConfigurationNode keyNode = BasicConfigurationNode.root(node.options());
            final Set<Object> keysToClear = new HashSet<>();
            for (Map.Entry<Object, ? extends ConfigurationNode> ent : node.childrenMap().entrySet()) {
                final @Nullable Object deserializedKey = deserialize(key, keySerializer, "key", keyNode.set(ent.getKey()), node.path());
                final @Nullable Object deserializedValue = deserialize(value, valueSerializer, "value", ent.getValue(), ent.getValue().path());
                if (deserializedKey == null || deserializedValue == null) {
                    continue;
                }
                if (keySerializer instanceof WriteBack) {
                    if (serialize(key, keySerializer, deserializedKey, "key", keyNode, node.path()) && !ent.getKey().equals(requireNonNull(keyNode.raw(), "Key must not be null!"))) {
                        keysToClear.add(ent.getKey());
                    }
                }
                map.put(deserializedKey, deserializedValue);
            }
            if (keySerializer instanceof WriteBack) { // supports cleaning keys which deserialize to the same value
                for (Object keyToClear : keysToClear) {
                    node.node(keyToClear).raw(null);
                }
            }
        }
        return map;
    }

    private @Nullable Object deserialize(Type type, TypeSerializer<?> serializer, String mapPart, ConfigurationNode node, NodePath path) {
        try {
            return serializer.deserialize(type, node);
        } catch (SerializationException ex) {
            ex.initPath(node::path);
            LOGGER.error("Could not deserialize {} {} into {} at {}: {}", mapPart, node.raw(), type, path, ex.rawMessage());
        }
        return null;
    }

    @Override
    public void serialize(AnnotatedType annotatedType, @Nullable Map<?, ?> obj, ConfigurationNode node) throws SerializationException {
        if (annotatedType.isAnnotationPresent(ThrowExceptions.class)) {
            this.fallback.serialize(annotatedType, obj, node);
            return;
        }
        final Type type = annotatedType.getType();
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new SerializationException(type, "Raw types are not supported for collections");
        }
        if (parameterizedType.getActualTypeArguments().length != 2) {
            throw new SerializationException(type, "Map expected two type arguments!");
        }
        final Type key = parameterizedType.getActualTypeArguments()[0];
        final Type value = parameterizedType.getActualTypeArguments()[1];
        final @Nullable TypeSerializer<?> keySerializer = node.options().serializers().get(key);
        final @Nullable TypeSerializer<?> valueSerializer = node.options().serializers().get(value);

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
                unvisitedKeys = Collections.emptySet();
            } else {
                unvisitedKeys = new HashSet<>(node.childrenMap().keySet());
            }
            final BasicConfigurationNode keyNode = BasicConfigurationNode.root(node.options());
            for (Map.Entry<?, ?> ent : obj.entrySet()) {
                if (!serialize(key, keySerializer, ent.getKey(), "key", keyNode, node.path())) {
                    continue;
                }
                final Object keyObj = requireNonNull(keyNode.raw(), "Key must not be null!");
                final ConfigurationNode child = node.node(keyObj);
                serialize(value, valueSerializer, ent.getValue(), "value", child, child.path());
                unvisitedKeys.remove(keyObj);
            }
            if (this.clearInvalids) {
                for (Object unusedChild : unvisitedKeys) {
                    node.removeChild(unusedChild);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean serialize(Type type, TypeSerializer serializer, Object object, String mapPart, ConfigurationNode node, NodePath path) {
        try {
            serializer.serialize(type, object, node);
            return true;
        } catch (SerializationException ex) {
            ex.initPath(node::path);
            LOGGER.error("Could not serialize {} {} from {} at {}: {}", mapPart, object, type, path, ex.rawMessage());
        }
        return false;
    }

    @Override
    public @Nullable Map<?, ?> emptyValue(AnnotatedType specificType, ConfigurationOptions options) {
        if (specificType.isAnnotationPresent(ThrowExceptions.class)) {
            return this.fallback.emptyValue(specificType, options);
        }
        return new LinkedHashMap<>();
    }

    public interface WriteBack { // marker interface
    }
}
