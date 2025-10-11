package org.bukkit.craftbukkit.persistence;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNBTTagConfigSerializer;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CraftPersistentDataContainer extends io.papermc.paper.persistence.PaperPersistentDataContainerView implements PersistentDataContainer { // Paper - split up view and mutable

    private final Map<String, Tag> customDataTags = new HashMap<>();

    public CraftPersistentDataContainer(Map<String, Tag> customTags, CraftPersistentDataTypeRegistry registry) {
        this(registry);
        this.customDataTags.putAll(customTags);
    }

    public CraftPersistentDataContainer(CraftPersistentDataTypeRegistry registry) {
        super(registry);
    }

    @Override
    public Tag getTag(final String key) {
        return this.customDataTags.get(key);
    }

    @Override
    public <T, Z> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");
        Preconditions.checkArgument(value != null, "The provided value cannot be null");

        this.customDataTags.put(key.toString(), this.registry.wrap(type, type.toPrimitive(value, this.adapterContext)));
    }

    @NotNull
    @Override
    public Set<NamespacedKey> getKeys() {
        Set<NamespacedKey> keys = new HashSet<>();

        this.customDataTags.keySet().forEach(key -> {
            String[] keyData = key.split(":", 2);
            if (keyData.length == 2) {
                keys.add(new NamespacedKey(keyData[0], keyData[1]));
            }
        });

        return keys;
    }

    @Override
    public void remove(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");

        this.customDataTags.remove(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return this.customDataTags.isEmpty();
    }

    @NotNull
    @Override
    public void copyTo(PersistentDataContainer other, boolean replace) {
        Preconditions.checkArgument(other != null, "The target container cannot be null");

        CraftPersistentDataContainer target = (CraftPersistentDataContainer) other;
        if (replace) {
            target.customDataTags.putAll(this.customDataTags);
        } else {
            this.customDataTags.forEach(target.customDataTags::putIfAbsent);
        }
    }

    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return this.adapterContext;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CraftPersistentDataContainer)) {
            return false;
        }

        Map<String, Tag> myRawMap = this.getRaw();
        Map<String, Tag> theirRawMap = ((CraftPersistentDataContainer) obj).getRaw();

        return Objects.equals(myRawMap, theirRawMap);
    }

    public CompoundTag toTagCompound() {
        CompoundTag tag = new CompoundTag();
        for (Entry<String, Tag> entry : this.customDataTags.entrySet()) {
            tag.put(entry.getKey(), entry.getValue());
        }
        return tag;
    }

    public void put(String key, Tag base) {
        this.customDataTags.put(key, base);
    }

    public void putAll(Map<String, Tag> map) {
        this.customDataTags.putAll(map);
    }

    public void putAll(CompoundTag compound) {
        for (String key : compound.keySet()) {
            this.customDataTags.put(key, compound.get(key));
        }
    }

    public Map<String, Tag> getRaw() {
        return this.customDataTags;
    }

    public CraftPersistentDataTypeRegistry getDataTagTypeRegistry() {
        return this.registry;
    }

    @Override
    public int hashCode() {
        int hashCode = 3;
        hashCode += this.customDataTags.hashCode(); // We will simply add the maps hashcode
        return hashCode;
    }

    public String serialize() {
        return CraftNBTTagConfigSerializer.serialize(this.toTagCompound());
    }

    public void clear() {
        this.customDataTags.clear();
    }

    @Override
    public void readFromBytes(final byte[] bytes, final boolean clear) throws java.io.IOException {
        if (clear) {
            this.clear();
        }
        try (final java.io.DataInputStream dataInput = new java.io.DataInputStream(new java.io.ByteArrayInputStream(bytes))) {
            final net.minecraft.nbt.CompoundTag compound = net.minecraft.nbt.NbtIo.read(dataInput);
            this.putAll(compound);
        }
    }

    public Map<String, Tag> getTagsCloned() {
        final Map<String, Tag> tags = new HashMap<>();
        this.customDataTags.forEach((key, tag) -> tags.put(key, tag.copy()));
        return tags;
    }

    public static Codec<CraftPersistentDataContainer> createCodec(final CraftPersistentDataTypeRegistry registry) {
        return CompoundTag.CODEC.xmap(tag -> {
            final CraftPersistentDataContainer container = new CraftPersistentDataContainer(registry);
            container.putAll(tag);
            return container;
        }, CraftPersistentDataContainer::toTagCompound);
    }

    @Override
    public int getSize() {
        return this.customDataTags.size();
    }
}
