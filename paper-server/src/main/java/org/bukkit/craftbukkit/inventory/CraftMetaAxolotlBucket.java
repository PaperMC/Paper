package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.meta.AxolotlBucketMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaAxolotlBucket extends CraftMetaItem implements AxolotlBucketMeta {

    static final ItemMetaKey VARIANT = new ItemMetaKey("Variant", "axolotl-variant");
    static final ItemMetaKeyType<TypedEntityData<EntityType<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");
    static final ItemMetaKeyType<CustomData> BUCKET_ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.BUCKET_ENTITY_DATA, "bucket-entity-tag");

    private Integer variant;
    private CompoundTag entityTag;
    private CompoundTag bucketEntityTag;

    CraftMetaAxolotlBucket(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof final CraftMetaAxolotlBucket bucketMeta)) {
            return;
        }

        this.variant = bucketMeta.variant;
        this.entityTag = bucketMeta.entityTag;
        this.bucketEntityTag = bucketMeta.bucketEntityTag;
    }

    CraftMetaAxolotlBucket(DataComponentPatch tag, final java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaAxolotlBucket.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTagWithEntityId();
            this.entityTag.getInt(CraftMetaAxolotlBucket.VARIANT.NBT).ifPresent(variant -> this.variant = variant);
        });
        getOrEmpty(tag, CraftMetaAxolotlBucket.BUCKET_ENTITY_TAG).ifPresent((nbt) -> {
            this.bucketEntityTag = nbt.copyTag();
            this.bucketEntityTag.getInt(CraftMetaAxolotlBucket.VARIANT.NBT).ifPresent(variant -> this.variant = variant);
        });
    }

    CraftMetaAxolotlBucket(Map<String, Object> map) {
        super(map);

        Integer variant = SerializableMeta.getObject(Integer.class, map, CraftMetaAxolotlBucket.VARIANT.BUKKIT, true);
        if (variant != null) {
            this.variant = variant;
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        this.entityTag = tag.getCompound(CraftMetaAxolotlBucket.ENTITY_TAG.NBT).orElse(this.entityTag);
        this.bucketEntityTag = tag.getCompound(CraftMetaAxolotlBucket.BUCKET_ENTITY_TAG.NBT).orElse(this.bucketEntityTag);
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (this.entityTag != null && !this.entityTag.isEmpty()) {
            internalTags.put(CraftMetaAxolotlBucket.ENTITY_TAG.NBT, this.entityTag);
        }
        if (this.bucketEntityTag != null && !this.bucketEntityTag.isEmpty()) {
            internalTags.put(CraftMetaAxolotlBucket.BUCKET_ENTITY_TAG.NBT, this.bucketEntityTag);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.entityTag != null) {
            tag.put(CraftMetaAxolotlBucket.ENTITY_TAG, TypedEntityData.decodeEntity(this.entityTag));
        }

        CompoundTag bucketEntityTag = (this.bucketEntityTag != null) ? this.bucketEntityTag.copy() : null;
        if (this.hasVariant()) {
            if (bucketEntityTag == null) {
                bucketEntityTag = new CompoundTag();
            }
            bucketEntityTag.putInt(CraftMetaAxolotlBucket.VARIANT.NBT, this.variant);
        }

        if (bucketEntityTag != null) {
            tag.put(CraftMetaAxolotlBucket.BUCKET_ENTITY_TAG, CustomData.of(bucketEntityTag));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBucketEmpty();
    }

    boolean isBucketEmpty() {
        return !(this.hasVariant() || this.entityTag != null || this.bucketEntityTag != null);
    }

    @Override
    public Axolotl.Variant getVariant() {
        com.google.common.base.Preconditions.checkState(this.hasVariant(), "Variant is absent, check hasVariant first!");
        return Axolotl.Variant.values()[this.variant];
    }

    @Override
    public void setVariant(Axolotl.Variant variant) {
        com.google.common.base.Preconditions.checkArgument(variant != null, "Variant cannot be null!");
        this.variant = variant.ordinal();
    }

    @Override
    public boolean hasVariant() {
        return this.variant != null;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaAxolotlBucket other) {
            return Objects.equals(this.variant, other.variant)
                    && Objects.equals(this.entityTag, other.entityTag)
                    && Objects.equals(this.bucketEntityTag, other.bucketEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaAxolotlBucket || this.isBucketEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasVariant()) {
            hash = 61 * hash + this.variant;
        }
        if (this.entityTag != null) {
            hash = 61 * hash + this.entityTag.hashCode();
        }
        if (this.bucketEntityTag != null) {
            hash = 61 * hash + this.bucketEntityTag.hashCode();
        }

        return original != hash ? CraftMetaAxolotlBucket.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaAxolotlBucket clone() {
        CraftMetaAxolotlBucket clone = (CraftMetaAxolotlBucket) super.clone();

        if (this.entityTag != null) {
            clone.entityTag = this.entityTag.copy();
        }
        if (this.bucketEntityTag != null) {
            clone.bucketEntityTag = this.bucketEntityTag.copy();
        }

        return clone;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasVariant()) {
            builder.put(CraftMetaAxolotlBucket.VARIANT.BUKKIT, this.variant);
        }

        return builder;
    }
}
