package org.bukkit.persistence;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A provider for list persistent data types that represent the known primitive
 * types exposed by {@link PersistentDataType}.
 */
public final class ListPersistentDataTypeProvider {

    private static final ListPersistentDataType<Byte, Byte> BYTE = new ListPersistentDataTypeImpl<>(PersistentDataType.BYTE);
    private static final ListPersistentDataType<Short, Short> SHORT = new ListPersistentDataTypeImpl<>(PersistentDataType.SHORT);
    private static final ListPersistentDataType<Integer, Integer> INTEGER = new ListPersistentDataTypeImpl<>(PersistentDataType.INTEGER);
    private static final ListPersistentDataType<Long, Long> LONG = new ListPersistentDataTypeImpl<>(PersistentDataType.LONG);
    private static final ListPersistentDataType<Float, Float> FLOAT = new ListPersistentDataTypeImpl<>(PersistentDataType.FLOAT);
    private static final ListPersistentDataType<Double, Double> DOUBLE = new ListPersistentDataTypeImpl<>(PersistentDataType.DOUBLE);
    private static final ListPersistentDataType<Byte, Boolean> BOOLEAN = new ListPersistentDataTypeImpl<>(PersistentDataType.BOOLEAN);
    private static final ListPersistentDataType<String, String> STRING = new ListPersistentDataTypeImpl<>(PersistentDataType.STRING);
    private static final ListPersistentDataType<byte[], byte[]> BYTE_ARRAY = new ListPersistentDataTypeImpl<>(PersistentDataType.BYTE_ARRAY);
    private static final ListPersistentDataType<int[], int[]> INTEGER_ARRAY = new ListPersistentDataTypeImpl<>(PersistentDataType.INTEGER_ARRAY);
    private static final ListPersistentDataType<long[], long[]> LONG_ARRAY = new ListPersistentDataTypeImpl<>(PersistentDataType.LONG_ARRAY);
    private static final ListPersistentDataType<PersistentDataContainer, PersistentDataContainer> DATA_CONTAINER = new ListPersistentDataTypeImpl<>(
            PersistentDataType.TAG_CONTAINER
    );

    ListPersistentDataTypeProvider() {
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of bytes.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<Byte, Byte> bytes() {
        return BYTE;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of shorts.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<Short, Short> shorts() {
        return SHORT;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of integers.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<Integer, Integer> integers() {
        return INTEGER;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of longs.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<Long, Long> longs() {
        return LONG;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of floats.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<Float, Float> floats() {
        return FLOAT;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of doubles.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<Double, Double> doubles() {
        return DOUBLE;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of booleans.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<Byte, Boolean> booleans() {
        return BOOLEAN;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of strings.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<String, String> strings() {
        return STRING;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of byte arrays.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<byte[], byte[]> byteArrays() {
        return BYTE_ARRAY;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of int arrays.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<int[], int[]> integerArrays() {
        return INTEGER_ARRAY;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * storing lists of long arrays.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<long[], long[]> longArrays() {
        return LONG_ARRAY;
    }

    /**
     * Provides a shared {@link ListPersistentDataType} that is capable of
     * persistent data containers.
     *
     * @return the persistent data type.
     */
    @NotNull
    public ListPersistentDataType<PersistentDataContainer, PersistentDataContainer> dataContainers() {
        return DATA_CONTAINER;
    }

    /**
     * Constructs a new list persistent data type given any persistent data type
     * for its elements.
     *
     * @param elementType the persistent data type that is capable of
     * writing/reading the elements of the list.
     * @param <P> the generic type of the primitives stored in the list.
     * @param <C> the generic type of the complex values yielded back by the
     * persistent data types.
     * @return the created list persistent data type.
     */
    @NotNull
    public <P, C> ListPersistentDataType<P, C> listTypeFrom(@NotNull final PersistentDataType<P, C> elementType) {
        return new ListPersistentDataTypeImpl<>(elementType);
    }

    /**
     * A private implementation of the {@link ListPersistentDataType} that uses
     * {@link Collections2} for conversion from/to the primitive list.
     *
     * @param <P> the generic type of the primitives stored in the list.
     * @param <C> the generic type of the complex values yielded back by the
     * persistent data types.
     */
    private static final class ListPersistentDataTypeImpl<P, C> implements ListPersistentDataType<P, C> {

        @NotNull
        private final PersistentDataType<P, C> innerType;

        private ListPersistentDataTypeImpl(@NotNull final PersistentDataType<P, C> innerType) {
            this.innerType = innerType;
        }

        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public Class<List<P>> getPrimitiveType() {
            return (Class<List<P>>) (Object) List.class;
        }

        @NotNull
        @Override
        @SuppressWarnings("unchecked")
        public Class<List<C>> getComplexType() {
            return (Class<List<C>>) (Object) List.class;
        }

        @NotNull
        @Override
        public List<P> toPrimitive(@NotNull final List<C> complex, @NotNull final PersistentDataAdapterContext context) {
            return Lists.transform(complex, s -> innerType.toPrimitive(s, context));
        }

        @NotNull
        @Override
        public List<C> fromPrimitive(@NotNull final List<P> primitive, @NotNull final PersistentDataAdapterContext context) {
            return Lists.transform(primitive, s -> innerType.fromPrimitive(s, context));
        }

        @NotNull
        @Override
        public PersistentDataType<P, C> elementType() {
            return this.innerType;
        }
    }
}
