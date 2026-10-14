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
import net.minecraft.world.entity.EntityTypes;
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
public class CraftMetaTropicalFishBucket extends CraftMetaItem implements TropicalFishBucketMeta {

    @Deprecated
    static final ItemMetaKey VARIANT = new ItemMetaKey("BucketVariantTag", "fish-variant");

    static final ItemMetaKeyType<net.minecraft.world.entity.animal.fish.TropicalFish.Pattern> PATTERN = new ItemMetaKeyType<>(DataComponents.TROPICAL_FISH_PATTERN, "fish-pattern");
    static final ItemMetaKeyType<net.minecraft.world.item.DyeColor> PATTERN_COLOR = new ItemMetaKeyType<>(DataComponents.TROPICAL_FISH_PATTERN_COLOR, "fish-pattern-color");
    static final ItemMetaKeyType<net.minecraft.world.item.DyeColor> BASE_COLOR = new ItemMetaKeyType<>(DataComponents.TROPICAL_FISH_BASE_COLOR, "fish-base-color");

    static final ItemMetaKeyType<TypedEntityData<EntityType<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");
    static final ItemMetaKeyType<CustomData> BUCKET_ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.BUCKET_ENTITY_DATA, "bucket-entity-tag");

    private net.minecraft.world.entity.animal.fish.TropicalFish.@Nullable Pattern pattern;
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

    CraftMetaTropicalFishBucket(DataComponentPatch patch, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledComponents) {
        super(patch, extraHandledComponents);

        getOrEmpty(patch, CraftMetaTropicalFishBucket.ENTITY_TAG).ifPresent((entityData) -> {
            this.entityTag = entityData.copyTagWithEntityId();
        });
        getOrEmpty(patch, CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG).ifPresent((customData) -> {
            this.bucketEntityTag = customData.copyTag();
        });
        if (!this.migrateLegacyItem(this.entityTag, this.bucketEntityTag)) {
            getOrEmpty(patch, CraftMetaTropicalFishBucket.PATTERN).ifPresent((pattern) -> {
                this.pattern = pattern;
            });
            getOrEmpty(patch, CraftMetaTropicalFishBucket.BASE_COLOR).ifPresent((bodyColor) -> {
                this.baseColor = bodyColor;
            });
            getOrEmpty(patch, CraftMetaTropicalFishBucket.PATTERN_COLOR).ifPresent((patternColor) -> {
                this.patternColor = patternColor;
            });
        }
    }

    @Deprecated
    private boolean migrateLegacyItem(@Nullable CompoundTag entityTag, @Nullable CompoundTag bucketEntityTag) {
        if (entityTag != null) {
            entityTag.getInt(CraftMetaTropicalFishBucket.VARIANT.NBT).ifPresent(packedVariant -> {
                this.pattern = net.minecraft.world.entity.animal.fish.TropicalFish.getPattern(packedVariant);
                this.baseColor = net.minecraft.world.entity.animal.fish.TropicalFish.getBaseColor(packedVariant);
                this.patternColor = net.minecraft.world.entity.animal.fish.TropicalFish.getPatternColor(packedVariant);
                entityTag.remove(CraftMetaTropicalFishBucket.VARIANT.NBT);
                if (this.isEmptyEntityTag(entityTag, EntityTypes.TROPICAL_FISH)) {
                    this.entityTag = null;
                }
            });
        }
        if (bucketEntityTag != null) {
            bucketEntityTag.getInt(CraftMetaTropicalFishBucket.VARIANT.NBT).ifPresent(packedVariant -> {
                this.pattern = net.minecraft.world.entity.animal.fish.TropicalFish.getPattern(packedVariant);
                this.baseColor = net.minecraft.world.entity.animal.fish.TropicalFish.getBaseColor(packedVariant);
                this.patternColor = net.minecraft.world.entity.animal.fish.TropicalFish.getPatternColor(packedVariant);
                bucketEntityTag.remove(CraftMetaTropicalFishBucket.VARIANT.NBT);
                if (bucketEntityTag.isEmpty()) {
                    this.bucketEntityTag = null;
                }
            });
        }
        return this.pattern != null && this.baseColor != null && this.patternColor != null;
    }

    CraftMetaTropicalFishBucket(Map<String, Object> map) {
        super(map);

        Integer packedVariant = SerializableMeta.getObject(Integer.class, map, CraftMetaTropicalFishBucket.VARIANT.BUKKIT, true);
        if (packedVariant != null) { // legacy
            this.pattern = net.minecraft.world.entity.animal.fish.TropicalFish.getPattern(packedVariant);
            this.baseColor = net.minecraft.world.entity.animal.fish.TropicalFish.getBaseColor(packedVariant);
            this.patternColor = net.minecraft.world.entity.animal.fish.TropicalFish.getPatternColor(packedVariant);
        } else {
            String pattern = SerializableMeta.getString(map, CraftMetaTropicalFishBucket.PATTERN.BUKKIT, true);
            if (pattern != null) {
                this.pattern = net.minecraft.world.entity.animal.fish.TropicalFish.Pattern.valueOf(pattern);
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

        tag.getCompound(CraftMetaTropicalFishBucket.ENTITY_TAG.NBT).ifPresent(entityTag -> {
            this.entityTag = entityTag;
        });
        tag.getCompound(CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG.NBT).ifPresent(entityTag -> {
            this.bucketEntityTag = entityTag;
        });
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
        this.pattern = net.minecraft.world.entity.animal.fish.TropicalFish.Pattern.values()[pattern.ordinal()];
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
