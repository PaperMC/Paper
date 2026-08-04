package io.papermc.paper.persistence;

import com.google.common.base.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataAdapterContext;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class PaperPersistentDataContainerView implements PersistentDataContainerView {

    protected final CraftPersistentDataTypeRegistry registry;
    protected final CraftPersistentDataAdapterContext adapterContext;

    public PaperPersistentDataContainerView(final CraftPersistentDataTypeRegistry registry) {
        this.registry = registry;
        this.adapterContext = new CraftPersistentDataAdapterContext(this.registry);
    }

    public abstract @Nullable Tag getTag(final String key);

    public abstract CompoundTag toTagCompound();

    @Override
    public <P, C> boolean has(final NamespacedKey key, final PersistentDataType<P, C> type) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");

        final Tag value = this.getTag(key.toString());
        if (value == null) {
            return false;
        }

        return this.registry.isInstanceOf(type, value);
    }

    @Override
    public boolean has(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "The provided key for the custom value was null"); // Paper
        return this.getTag(key.toString()) != null;
    }

    @Override
    public <P, C> @Nullable C get(final NamespacedKey key, final PersistentDataType<P, C> type) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");

        final Tag value = this.getTag(key.toString());
        if (value == null) {
            return null;
        }

        return type.fromPrimitive(this.registry.extract(type, value), this.adapterContext);
    }

    @Override
    public <P, C> C getOrDefault(final NamespacedKey key, final PersistentDataType<P, C> type, final C defaultValue) {
        final C c = this.get(key, type);
        return c != null ? c : defaultValue;
    }

    @Override
    public @Nullable PersistentDataType<?, ?> getType(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");

        final Tag value = this.getTag(key.toString());
        if (value == null) {
            return null;
        }

        return getTagType(value);
    }

    @Override
    public Set<NamespacedKey> getKeys() {
        final Set<String> names = this.toTagCompound().keySet();
        final Set<NamespacedKey> keys = new HashSet<>(names.size());
        names.forEach(key -> {
            final String[] keyPart = key.split(":", 2);
            if (keyPart.length == 2) {
                keys.add(new NamespacedKey(keyPart[0], keyPart[1]));
            }
        });
        return Collections.unmodifiableSet(keys);
    }

    @Override
    public boolean isEmpty() {
        return this.toTagCompound().isEmpty();
    }

    @Override
    public void copyTo(final PersistentDataContainer other, final boolean replace) {
        Preconditions.checkArgument(other != null, "The target container cannot be null");
        final CraftPersistentDataContainer target = (CraftPersistentDataContainer) other;
        final CompoundTag tag = this.toTagCompound();
        for (final String key : tag.keySet()) {
            if (replace || !target.getRaw().containsKey(key)) {
                target.getRaw().put(key, tag.get(key).copy());
            }
        }
    }

    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return this.adapterContext;
    }

    @Override
    public byte[] serializeToBytes() throws IOException {
        final CompoundTag root = this.toTagCompound();
        final ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        try (final DataOutputStream dataOutput = new DataOutputStream(byteArrayOutput)) {
            NbtIo.write(root, dataOutput);
            return byteArrayOutput.toByteArray();
        }
    }

    private static @Nullable PersistentDataType<?, ?> getTagType(final Tag value) {
        return switch (value.getId()) {
            case Tag.TAG_BYTE -> PersistentDataType.BYTE;
            case Tag.TAG_SHORT -> PersistentDataType.SHORT;
            case Tag.TAG_INT -> PersistentDataType.INTEGER;
            case Tag.TAG_LONG -> PersistentDataType.LONG;
            case Tag.TAG_FLOAT -> PersistentDataType.FLOAT;
            case Tag.TAG_DOUBLE -> PersistentDataType.DOUBLE;
            case Tag.TAG_BYTE_ARRAY -> PersistentDataType.BYTE_ARRAY;
            case Tag.TAG_STRING -> PersistentDataType.STRING;
            case Tag.TAG_COMPOUND -> PersistentDataType.TAG_CONTAINER;
            case Tag.TAG_INT_ARRAY -> PersistentDataType.INTEGER_ARRAY;
            case Tag.TAG_LONG_ARRAY -> PersistentDataType.LONG_ARRAY;
            case Tag.TAG_LIST -> {
                if (!(value instanceof ListTag listTag)) {
                    yield null;
                }

                yield getListTagType(listTag);
            }
            default -> null;
        };
    }

    private static @Nullable ListPersistentDataType<?, ?> getListTagType(final ListTag value) {
        return switch (value.identifyRawElementType()) {
            case Tag.TAG_BYTE -> PersistentDataType.LIST.bytes();
            case Tag.TAG_SHORT -> PersistentDataType.LIST.shorts();
            case Tag.TAG_INT -> PersistentDataType.LIST.integers();
            case Tag.TAG_LONG -> PersistentDataType.LIST.longs();
            case Tag.TAG_FLOAT -> PersistentDataType.LIST.floats();
            case Tag.TAG_DOUBLE -> PersistentDataType.LIST.doubles();
            case Tag.TAG_BYTE_ARRAY -> PersistentDataType.LIST.byteArrays();
            case Tag.TAG_STRING -> PersistentDataType.LIST.strings();
            case Tag.TAG_COMPOUND -> PersistentDataType.LIST.dataContainers();
            case Tag.TAG_INT_ARRAY -> PersistentDataType.LIST.integerArrays();
            case Tag.TAG_LONG_ARRAY -> PersistentDataType.LIST.longArrays();
            case Tag.TAG_LIST -> {
                if (value.isEmpty()) {
                    yield null;
                }

                final Tag nestedTag = value.getFirst();
                if (!(nestedTag instanceof ListTag nestedListTag)) {
                    yield null;
                }

                yield getListTagType(nestedListTag);
            }
            default -> null;
        };
    }
}
