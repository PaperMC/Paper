package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.TypedEntityData;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaArmorStand extends CraftMetaItem implements com.destroystokyo.paper.inventory.meta.ArmorStandMeta {

    static final ItemMetaKeyType<TypedEntityData<EntityType<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");

    static final ItemMetaKey ENTITY_ID = new ItemMetaKey("id", "entity-id");
    static final ItemMetaKey INVISIBLE = new ItemMetaKey("Invisible", "invisible");
    static final ItemMetaKey NO_BASE_PLATE = new ItemMetaKey("NoBasePlate", "no-base-plate");
    static final ItemMetaKey SHOW_ARMS = new ItemMetaKey("ShowArms", "show-arms");
    static final ItemMetaKey SMALL = new ItemMetaKey("Small", "small");
    static final ItemMetaKey MARKER = new ItemMetaKey("Marker", "marker");

    CompoundTag entityTag;

    CraftMetaArmorStand(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaArmorStand)) {
            return;
        }

        CraftMetaArmorStand armorStand = (CraftMetaArmorStand) meta;
        this.entityTag = armorStand.entityTag;
    }

    CraftMetaArmorStand(DataComponentPatch tag, final java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaArmorStand.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTagWithEntityId();
        });
    }

    CraftMetaArmorStand(Map<String, Object> map) {
        super(map);

        String entityTag = SerializableMeta.getString(map, ENTITY_TAG.BUKKIT, true);
        if (entityTag != null) {
            java.io.ByteArrayInputStream buf = new java.io.ByteArrayInputStream(java.util.Base64.getDecoder().decode(entityTag));
            try {
                this.entityTag = net.minecraft.nbt.NbtIo.readCompressed(buf, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(CraftMetaItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            return;
        }
        SerializableMeta.getObjectOptionally(Boolean.class, map, INVISIBLE.BUKKIT, true).ifPresent((value) -> {
            populateTagIfNull();
            this.entityTag.putBoolean(INVISIBLE.NBT, value);
        });
        SerializableMeta.getObjectOptionally(Boolean.class, map, NO_BASE_PLATE.BUKKIT, true).ifPresent((value) -> {
            populateTagIfNull();
            this.entityTag.putBoolean(NO_BASE_PLATE.NBT, value);
        });
        SerializableMeta.getObjectOptionally(Boolean.class, map, SHOW_ARMS.BUKKIT, true).ifPresent((value) -> {
            populateTagIfNull();
            this.entityTag.putBoolean(SHOW_ARMS.NBT, value);
        });
        SerializableMeta.getObjectOptionally(Boolean.class, map, SMALL.BUKKIT, true).ifPresent((value) -> {
            populateTagIfNull();
            this.entityTag.putBoolean(SMALL.NBT, value);
        });
        SerializableMeta.getObjectOptionally(Boolean.class, map, MARKER.BUKKIT, true).ifPresent((value) -> {
            populateTagIfNull();
            this.entityTag.putBoolean(MARKER.NBT, value);
        });
        SerializableMeta.getObjectOptionally(String.class, map, ENTITY_ID, true).ifPresent((value) -> {
            populateTagIfNull();
            this.entityTag.putString(ENTITY_ID.NBT, value);
        });
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        tag.getCompound(CraftMetaArmorStand.ENTITY_TAG.NBT).ifPresent(entityTag -> {
            if (!entityTag.contains(ENTITY_ID.NBT)) entityTag.putString(ENTITY_ID.NBT, EntityType.getKey(EntityType.ARMOR_STAND).toString()); // fixup legacy armorstand metas that did not include this.
            this.entityTag = entityTag;
        });
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.entityTag != null) {
            tag.put(CraftMetaArmorStand.ENTITY_TAG, TypedEntityData.decodeEntity(this.entityTag));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.ARMOR_STAND;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isArmorStandEmpty();
    }

    boolean isArmorStandEmpty() {
        return this.entityTag == null || this.entityTag.size() == 1 && this.entityTag.contains("id"); // consider armor stand empty if tag is empty.
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaArmorStand other) {
            return Objects.equals(this.entityTag, other.entityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmorStand || this.isArmorStandEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.entityTag != null) {
            hash = 73 * hash + this.entityTag.hashCode();
        }

        return original != hash ? CraftMetaArmorStand.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.entityTag != null) {
            java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
            try {
                net.minecraft.nbt.NbtIo.writeCompressed(this.entityTag, buf);
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(CraftMetaItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            builder.put(ENTITY_TAG.BUKKIT, java.util.Base64.getEncoder().encodeToString(buf.toByteArray()));
        }

        return builder;
    }

    @Override
    public CraftMetaArmorStand clone() {
        CraftMetaArmorStand clone = (CraftMetaArmorStand) super.clone();

        if (this.entityTag != null) {
            clone.entityTag = this.entityTag.copy();
        }

        return clone;
    }

    private void populateTagIfNull() {
        if (this.entityTag == null) {
            this.entityTag = new CompoundTag();
            this.entityTag.putString(ENTITY_ID.NBT, EntityType.getKey(EntityType.ARMOR_STAND).toString());
        }
    }

    @Override
    public boolean isInvisible() {
        return this.entityTag != null && this.entityTag.getBooleanOr(INVISIBLE.NBT, false);
    }

    @Override
    public boolean hasNoBasePlate() {
        return this.entityTag != null && this.entityTag.getBooleanOr(NO_BASE_PLATE.NBT, false);
    }

    @Override
    public boolean shouldShowArms() {
        return this.entityTag != null && this.entityTag.getBooleanOr(SHOW_ARMS.NBT, false);
    }

    @Override
    public boolean isSmall() {
        return this.entityTag != null && this.entityTag.getBooleanOr(SMALL.NBT, false);
    }

    @Override
    public boolean isMarker() {
        return this.entityTag != null && this.entityTag.getBooleanOr(MARKER.NBT, false);
    }

    @Override
    public void setInvisible(boolean invisible) {
        populateTagIfNull();
        this.entityTag.putBoolean(INVISIBLE.NBT, invisible);
    }

    @Override
    public void setNoBasePlate(boolean noBasePlate) {
        populateTagIfNull();
        this.entityTag.putBoolean(NO_BASE_PLATE.NBT, noBasePlate);
    }

    @Override
    public void setShowArms(boolean showArms) {
        populateTagIfNull();
        this.entityTag.putBoolean(SHOW_ARMS.NBT, showArms);
    }

    @Override
    public void setSmall(boolean small) {
        populateTagIfNull();
        this.entityTag.putBoolean(SMALL.NBT, small);
    }

    @Override
    public void setMarker(boolean marker) {
        populateTagIfNull();
        this.entityTag.putBoolean(MARKER.NBT, marker);
    }
}
