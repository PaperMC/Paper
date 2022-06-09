package io.papermc.paper.configuration.serializer.collections;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import io.leangen.geantyref.TypeFactory;
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

public class TableSerializer implements TypeSerializer<Table<?, ?, ?>> {
    private static final int ROW_TYPE_ARGUMENT_INDEX = 0;
    private static final int COLUMN_TYPE_ARGUMENT_INDEX = 1;
    private static final int VALUE_TYPE_ARGUMENT_INDEX = 2;

    @Override
    public Table<?, ?, ?> deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        final Table<?, ?, ?> table = HashBasedTable.create();
        if (!node.empty() && node.isMap()) {
            this.deserialize0(table, (ParameterizedType) type, node);
        }
        return table;
    }

    @SuppressWarnings("unchecked")
    private <R, C, V> void deserialize0(final Table<R, C, V> table, final ParameterizedType type, final ConfigurationNode node) throws SerializationException {
        final Type rowType = type.getActualTypeArguments()[ROW_TYPE_ARGUMENT_INDEX];
        final Type columnType = type.getActualTypeArguments()[COLUMN_TYPE_ARGUMENT_INDEX];
        final Type valueType = type.getActualTypeArguments()[VALUE_TYPE_ARGUMENT_INDEX];

        final TypeSerializer<R> rowKeySerializer = (TypeSerializer<R>) node.options().serializers().get(rowType);
        if (rowKeySerializer == null) {
            throw new SerializationException("Could not find serializer for table row type " + rowType);
        }

        final Type mapType = TypeFactory.parameterizedClass(Map.class, columnType, valueType);
        final TypeSerializer<Map<C, V>> columnValueSerializer = (TypeSerializer<Map<C, V>>) node.options().serializers().get(mapType);
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
    public void serialize(final Type type, final @Nullable Table<?, ?, ?> table, final ConfigurationNode node) throws SerializationException {
        if (table != null) {
            this.serialize0(table, (ParameterizedType) type, node);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <R, C, V> void serialize0(final Table<R, C, V> table, final ParameterizedType type, final ConfigurationNode node) throws SerializationException {
        final Type rowType = type.getActualTypeArguments()[ROW_TYPE_ARGUMENT_INDEX];
        final Type columnType = type.getActualTypeArguments()[COLUMN_TYPE_ARGUMENT_INDEX];
        final Type valueType = type.getActualTypeArguments()[VALUE_TYPE_ARGUMENT_INDEX];

        final TypeSerializer rowKeySerializer = node.options().serializers().get(rowType);
        if (rowKeySerializer == null) {
            throw new SerializationException("Could not find a serializer for table row type " + rowType);
        }

        final BasicConfigurationNode rowKeyNode = BasicConfigurationNode.root(node.options());
        for (final R key : table.rowKeySet()) {
            rowKeySerializer.serialize(rowType, key, rowKeyNode.set(key));
            final Object keyObj = Objects.requireNonNull(rowKeyNode.raw());
            node.node(keyObj).set(TypeFactory.parameterizedClass(Map.class, columnType, valueType), table.row(key));
        }
    }

    @Override
    public @Nullable Table<?, ?, ?> emptyValue(Type specificType, ConfigurationOptions options) {
        return ImmutableTable.of();
    }
}
