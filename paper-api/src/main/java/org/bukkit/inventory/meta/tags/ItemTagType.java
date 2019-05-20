package org.bukkit.inventory.meta.tags;

import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an enum with a generic content type. It defines the
 * types a custom item tag can have.
 * <p>
 * This interface can be used to create your own custom {@link ItemTagType} with
 * different complex types. This may be useful for the likes of a
 * UUIDItemTagType:
 * <pre>
 * {@code
 * public class UUIDItemTagType implements ItemTagType<byte[], UUID> {
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
 *         public byte[] toPrimitive(UUID complex, ItemTagAdapterContext context) {
 *             ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
 *             bb.putLong(complex.getMostSignificantBits());
 *             bb.putLong(complex.getLeastSignificantBits());
 *             return bb.array();
 *         }
 *
 *         {@literal @Override}
 *         public UUID fromPrimitive(byte[] primitive, ItemTagAdapterContext context) {
 *             ByteBuffer bb = ByteBuffer.wrap(primitive);
 *             long firstLong = bb.getLong();
 *             long secondLong = bb.getLong();
 *             return new UUID(firstLong, secondLong);
 *         }
 *     }}</pre>
 *
 * @param <T> the primary object type that is stored in the given tag
 * @param <Z> the retrieved object type when applying this item tag type
 *
 * @deprecated please use {@link PersistentDataType} as this part of the api is being replaced
 */
@Deprecated
public interface ItemTagType<T, Z> {

    /*
        The primitive one value types.
     */
    ItemTagType<Byte, Byte> BYTE = new PrimitiveTagType<>(Byte.class);
    ItemTagType<Short, Short> SHORT = new PrimitiveTagType<>(Short.class);
    ItemTagType<Integer, Integer> INTEGER = new PrimitiveTagType<>(Integer.class);
    ItemTagType<Long, Long> LONG = new PrimitiveTagType<>(Long.class);
    ItemTagType<Float, Float> FLOAT = new PrimitiveTagType<>(Float.class);
    ItemTagType<Double, Double> DOUBLE = new PrimitiveTagType<>(Double.class);

    /*
        String.
     */
    ItemTagType<String, String> STRING = new PrimitiveTagType<>(String.class);

    /*
        Primitive Arrays.
     */
    ItemTagType<byte[], byte[]> BYTE_ARRAY = new PrimitiveTagType<>(byte[].class);
    ItemTagType<int[], int[]> INTEGER_ARRAY = new PrimitiveTagType<>(int[].class);
    ItemTagType<long[], long[]> LONG_ARRAY = new PrimitiveTagType<>(long[].class);

    /*
        Nested TagContainer.
     */
    ItemTagType<CustomItemTagContainer, CustomItemTagContainer> TAG_CONTAINER = new PrimitiveTagType<>(CustomItemTagContainer.class);

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
    T toPrimitive(@NotNull Z complex, @NotNull ItemTagAdapterContext context);

    /**
     * Creates a complex object based of the passed primitive value
     *
     * @param primitive the primitive value
     * @param context the context this operation is running in
     * @return the complex object instance
     */
    @NotNull
    Z fromPrimitive(@NotNull T primitive, @NotNull ItemTagAdapterContext context);

    /**
     * A default implementation that simply exists to pass on the retrieved or
     * inserted value to the next layer.
     *
     * This implementation does not add any kind of logic, but is used to
     * provide default implementations for the primitive types.
     *
     * @param <T> the generic type of the primitive objects
     */
    class PrimitiveTagType<T> implements ItemTagType<T, T> {

        private final Class<T> primitiveType;

        PrimitiveTagType(@NotNull Class<T> primitiveType) {
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
        public T toPrimitive(@NotNull T complex, @NotNull ItemTagAdapterContext context) {
            return complex;
        }

        @NotNull
        @Override
        public T fromPrimitive(@NotNull T primitive, @NotNull ItemTagAdapterContext context) {
            return primitive;
        }
    }
}
