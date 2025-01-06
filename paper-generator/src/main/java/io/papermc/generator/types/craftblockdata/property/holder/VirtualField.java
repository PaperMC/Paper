package io.papermc.generator.types.craftblockdata.property.holder;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record VirtualField(
    String name,
    Type valueType,
    DataHolderType holderType,
    String baseName,
    @Nullable Class<?> keyClass,
    Collection<? extends Property<?>> values
) {

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <T extends Property<? extends Comparable<?>>> VirtualField.FieldValue<T> createCollection(String name, TypeToken<T> valueType, boolean isArray, String baseName) {
        return new VirtualField.FieldValue<>(name, valueType, isArray ? DataHolderType.ARRAY : DataHolderType.LIST, baseName, null);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <T extends Property<? extends Comparable<?>>> VirtualField.FieldValue<T> createCollection(String name, Class<T> valueType, boolean isArray, String baseName) {
        return createCollection(name, TypeToken.of(valueType), isArray, baseName);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <T extends Property<? extends Comparable<?>>> VirtualField.FieldValue<T> createMap(String name, Class<?> keyClass, TypeToken<T> valueType, String baseName) {
        return new VirtualField.FieldValue<>(name, valueType, DataHolderType.MAP, baseName, keyClass);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <T extends Property<? extends Comparable<?>>> VirtualField.FieldValue<T> createMap(String name, Class<?> keyClass, Class<T> valueType, String baseName) {
        return createMap(name, keyClass, TypeToken.of(valueType), baseName);
    }

    public static class FieldValue<T extends Property<? extends Comparable<?>>> {

        private final String name;
        private final DataHolderType holderType;
        private final TypeToken<T> valueTypeToken;
        private final String baseName;
        private final @Nullable Class<?> keyClass;

        private @Nullable Collection<T> values;

        public FieldValue(String name, TypeToken<T> valueTypeToken, DataHolderType holderType, String baseName, @Nullable Class<?> keyClass) {
            this.name = name;
            this.valueTypeToken = valueTypeToken;
            this.holderType = holderType;
            this.baseName = baseName;
            this.keyClass = keyClass;
        }

        @Contract(value = "_ -> this", mutates = "this")
        public FieldValue<T> withValues(Collection<T> properties) {
            this.values = List.copyOf(properties);
            return this;
        }

        public VirtualField make() {
            Preconditions.checkState(this.values != null && !this.values.isEmpty(), "The field should doesn't have any content");
            return new VirtualField(this.name, this.valueTypeToken.getType(), this.holderType, this.baseName, this.keyClass, this.values);
        }
    }
}
