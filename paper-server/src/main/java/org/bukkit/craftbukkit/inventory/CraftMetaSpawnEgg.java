package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.TypedEntityData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.entity.CraftEntitySnapshot;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaSpawnEgg extends CraftMetaItem implements SpawnEggMeta {

    static final ItemMetaKeyType<TypedEntityData<net.minecraft.world.entity.EntityType<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey ENTITY_ID = new ItemMetaKey("id");

    private CompoundTag entityTag;

    CraftMetaSpawnEgg(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaSpawnEgg spawnEggMeta)) {
            return;
        }

        this.entityTag = spawnEggMeta.entityTag;
    }

    CraftMetaSpawnEgg(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaSpawnEgg.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTagWithEntityId();
        });
    }

    CraftMetaSpawnEgg(Map<String, Object> map) {
        super(map);
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        this.entityTag = tag.getCompound(CraftMetaSpawnEgg.ENTITY_TAG.NBT).orElse(this.entityTag);
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (this.entityTag != null && !this.entityTag.isEmpty()) {
            internalTags.put(CraftMetaSpawnEgg.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.entityTag != null) {
            tag.put(CraftMetaSpawnEgg.ENTITY_TAG, TypedEntityData.decodeEntity(this.entityTag));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isSpawnEggEmpty();
    }

    boolean isSpawnEggEmpty() {
        return this.entityTag == null;
    }

    @Override
    public EntityType getSpawnedType() {
        throw new UnsupportedOperationException("Must check item type to get spawned type");
    }

    @Override
    public void setSpawnedType(EntityType type) {
        throw new UnsupportedOperationException("Must change item type to set spawned type");
    }

    @Override
    public EntityType getCustomSpawnedType() {
        return Optional.ofNullable(this.entityTag)
            .flatMap(tag -> tag.read(ENTITY_ID.NBT, BuiltInRegistries.ENTITY_TYPE.byNameCodec()))
            .map(CraftEntityType::minecraftToBukkit)
            .orElse(null);
    }

    @Override
    public void setCustomSpawnedType(final EntityType type) {
        if (type == null) {
            if (this.entityTag != null) {
                this.entityTag.remove(ENTITY_ID.NBT);
            }
        } else {
            if (this.entityTag == null) {
                this.entityTag = new CompoundTag();
            }
            this.entityTag.putString(ENTITY_ID.NBT, type.key().toString());
        }
    }

    @Override
    public EntitySnapshot getSpawnedEntity() {
        if (this.entityTag == null) {
            return null;
        }

        return CraftEntitySnapshot.create(this.entityTag);
    }

    @Override
    public void setSpawnedEntity(EntitySnapshot snapshot) {
        Preconditions.checkArgument(snapshot.getEntityType().isSpawnable(), "Entity is not spawnable");
        this.entityTag = ((CraftEntitySnapshot) snapshot).getData();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaSpawnEgg other) {
            return Objects.equals(this.entityTag, other.entityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSpawnEgg || this.isSpawnEggEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.entityTag != null) {
            hash = 73 * hash + this.entityTag.hashCode();
        }

        return original != hash ? CraftMetaSpawnEgg.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        return builder;
    }

    @Override
    public CraftMetaSpawnEgg clone() {
        CraftMetaSpawnEgg clone = (CraftMetaSpawnEgg) super.clone();

        if (this.entityTag != null) {
            clone.entityTag = this.entityTag.copy();
        }

        return clone;
    }
}
