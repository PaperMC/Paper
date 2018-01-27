package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaArmorStand extends CraftMetaItem implements com.destroystokyo.paper.inventory.meta.ArmorStandMeta { // Paper

    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");
    // Paper start
    static final ItemMetaKey INVISIBLE = new ItemMetaKey("Invisible", "invisible");
    static final ItemMetaKey NO_BASE_PLATE = new ItemMetaKey("NoBasePlate", "no-base-plate");
    static final ItemMetaKey SHOW_ARMS = new ItemMetaKey("ShowArms", "show-arms");
    static final ItemMetaKey SMALL = new ItemMetaKey("Small", "small");
    static final ItemMetaKey MARKER = new ItemMetaKey("Marker", "marker");

    private boolean invisible;
    private boolean noBasePlate;
    private boolean showArms;
    private boolean small;
    private boolean marker;
    // Paper end
    NBTTagCompound entityTag;

    CraftMetaArmorStand(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaArmorStand)) {
            return;
        }

        CraftMetaArmorStand armorStand = (CraftMetaArmorStand) meta;
        // Paper start
        this.invisible = armorStand.invisible;
        this.noBasePlate = armorStand.noBasePlate;
        this.showArms = armorStand.showArms;
        this.small = armorStand.small;
        this.marker = armorStand.marker;
        // Paper end
        this.entityTag = armorStand.entityTag;
    }

    CraftMetaArmorStand(NBTTagCompound tag) {
        super(tag);

        if (tag.hasKey(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompound(ENTITY_TAG.NBT);

            // Paper start
            if (entityTag.hasKey(INVISIBLE.NBT)) {
                invisible = entityTag.getBoolean(INVISIBLE.NBT);
            }

            if (entityTag.hasKey(NO_BASE_PLATE.NBT)) {
                noBasePlate = entityTag.getBoolean(NO_BASE_PLATE.NBT);
            }

            if (entityTag.hasKey(SHOW_ARMS.NBT)) {
                showArms = entityTag.getBoolean(SHOW_ARMS.NBT);
            }

            if (entityTag.hasKey(SMALL.NBT)) {
                small = entityTag.getBoolean(SMALL.NBT);
            }

            if (entityTag.hasKey(MARKER.NBT)) {
                marker = entityTag.getBoolean(MARKER.NBT);
            }
            // Paper end
        }
    }

    CraftMetaArmorStand(Map<String, Object> map) {
        super(map);

        // Paper start
        boolean invis = SerializableMeta.getBoolean(map, INVISIBLE.BUKKIT);
        boolean noBase = SerializableMeta.getBoolean(map, NO_BASE_PLATE.BUKKIT);
        boolean showArms = SerializableMeta.getBoolean(map, SHOW_ARMS.BUKKIT);
        boolean small = SerializableMeta.getBoolean(map, SMALL.BUKKIT);
        boolean marker = SerializableMeta.getBoolean(map, MARKER.BUKKIT);

        this.invisible = invis;
        this.noBasePlate = noBase;
        this.showArms = showArms;
        this.small = small;
        this.marker = marker;
        // Paper end
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.hasKey(ENTITY_TAG.NBT)) {
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
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        // Paper start
        if (!isArmorStandEmpty() && entityTag == null) {
            entityTag = new NBTTagCompound();
        }

        if (isInvisible()) {
            entityTag.setBoolean(INVISIBLE.NBT, invisible);
        }

        if (hasNoBasePlate()) {
            entityTag.setBoolean(NO_BASE_PLATE.NBT, noBasePlate);
        }

        if (shouldShowArms()) {
            entityTag.setBoolean(SHOW_ARMS.NBT, showArms);
        }

        if (isSmall()) {
            entityTag.setBoolean(SMALL.NBT, small);
        }

        if (isMarker()) {
            entityTag.setBoolean(MARKER.NBT, marker);
        }
        // Paper end

        if (entityTag != null) {
            tag.set(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case ARMOR_STAND:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isArmorStandEmpty();
    }

    boolean isArmorStandEmpty() {
        return !(isInvisible() || hasNoBasePlate() || shouldShowArms() || isSmall() || isMarker() || entityTag != null);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaArmorStand) {
            CraftMetaArmorStand that = (CraftMetaArmorStand) meta;

            // Paper start
            return invisible == that.invisible &&
                noBasePlate == that.noBasePlate &&
                showArms == that.showArms &&
                small == that.small &&
                marker == that.marker;
            // Paper end
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmorStand || isArmorStandEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        // Paper start
        hash += entityTag != null ? 73 * hash + entityTag.hashCode() : 0;
        hash += isInvisible() ? 61 * hash + 1231 : 0;
        hash += hasNoBasePlate() ? 61 * hash + 1231 : 0;
        hash += shouldShowArms() ? 61 * hash + 1231 : 0;
        hash += isSmall() ? 61 * hash + 1231 : 0;
        hash += isMarker() ? 61 * hash + 1231 : 0;
        // Paper end

        return original != hash ? CraftMetaArmorStand.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        // Paper start
        if (isInvisible()) {
            builder.put(INVISIBLE.BUKKIT, invisible);
        }

        if (hasNoBasePlate()) {
            builder.put(NO_BASE_PLATE.BUKKIT, noBasePlate);
        }

        if (shouldShowArms()) {
            builder.put(SHOW_ARMS.BUKKIT, showArms);
        }

        if (isSmall()) {
            builder.put(SMALL.BUKKIT, small);
        }

        if (isMarker()) {
            builder.put(MARKER.BUKKIT, marker);
        }
        // Paper end

        return builder;
    }

    @Override
    public CraftMetaArmorStand clone() {
        CraftMetaArmorStand clone = (CraftMetaArmorStand) super.clone();

        if (entityTag != null) {
            clone.entityTag = entityTag.clone();
        }

        return clone;
    }

    // Paper start
    @Override
    public boolean isInvisible() {
        return invisible;
    }

    @Override
    public boolean hasNoBasePlate() {
        return noBasePlate;
    }

    @Override
    public boolean shouldShowArms() {
        return showArms;
    }

    @Override
    public boolean isSmall() {
        return small;
    }

    @Override
    public boolean isMarker() {
        return marker;
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    @Override
    public void setNoBasePlate(boolean noBasePlate) {
        this.noBasePlate = noBasePlate;
    }

    @Override
    public void setShowArms(boolean showArms) {
        this.showArms = showArms;
    }

    @Override
    public void setSmall(boolean small) {
        this.small = small;
    }

    @Override
    public void setMarker(boolean marker) {
        this.marker = marker;
    }
    // Paper end
}
