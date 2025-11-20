package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
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
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@DelegateDeserialization(SerializableMeta.class)
class CraftMetaTropicalFishBucket extends CraftMetaItem implements TropicalFishBucketMeta {

    @Deprecated
    static final ItemMetaKey VARIANT = new ItemMetaKey("BucketVariantTag", "fish-variant");

    static final ItemMetaKeyType<net.minecraft.world.entity.animal.TropicalFish.Pattern> PATTERN = new ItemMetaKeyType<>(DataComponents.TROPICAL_FISH_PATTERN, "fish-pattern");
    static final ItemMetaKeyType<net.minecraft.world.item.DyeColor> PATTERN_COLOR = new ItemMetaKeyType<>(DataComponents.TROPICAL_FISH_PATTERN_COLOR, "fish-pattern-color");
    static final ItemMetaKeyType<net.minecraft.world.item.DyeColor> BASE_COLOR = new ItemMetaKeyType<>(DataComponents.TROPICAL_FISH_BASE_COLOR, "fish-base-color");

    static final ItemMetaKeyType<TypedEntityData<EntityType<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");
    static final ItemMetaKeyType<CustomData> BUCKET_ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.BUCKET_ENTITY_DATA, "bucket-entity-tag");

    private net.minecraft.world.entity.animal.TropicalFish.@Nullable Pattern pattern;
    private net.minecraft.world.item.@Nullable DyeColor baseColor;
    private net.minecraft.world.item.@Nullable DyeColor patternColor;

    private @Nullable CompoundTag entityTag;
    private @Nullable CompoundTag bucketEntityTag;

    CraftMetaTropicalFishBucket(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof final CraftMetaTropicalFishBucket tropicalFishBucketMeta)) {
            return;
        }

        this.pattern = tropicalFishBucketMeta.pattern;
        this.baseColor = tropicalFishBucketMeta.baseColor;
        this.patternColor = tropicalFishBucketMeta.patternColor;
        this.entityTag = tropicalFishBucketMeta.entityTag;
        this.bucketEntityTag = tropicalFishBucketMeta.bucketEntityTag;
    }

    CraftMetaTropicalFishBucket(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaTropicalFishBucket.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTagWithEntityId();
            this.entityTag.getInt(CraftMetaTropicalFishBucket.VARIANT.NBT).ifPresent(variant -> {
                this.pattern = net.minecraft.world.entity.animal.TropicalFish.getPattern(variant);
                this.baseColor = net.minecraft.world.entity.animal.TropicalFish.getBaseColor(variant);
                this.patternColor = net.minecraft.world.entity.animal.TropicalFish.getPatternColor(variant);
            });
        });
        getOrEmpty(tag, CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG).ifPresent((nbt) -> {
            this.bucketEntityTag = nbt.copyTag();
            this.bucketEntityTag.getInt(CraftMetaTropicalFishBucket.VARIANT.NBT).ifPresent(variant -> {
                this.pattern = net.minecraft.world.entity.animal.TropicalFish.getPattern(variant);
                this.baseColor = net.minecraft.world.entity.animal.TropicalFish.getBaseColor(variant);
                this.patternColor = net.minecraft.world.entity.animal.TropicalFish.getPatternColor(variant);
            });
        });
        getOrEmpty(tag, CraftMetaTropicalFishBucket.PATTERN).ifPresent((pattern) -> {
            this.pattern = pattern;
        });
        getOrEmpty(tag, CraftMetaTropicalFishBucket.BASE_COLOR).ifPresent((bodyColor) -> {
            this.baseColor = bodyColor;
        });
        getOrEmpty(tag, CraftMetaTropicalFishBucket.PATTERN_COLOR).ifPresent((patternColor) -> {
            this.patternColor = patternColor;
        });
    }

    CraftMetaTropicalFishBucket(Map<String, Object> map) {
        super(map);

        Integer variant = SerializableMeta.getObject(Integer.class, map, CraftMetaTropicalFishBucket.VARIANT.BUKKIT, true);
        if (variant != null) { // legacy
            this.pattern = net.minecraft.world.entity.animal.TropicalFish.getPattern(variant);
            this.baseColor = net.minecraft.world.entity.animal.TropicalFish.getBaseColor(variant);
            this.patternColor = net.minecraft.world.entity.animal.TropicalFish.getPatternColor(variant);
        } else {
            String pattern = SerializableMeta.getString(map, CraftMetaTropicalFishBucket.PATTERN.BUKKIT, true);
            if (pattern != null) {
                this.pattern = net.minecraft.world.entity.animal.TropicalFish.Pattern.valueOf(pattern);
            }

            String baseColor = SerializableMeta.getString(map, CraftMetaTropicalFishBucket.BASE_COLOR.BUKKIT, true);
            if (baseColor != null) {
                this.baseColor = net.minecraft.world.item.DyeColor.valueOf(baseColor);
            }

            String patternColor = SerializableMeta.getString(map, CraftMetaTropicalFishBucket.PATTERN_COLOR.BUKKIT, true);
            if (patternColor != null) {
                this.patternColor = net.minecraft.world.item.DyeColor.valueOf(patternColor);
            }
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

        if (this.bucketEntityTag != null) {
            tag.put(CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG, CustomData.of(this.bucketEntityTag));
        }

        if (this.pattern != null) {
            tag.put(CraftMetaTropicalFishBucket.PATTERN, this.pattern);
        }
        if (this.baseColor != null) {
            tag.put(CraftMetaTropicalFishBucket.BASE_COLOR, this.baseColor);
        }
        if (this.patternColor != null) {
            tag.put(CraftMetaTropicalFishBucket.PATTERN_COLOR, this.patternColor);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBucketEmpty();
    }

    boolean isBucketEmpty() {
        return !(this.hasPattern() || this.hasBodyColor() || this.hasPatternColor() || this.entityTag != null || this.bucketEntityTag != null);
    }

    @Override
    public DyeColor getPatternColor() {
        Preconditions.checkState(this.hasPatternColor(), "Pattern color is absent, check hasPatternColor first!");
        return DyeColor.values()[this.patternColor.ordinal()];
    }

    @Override
    public void setPatternColor(DyeColor color) {
        Preconditions.checkArgument(color != null, "Pattern color cannot be null!");
        this.patternColor = net.minecraft.world.item.DyeColor.byId(color.ordinal());
    }

    @Override
    public DyeColor getBodyColor() {
        Preconditions.checkState(this.hasBodyColor(), "Body color is absent, check hasBodyColor first!");
        return DyeColor.values()[this.baseColor.ordinal()];
    }

    @Override
    public void setBodyColor(DyeColor color) {
        Preconditions.checkArgument(color != null, "Body color cannot be null!");
        this.baseColor = net.minecraft.world.item.DyeColor.byId(color.ordinal());
    }

    @Override
    public TropicalFish.Pattern getPattern() {
        Preconditions.checkState(this.hasPattern(), "Pattern is absent, check hasPattern first!");
        return TropicalFish.Pattern.values()[this.pattern.ordinal()];
    }

    @Override
    public void setPattern(TropicalFish.Pattern pattern) {
        Preconditions.checkArgument(pattern != null, "Pattern cannot be null!");
        this.pattern = net.minecraft.world.entity.animal.TropicalFish.Pattern.values()[pattern.ordinal()];
    }

    @Override
    public boolean hasPattern() {
        return this.pattern != null;
    }

    @Override
    public boolean hasBodyColor() {
        return this.baseColor != null;
    }

    @Override
    public boolean hasPatternColor() {
        return this.patternColor != null;
    }

    @Override
    public boolean hasVariant() {
        return this.hasPattern() || this.hasBodyColor() || this.hasPatternColor();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaTropicalFishBucket other) {
            return Objects.equals(this.pattern, other.pattern)
                    && Objects.equals(this.baseColor, other.baseColor)
                    && Objects.equals(this.patternColor, other.patternColor)
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

        if (this.pattern != null) {
            hash = 61 * hash + this.pattern.hashCode();
        }
        if (this.baseColor != null) {
            hash = 61 * hash + this.baseColor.hashCode();
        }
        if (this.patternColor != null) {
            hash = 61 * hash + this.patternColor.hashCode();
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
        clone.patternColor = this.patternColor;
        clone.baseColor = this.baseColor;
        clone.pattern = this.pattern;

        return clone;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.pattern != null) {
            builder.put(CraftMetaTropicalFishBucket.PATTERN.BUKKIT, this.pattern.name());
        }
        if (this.baseColor != null) {
            builder.put(CraftMetaTropicalFishBucket.BASE_COLOR.BUKKIT, this.baseColor.name());
        }
        if (this.patternColor != null) {
            builder.put(CraftMetaTropicalFishBucket.PATTERN_COLOR.BUKKIT, this.patternColor.name());
        }

        return builder;
    }
}
