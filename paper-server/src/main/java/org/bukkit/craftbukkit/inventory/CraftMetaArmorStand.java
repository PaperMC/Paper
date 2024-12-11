package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaArmorStand extends CraftMetaItem implements com.destroystokyo.paper.inventory.meta.ArmorStandMeta { // Paper

    static final ItemMetaKeyType<CustomData> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");
    // Paper start
    static final ItemMetaKey ENTITY_ID = new ItemMetaKey("id", "entity-id");
    static final ItemMetaKey INVISIBLE = new ItemMetaKey("Invisible", "invisible");
    static final ItemMetaKey NO_BASE_PLATE = new ItemMetaKey("NoBasePlate", "no-base-plate");
    static final ItemMetaKey SHOW_ARMS = new ItemMetaKey("ShowArms", "show-arms");
    static final ItemMetaKey SMALL = new ItemMetaKey("Small", "small");
    static final ItemMetaKey MARKER = new ItemMetaKey("Marker", "marker");
    // Paper end
    CompoundTag entityTag;

    CraftMetaArmorStand(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaArmorStand)) {
            return;
        }

        CraftMetaArmorStand armorStand = (CraftMetaArmorStand) meta;
        this.entityTag = armorStand.entityTag;
    }

    CraftMetaArmorStand(DataComponentPatch tag, final java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) { // Paper
        super(tag, extraHandledDcts); // Paper

        getOrEmpty(tag, CraftMetaArmorStand.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTag();
        });
    }

    CraftMetaArmorStand(Map<String, Object> map) {
        super(map);
        // Paper start
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
        // Paper end
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(CraftMetaArmorStand.ENTITY_TAG.NBT)) {
            this.entityTag = tag.getCompound(CraftMetaArmorStand.ENTITY_TAG.NBT);
            if (!this.entityTag.contains(ENTITY_ID.NBT)) entityTag.putString(ENTITY_ID.NBT, "minecraft:armor_stand"); // Paper - fixup legacy armorstand metas that did not include this.
        }
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (false && this.entityTag != null && !this.entityTag.isEmpty()) { // Paper - now correctly serialised as entity tag
            internalTags.put(CraftMetaArmorStand.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.entityTag != null) {
            tag.put(CraftMetaArmorStand.ENTITY_TAG, CustomData.of(this.entityTag));
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
        return entityTag == null || entityTag.isEmpty(); // Paper - consider armor stand empty if tag is empty.
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaArmorStand) {
            CraftMetaArmorStand that = (CraftMetaArmorStand) meta;

            // Paper start
            return java.util.Objects.equals(this.entityTag, that.entityTag);
            // Paper end
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

        // Paper start
        if (entityTag == null) {
            return builder;
        } else if (true) {
            java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
            try {
                net.minecraft.nbt.NbtIo.writeCompressed(entityTag, buf);
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(CraftMetaItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            builder.put(ENTITY_TAG.BUKKIT, java.util.Base64.getEncoder().encodeToString(buf.toByteArray()));
            return builder;
        }
        // Paper end

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

    // Paper start
    private void populateTagIfNull() {
        if (this.entityTag == null) {
            this.entityTag = new CompoundTag();
            this.entityTag.putString(ENTITY_ID.NBT, "minecraft:armor_stand");
        }
    }

    @Override
    public boolean isInvisible() {
        return entityTag != null && entityTag.contains(INVISIBLE.NBT) && entityTag.getBoolean(INVISIBLE.NBT);
    }

    @Override
    public boolean hasNoBasePlate() {
        return entityTag != null && entityTag.contains(NO_BASE_PLATE.NBT) && entityTag.getBoolean(NO_BASE_PLATE.NBT);
    }

    @Override
    public boolean shouldShowArms() {
        return entityTag != null && entityTag.contains(SHOW_ARMS.NBT) && entityTag.getBoolean(SHOW_ARMS.NBT);
    }

    @Override
    public boolean isSmall() {
        return entityTag != null && entityTag.contains(SMALL.NBT) && entityTag.getBoolean(SMALL.NBT);
    }

    @Override
    public boolean isMarker() {
        return entityTag != null && entityTag.contains(MARKER.NBT) && entityTag.getBoolean(MARKER.NBT);
    }

    @Override
    public void setInvisible(boolean invisible) {
        populateTagIfNull();
        entityTag.putBoolean(INVISIBLE.NBT, invisible);
    }

    @Override
    public void setNoBasePlate(boolean noBasePlate) {
        populateTagIfNull();
        entityTag.putBoolean(NO_BASE_PLATE.NBT, noBasePlate);
    }

    @Override
    public void setShowArms(boolean showArms) {
        populateTagIfNull();
        entityTag.putBoolean(SHOW_ARMS.NBT, showArms);
    }

    @Override
    public void setSmall(boolean small) {
        populateTagIfNull();
        entityTag.putBoolean(SMALL.NBT, small);
    }

    @Override
    public void setMarker(boolean marker) {
        populateTagIfNull();
        entityTag.putBoolean(MARKER.NBT, marker);
    }
    // Paper end
}
