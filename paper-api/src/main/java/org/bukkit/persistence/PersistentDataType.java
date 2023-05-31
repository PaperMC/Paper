package org.bukkit.persistence;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents an enum with a generic content type. It defines the
 * types a custom tag can have.
 * <p>
 * This interface can be used to create your own custom
 * {@link PersistentDataType} with different complex types. This may be useful
 * for the likes of a UUIDTagType:
 * <pre>
 * {@code
 * public class UUIDTagType implements PersistentDataType<byte[], UUID> {
 *
 *         {@literal @Override}
 *         public Class<byte[]> getPrimitiveType() {
 *             return byte[].class;
 *         }
 *
 *         {@literal @Override}
 *         public Class<UUID> getComplexType() {
 *             return UUID.class;
 *         }
 *
 *         {@literal @Override}
 *         public byte[] toPrimitive(UUID complex, PersistentDataAdapterContext context) {
 *             ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
 *             bb.putLong(complex.getMostSignificantBits());
 *             bb.putLong(complex.getLeastSignificantBits());
 *             return bb.array();
 *         }
 *
 *         {@literal @Override}
 *         public UUID fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
 *             ByteBuffer bb = ByteBuffer.wrap(primitive);
 *             long firstLong = bb.getLong();
 *             long secondLong = bb.getLong();
 *             return new UUID(firstLong, secondLong);
 *         }
 *     }}</pre>
 *
 * @param <T> the primary object type that is stored in the given tag
 * @param <Z> the retrieved object type when applying this tag type
 */
public interface PersistentDataType<T, Z> {

    /*
        The primitive one value types.
     */
    PersistentDataType<Byte, Byte> BYTE = new PrimitivePersistentDataType<>(Byte.class);
    PersistentDataType<Short, Short> SHORT = new PrimitivePersistentDataType<>(Short.class);
    PersistentDataType<Integer, Integer> INTEGER = new PrimitivePersistentDataType<>(Integer.class);
    PersistentDataType<Long, Long> LONG = new PrimitivePersistentDataType<>(Long.class);
    PersistentDataType<Float, Float> FLOAT = new PrimitivePersistentDataType<>(Float.class);
    PersistentDataType<Double, Double> DOUBLE = new PrimitivePersistentDataType<>(Double.class);

    /*
        Boolean.
     */
    /**
     * A convenience implementation to convert between Byte and Boolean as there is
     * no native implementation for booleans. <br>
     * Any byte value not equal to 0 is considered to be true.
     */
    PersistentDataType<Byte, Boolean> BOOLEAN = new BooleanPersistentDataType();

    /*
        String.
     */
    PersistentDataType<String, String> STRING = new PrimitivePersistentDataType<>(String.class);

    /*
        Primitive Arrays.
     */
    PersistentDataType<byte[], byte[]> BYTE_ARRAY = new PrimitivePersistentDataType<>(byte[].class);
    PersistentDataType<int[], int[]> INTEGER_ARRAY = new PrimitivePersistentDataType<>(int[].class);
    PersistentDataType<long[], long[]> LONG_ARRAY = new PrimitivePersistentDataType<>(long[].class);

    /*
        Complex Arrays.
     */
    PersistentDataType<PersistentDataContainer[], PersistentDataContainer[]> TAG_CONTAINER_ARRAY = new PrimitivePersistentDataType<>(PersistentDataContainer[].class);

    /*
        Nested PersistentDataContainer.
     */
    PersistentDataType<PersistentDataContainer, PersistentDataContainer> TAG_CONTAINER = new PrimitivePersistentDataType<>(PersistentDataContainer.class);

    /**
     * Returns the primitive data type of this tag.
     *
     * @return the class
     */
    @NotNull
    Class<T> getPrimitiveType();

    /**
     * Returns the complex object type the primitive value resembles.
     *
     * @return the class type
     */
    @NotNull
    Class<Z> getComplexType();

    /**
     * Returns the primitive data that resembles the complex object passed to
     * this method.
     *
     * @param complex the complex object instance
     * @param context the context this operation is running in
     * @return the primitive value
     */
    @NotNull
    T toPrimitive(@NotNull Z complex, @NotNull PersistentDataAdapterContext context);

    /**
     * Creates a complex object based of the passed primitive value
     *
     * @param primitive the primitive value
     * @param context the context this operation is running in
     * @return the complex object instance
     */
    @NotNull
    Z fromPrimitive(@NotNull T primitive, @NotNull PersistentDataAdapterContext context);

    /**
     * A default implementation that simply exists to pass on the retrieved or
     * inserted value to the next layer.
     * <p>
     * This implementation does not add any kind of logic, but is used to
     * provide default implementations for the primitive types.
     *
     * @param <T> the generic type of the primitive objects
     */
    class PrimitivePersistentDataType<T> implements PersistentDataType<T, T> {

        private final Class<T> primitiveType;

        PrimitivePersistentDataType(@NotNull Class<T> primitiveType) {
            this.primitiveType = primitiveType;
        }

        @NotNull
        @Override
        public Class<T> getPrimitiveType() {
            return primitiveType;
        }

        @NotNull
        @Override
        public Class<T> getComplexType() {
            return primitiveType;
        }

        @NotNull
        @Override
        public T toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
            return complex;
        }

        @NotNull
        @Override
        public T fromPrimitive(@NotNull T primitive, @NotNull PersistentDataAdapterContext context) {
            return primitive;
        }
    }

    /**
     * A convenience implementation to convert between Byte and Boolean as there is
     * no native implementation for booleans. <br>
     * Any byte value not equal to 0 is considered to be true.
     */
    class BooleanPersistentDataType implements PersistentDataType<Byte, Boolean> {

        @NotNull
        @Override
        public Class<Byte> getPrimitiveType() {
            return Byte.class;
        }

        @NotNull
        @Override
        public Class<Boolean> getComplexType() {
            return Boolean.class;
        }

        @NotNull
        @Override
        public Byte toPrimitive(@NotNull Boolean complex, @NotNull PersistentDataAdapterContext context) {
            return (byte) (complex ? 1 : 0);
        }

        @NotNull
        @Override
        public Boolean fromPrimitive(@NotNull Byte primitive, @NotNull PersistentDataAdapterContext context) {
            return primitive != 0;
        }
    }
}
