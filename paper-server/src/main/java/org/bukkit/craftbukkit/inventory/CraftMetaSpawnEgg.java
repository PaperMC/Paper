package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaSpawnEgg extends CraftMetaItem implements SpawnEggMeta {

    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey ENTITY_ID = new ItemMetaKey("id");

    private EntityType spawnedType;

    CraftMetaSpawnEgg(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaSpawnEgg)) {
            return;
        }

        CraftMetaSpawnEgg egg = (CraftMetaSpawnEgg) meta;
        this.spawnedType = egg.spawnedType;
    }

    CraftMetaSpawnEgg(NBTTagCompound tag) {
        super(tag);

        if (tag.hasKey(ENTITY_TAG.NBT)) {
            NBTTagCompound entityTag = tag.getCompound(ENTITY_TAG.NBT);

            if (entityTag.hasKey(ENTITY_ID.NBT)) {
                this.spawnedType = EntityType.fromName(new MinecraftKey(entityTag.getString(ENTITY_ID.NBT)).a()); // PAIL: rename
            }
        }
    }

    CraftMetaSpawnEgg(Map<String, Object> map) {
        super(map);

        String entityType = SerializableMeta.getString(map, ENTITY_ID.BUKKIT, true);
        setSpawnedType(EntityType.fromName(entityType));
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (hasSpawnedType()) {
            NBTTagCompound entityTag = new NBTTagCompound();
            entityTag.setString(ENTITY_ID.NBT, new MinecraftKey(spawnedType.getName()).toString());

            tag.set(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case MONSTER_EGG:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSpawnEggEmpty();
    }

    boolean isSpawnEggEmpty() {
        return !hasSpawnedType();
    }

    boolean hasSpawnedType() {
        return spawnedType != null;
    }

    @Override
    public EntityType getSpawnedType() {
        return spawnedType;
    }

    @Override
    public void setSpawnedType(EntityType type) {
        Preconditions.checkArgument(type == null || type.getName() != null, "Spawn egg type must have name (%s)", type);

        this.spawnedType = type;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSpawnEgg) {
            CraftMetaSpawnEgg that = (CraftMetaSpawnEgg) meta;

            return hasSpawnedType() ? that.hasSpawnedType() && this.spawnedType.equals(that.spawnedType) : !that.hasSpawnedType();
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSpawnEgg || isSpawnEggEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasSpawnedType()) {
            hash = 73 * hash + spawnedType.hashCode();
        }

        return original != hash ? CraftMetaSpawnEgg.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasSpawnedType()) {
            builder.put(ENTITY_ID.BUKKIT, spawnedType.getName());
        }

        return builder;
    }

    @Override
    public CraftMetaSpawnEgg clone() {
        CraftMetaSpawnEgg clone = (CraftMetaSpawnEgg) super.clone();

        clone.spawnedType = spawnedType;

        return clone;
    }
}
