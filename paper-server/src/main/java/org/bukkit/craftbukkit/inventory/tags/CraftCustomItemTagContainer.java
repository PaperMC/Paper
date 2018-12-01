package org.bukkit.craftbukkit.inventory.tags;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftCustomTagTypeRegistry;
import org.bukkit.craftbukkit.util.CraftNBTTagConfigSerializer;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;
import org.bukkit.inventory.meta.tags.ItemTagType;

public final class CraftCustomItemTagContainer implements CustomItemTagContainer {

    private final Map<String, NBTBase> customTags = new HashMap<>();
    private final CraftCustomTagTypeRegistry tagTypeRegistry;
    private final CraftItemTagAdapterContext adapterContext;

    public CraftCustomItemTagContainer(Map<String, NBTBase> customTags, CraftCustomTagTypeRegistry tagTypeRegistry) {
        this(tagTypeRegistry);
        this.customTags.putAll(customTags);
    }

    public CraftCustomItemTagContainer(CraftCustomTagTypeRegistry tagTypeRegistry) {
        this.tagTypeRegistry = tagTypeRegistry;
        this.adapterContext = new CraftItemTagAdapterContext(this.tagTypeRegistry);
    }

    @Override
    public <T, Z> void setCustomTag(NamespacedKey key, ItemTagType<T, Z> type, Z value) {
        Validate.notNull(key, "The provided key for the custom value was null");
        Validate.notNull(type, "The provided type for the custom value was null");
        Validate.notNull(value, "The provided value for the custom value was null");

        this.customTags.put(key.toString(), tagTypeRegistry.wrap(type.getPrimitiveType(), type.toPrimitive(value, adapterContext)));
    }

    @Override
    public <T, Z> boolean hasCustomTag(NamespacedKey key, ItemTagType<T, Z> type) {
        Validate.notNull(key, "The provided key for the custom value was null");
        Validate.notNull(type, "The provided type for the custom value was null");

        NBTBase value = this.customTags.get(key.toString());
        if (value == null) {
            return false;
        }

        return tagTypeRegistry.isInstanceOf(type.getPrimitiveType(), value);
    }

    @Override
    public <T, Z> Z getCustomTag(NamespacedKey key, ItemTagType<T, Z> type) {
        Validate.notNull(key, "The provided key for the custom value was null");
        Validate.notNull(type, "The provided type for the custom value was null");

        NBTBase value = this.customTags.get(key.toString());
        if (value == null) {
            return null;
        }

        return type.fromPrimitive(tagTypeRegistry.extract(type.getPrimitiveType(), value), adapterContext);
    }

    @Override
    public void removeCustomTag(NamespacedKey key) {
        Validate.notNull(key, "The provided key for the custom value was null");

        this.customTags.remove(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return this.customTags.isEmpty();
    }

    @Override
    public ItemTagAdapterContext getAdapterContext() {
        return this.adapterContext;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CraftCustomItemTagContainer)) {
            return false;
        }

        Map<String, NBTBase> myRawMap = getRaw();
        Map<String, NBTBase> theirRawMap = ((CraftCustomItemTagContainer) obj).getRaw();

        return Objects.equals(myRawMap, theirRawMap);
    }

    public NBTTagCompound toTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        for (Entry<String, NBTBase> entry : this.customTags.entrySet()) {
            tag.set(entry.getKey(), entry.getValue());
        }
        return tag;
    }

    public void put(String key, NBTBase base) {
        this.customTags.put(key, base);
    }

    public void putAll(Map<String, NBTBase> map) {
        this.customTags.putAll(map);
    }

    public void putAll(NBTTagCompound compound) {
        for (String key : compound.getKeys()) {
            this.customTags.put(key, compound.get(key));
        }
    }

    public Map<String, NBTBase> getRaw() {
        return this.customTags;
    }

    @Override
    public int hashCode() {
        int hashCode = 3;
        hashCode += this.customTags.hashCode(); // We will simply add the maps hashcode
        return hashCode;
    }

    public Map<String, Object> serialize() {
        return (Map<String, Object>) CraftNBTTagConfigSerializer.serialize(toTagCompound());
    }
}
