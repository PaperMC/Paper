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
import org.bukkit.DyeColor;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.entity.CraftTropicalFish;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaTropicalFishBucket extends CraftMetaItem implements TropicalFishBucketMeta {

    static final ItemMetaKey VARIANT = new ItemMetaKey("BucketVariantTag", "fish-variant");
    static final ItemMetaKeyType<TypedEntityData<EntityType<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");
    static final ItemMetaKeyType<CustomData> BUCKET_ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.BUCKET_ENTITY_DATA, "bucket-entity-tag");

    private Integer variant;
    private CompoundTag entityTag;
    private CompoundTag bucketEntityTag;

    CraftMetaTropicalFishBucket(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof final CraftMetaTropicalFishBucket tropicalFishBucketMeta)) {
            return;
        }

        this.variant = tropicalFishBucketMeta.variant;
        this.entityTag = tropicalFishBucketMeta.entityTag;
        this.bucketEntityTag = tropicalFishBucketMeta.bucketEntityTag;
    }

    CraftMetaTropicalFishBucket(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaTropicalFishBucket.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTagWithEntityId();
            this.entityTag.getInt(CraftMetaTropicalFishBucket.VARIANT.NBT).ifPresent(variant -> this.variant = variant);
        });
        getOrEmpty(tag, CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG).ifPresent((nbt) -> {
            this.bucketEntityTag = nbt.copyTag();
            this.bucketEntityTag.getInt(CraftMetaTropicalFishBucket.VARIANT.NBT).ifPresent(variant -> this.variant = variant);
        });
    }

    CraftMetaTropicalFishBucket(Map<String, Object> map) {
        super(map);

        Integer variant = SerializableMeta.getObject(Integer.class, map, CraftMetaTropicalFishBucket.VARIANT.BUKKIT, true);
        if (variant != null) {
            this.variant = variant;
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        this.entityTag = tag.getCompound(CraftMetaTropicalFishBucket.ENTITY_TAG.NBT).orElse(this.entityTag);
        this.bucketEntityTag = tag.getCompound(CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG.NBT).orElse(this.bucketEntityTag);
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (this.entityTag != null && !this.entityTag.isEmpty()) {
            internalTags.put(CraftMetaTropicalFishBucket.ENTITY_TAG.NBT, this.entityTag);
        }
        if (this.bucketEntityTag != null && !this.bucketEntityTag.isEmpty()) {
            internalTags.put(CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG.NBT, this.bucketEntityTag);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.entityTag != null) {
            tag.put(CraftMetaTropicalFishBucket.ENTITY_TAG, TypedEntityData.decodeEntity(this.entityTag));
        }

        CompoundTag bucketEntityTag = (this.bucketEntityTag != null) ? this.bucketEntityTag.copy() : null;
        if (this.hasVariant()) {
            if (bucketEntityTag == null) {
                bucketEntityTag = new CompoundTag();
            }
            bucketEntityTag.putInt(CraftMetaTropicalFishBucket.VARIANT.NBT, this.variant);
        }

        if (bucketEntityTag != null) {
            tag.put(CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG, CustomData.of(bucketEntityTag));
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
    public DyeColor getPatternColor() {
        com.google.common.base.Preconditions.checkState(this.hasVariant(), "This bucket doesn't have variant, check hasVariant first!");
        return CraftTropicalFish.getPatternColor(this.variant);
    }

    @Override
    public void setPatternColor(DyeColor color) {
        if (this.variant == null) {
            this.variant = 0;
        }
        this.variant = CraftTropicalFish.getData(color, this.getBodyColor(), this.getPattern()); // Paper - properly set tropical fish pattern color without mutating body color
    }

    @Override
    public DyeColor getBodyColor() {
        com.google.common.base.Preconditions.checkState(this.hasVariant(), "This bucket doesn't have variant, check hasVariant first!");
        return CraftTropicalFish.getBodyColor(this.variant);
    }

    @Override
    public void setBodyColor(DyeColor color) {
        if (this.variant == null) {
            this.variant = 0;
        }
        this.variant = CraftTropicalFish.getData(this.getPatternColor(), color, this.getPattern());
    }

    @Override
    public TropicalFish.Pattern getPattern() {
        com.google.common.base.Preconditions.checkState(this.hasVariant(), "This bucket doesn't have variant, check hasVariant first!");
        return CraftTropicalFish.getPattern(this.variant);
    }

    @Override
    public void setPattern(TropicalFish.Pattern pattern) {
        if (this.variant == null) {
            this.variant = 0;
        }
        this.variant = CraftTropicalFish.getData(this.getPatternColor(), this.getBodyColor(), pattern);
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
        if (meta instanceof final CraftMetaTropicalFishBucket other) {
            return Objects.equals(this.variant, other.variant)
                    && Objects.equals(this.entityTag, other.entityTag)
                    && Objects.equals(this.bucketEntityTag, other.bucketEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaTropicalFishBucket || this.isBucketEmpty());
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

        return original != hash ? CraftMetaTropicalFishBucket.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaTropicalFishBucket clone() {
        CraftMetaTropicalFishBucket clone = (CraftMetaTropicalFishBucket) super.clone();

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
            builder.put(CraftMetaTropicalFishBucket.VARIANT.BUKKIT, this.variant);
        }

        return builder;
    }
}
