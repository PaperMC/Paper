package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.entity.CraftTropicalFish;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaTropicalFishBucket extends CraftMetaItem implements TropicalFishBucketMeta {
    static final ItemMetaKey VARIANT = new ItemMetaKey("BucketVariantTag", "fish-variant");
    static final ItemMetaKeyType<CustomData> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");

    private Integer variant;
    private NBTTagCompound entityTag;

    CraftMetaTropicalFishBucket(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaTropicalFishBucket)) {
            return;
        }

        CraftMetaTropicalFishBucket bucket = (CraftMetaTropicalFishBucket) meta;
        this.variant = bucket.variant;
        this.entityTag = bucket.entityTag;
    }

    CraftMetaTropicalFishBucket(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, ENTITY_TAG).ifPresent((nbt) -> {
            entityTag = nbt.copyTag();

            if (entityTag.contains(VARIANT.NBT, CraftMagicNumbers.NBT.TAG_INT)) {
                this.variant = entityTag.getInt(VARIANT.NBT);
            }
        });
    }

    CraftMetaTropicalFishBucket(Map<String, Object> map) {
        super(map);

        Integer variant = SerializableMeta.getObject(Integer.class, map, VARIANT.BUKKIT, true);
        if (variant != null) {
            this.variant = variant;
        }
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompound(ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(Map<String, NBTBase> internalTags) {
        if (entityTag != null && !entityTag.isEmpty()) {
            internalTags.put(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (hasVariant()) {
            if (entityTag == null) {
                entityTag = new NBTTagCompound();
            }
            entityTag.putInt(VARIANT.NBT, variant);
        }

        if (entityTag != null) {
            tag.put(ENTITY_TAG, CustomData.of(entityTag));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.TROPICAL_FISH_BUCKET;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBucketEmpty();
    }

    boolean isBucketEmpty() {
        return !(hasVariant() || entityTag != null);
    }

    @Override
    public DyeColor getPatternColor() {
        return CraftTropicalFish.getPatternColor(variant);
    }

    @Override
    public void setPatternColor(DyeColor color) {
        if (variant == null) {
            variant = 0;
        }
        variant = CraftTropicalFish.getData(color, getPatternColor(), getPattern());
    }

    @Override
    public DyeColor getBodyColor() {
        return CraftTropicalFish.getBodyColor(variant);
    }

    @Override
    public void setBodyColor(DyeColor color) {
        if (variant == null) {
            variant = 0;
        }
        variant = CraftTropicalFish.getData(getPatternColor(), color, getPattern());
    }

    @Override
    public TropicalFish.Pattern getPattern() {
        return CraftTropicalFish.getPattern(variant);
    }

    @Override
    public void setPattern(TropicalFish.Pattern pattern) {
        if (variant == null) {
            variant = 0;
        }
        variant = CraftTropicalFish.getData(getPatternColor(), getBodyColor(), pattern);
    }

    @Override
    public boolean hasVariant() {
        return variant != null;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaTropicalFishBucket) {
            CraftMetaTropicalFishBucket that = (CraftMetaTropicalFishBucket) meta;

            return (hasVariant() ? that.hasVariant() && this.variant.equals(that.variant) : !that.hasVariant())
                    && (entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : that.entityTag == null);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaTropicalFishBucket || isBucketEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasVariant()) {
            hash = 61 * hash + variant;
        }
        if (entityTag != null) {
            hash = 61 * hash + entityTag.hashCode();
        }

        return original != hash ? CraftMetaTropicalFishBucket.class.hashCode() ^ hash : hash;
    }


    @Override
    public CraftMetaTropicalFishBucket clone() {
        CraftMetaTropicalFishBucket clone = (CraftMetaTropicalFishBucket) super.clone();

        if (entityTag != null) {
            clone.entityTag = entityTag.copy();
        }

        return clone;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasVariant()) {
            builder.put(VARIANT.BUKKIT, variant);
        }

        return builder;
    }
}
