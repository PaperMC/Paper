package io.papermc.paper.configuration.serializer.collection;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import io.leangen.geantyref.TypeFactory;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class TableSerializer implements TypeSerializer.Annotated<Table<?, ?, ?>> {
    private static final int ROW_TYPE_ARGUMENT_INDEX = 0;
    private static final int COLUMN_TYPE_ARGUMENT_INDEX = 1;
    private static final int VALUE_TYPE_ARGUMENT_INDEX = 2;

    @Override
    public Table<?, ?, ?> deserialize(final AnnotatedType type, final ConfigurationNode node) throws SerializationException {
        final Table<?, ?, ?> table = HashBasedTable.create();
        if (!node.empty() && node.isMap()) {
            this.deserialize0(table, (AnnotatedParameterizedType) type, node);
        }
        return table;
    }

    @SuppressWarnings("unchecked")
    private <R, C, V> void deserialize0(final Table<R, C, V> table, final AnnotatedParameterizedType type, final ConfigurationNode node) throws SerializationException {
        final AnnotatedType rowType = type.getAnnotatedActualTypeArguments()[ROW_TYPE_ARGUMENT_INDEX];
        final AnnotatedType columnType = type.getAnnotatedActualTypeArguments()[COLUMN_TYPE_ARGUMENT_INDEX];
        final AnnotatedType valueType = type.getAnnotatedActualTypeArguments()[VALUE_TYPE_ARGUMENT_INDEX];

        final TypeSerializer<R> rowKeySerializer = (TypeSerializer<R>) node.options().serializers().get(rowType);
        if (rowKeySerializer == null) {
            throw new SerializationException("Could not find serializer for table row type " + rowType);
        }

        final ParameterizedType mapType = (ParameterizedType) TypeFactory.parameterizedClass(Map.class, columnType.getType(), valueType.getType());
        final AnnotatedParameterizedType annotatedMapType = TypeFactory.parameterizedAnnotatedType(mapType, type.getAnnotations(), columnType.getAnnotations(), valueType.getAnnotations());
        final TypeSerializer<Map<C, V>> columnValueSerializer = (TypeSerializer<Map<C, V>>) node.options().serializers().get(annotatedMapType);
        if (columnValueSerializer == null) {
            throw new SerializationException("Could not find serializer for table column-value map " + type);
        }

        final BasicConfigurationNode rowKeyNode = BasicConfigurationNode.root(node.options());

        for (final Object key : node.childrenMap().keySet()) {
            final R rowKey = rowKeySerializer.deserialize(rowType, rowKeyNode.set(key));
            final Map<C, V> map = columnValueSerializer.deserialize(mapType, node.node(rowKeyNode.raw()));
            map.forEach((column, value) -> table.put(rowKey, column, value));
        }
    }

    @Override
    public void serialize(
        final AnnotatedType type,
        final @Nullable Table<?, ?, ?> obj,
        final ConfigurationNode node
    ) throws SerializationException {
        if (obj != null) {
            this.serialize0(obj, (AnnotatedParameterizedType) type, node);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <R, C, V> void serialize0(final Table<R, C, V> table, final AnnotatedParameterizedType type, final ConfigurationNode node) throws SerializationException {
        final AnnotatedType rowType = type.getAnnotatedActualTypeArguments()[ROW_TYPE_ARGUMENT_INDEX];
        final AnnotatedType columnType = type.getAnnotatedActualTypeArguments()[COLUMN_TYPE_ARGUMENT_INDEX];
        final AnnotatedType valueType = type.getAnnotatedActualTypeArguments()[VALUE_TYPE_ARGUMENT_INDEX];

        final TypeSerializer rowKeySerializer = node.options().serializers().get(rowType);
        if (rowKeySerializer == null) {
            throw new SerializationException("Could not find a serializer for table row type " + rowType);
        }

        final BasicConfigurationNode rowKeyNode = BasicConfigurationNode.root(node.options());
        for (final R key : table.rowKeySet()) {
            rowKeySerializer.serialize(rowType, key, rowKeyNode.set(key));
            final Object keyObj = Objects.requireNonNull(rowKeyNode.raw());
            final ParameterizedType mapType = (ParameterizedType) TypeFactory.parameterizedClass(Map.class, columnType.getType(), valueType.getType());
            final AnnotatedParameterizedType annotatedMapType = TypeFactory.parameterizedAnnotatedType(mapType, type.getAnnotations(), columnType.getAnnotations(), valueType.getAnnotations());
            node.node(keyObj).set(annotatedMapType, table.row(key));
        }
    }

    @Override
    public @Nullable Table<?, ?, ?> emptyValue(final Type specificType, final ConfigurationOptions options) {
        return ImmutableTable.of();
    }
}
