package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaEntityTag extends CraftMetaItem {

    private static final Set<Material> ENTITY_TAGGABLE_MATERIALS = Sets.newHashSet(
            Material.COD_BUCKET,
            Material.PUFFERFISH_BUCKET,
            Material.SALMON_BUCKET,
            Material.ITEM_FRAME,
            Material.GLOW_ITEM_FRAME,
            Material.PAINTING
    );

    static final ItemMetaKeyType<CustomData> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "EntityTag", "entity-tag");
    CompoundTag entityTag;

    CraftMetaEntityTag(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaEntityTag)) {
            return;
        }

        CraftMetaEntityTag entity = (CraftMetaEntityTag) meta;
        this.entityTag = entity.entityTag;
    }

    CraftMetaEntityTag(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, CraftMetaEntityTag.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTag();
        });
    }

    CraftMetaEntityTag(Map<String, Object> map) {
        super(map);
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(CraftMetaEntityTag.ENTITY_TAG.NBT)) {
            this.entityTag = tag.getCompound(CraftMetaEntityTag.ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (this.entityTag != null && !this.entityTag.isEmpty()) {
            internalTags.put(CraftMetaEntityTag.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.entityTag != null) {
            tag.put(CraftMetaEntityTag.ENTITY_TAG, CustomData.of(this.entityTag));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return CraftMetaEntityTag.ENTITY_TAGGABLE_MATERIALS.contains(type);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isEntityTagEmpty();
    }

    boolean isEntityTagEmpty() {
        return !(this.entityTag != null);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaEntityTag) {
            CraftMetaEntityTag that = (CraftMetaEntityTag) meta;

            return this.entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : this.entityTag == null;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaEntityTag || this.isEntityTagEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.entityTag != null) {
            hash = 73 * hash + this.entityTag.hashCode();
        }

        return original != hash ? CraftMetaEntityTag.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        return builder;
    }

    @Override
    public CraftMetaEntityTag clone() {
        CraftMetaEntityTag clone = (CraftMetaEntityTag) super.clone();

        if (this.entityTag != null) {
            clone.entityTag = this.entityTag.copy();
        }

        return clone;
    }
}
