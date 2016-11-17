package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagString;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.inventory.meta.MapMeta;

import com.google.common.collect.ImmutableMap;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaMap extends CraftMetaItem implements MapMeta {
    static final ItemMetaKey MAP_SCALING = new ItemMetaKey("map_is_scaling", "scaling");
    static final ItemMetaKey MAP_LOC_NAME = new ItemMetaKey("LocName", "display-loc-name");
    static final ItemMetaKey MAP_COLOR = new ItemMetaKey("Mapcolor", "display-map-color");
    static final byte SCALING_EMPTY = (byte) 0;
    static final byte SCALING_TRUE = (byte) 1;
    static final byte SCALING_FALSE = (byte) 2;

    private byte scaling = SCALING_EMPTY;
    private String locName;
    private String mapColor;

    CraftMetaMap(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaMap)) {
            return;
        }

        CraftMetaMap map = (CraftMetaMap) meta;
        this.scaling = map.scaling;
    }

    CraftMetaMap(NBTTagCompound tag) {
        super(tag);

        if (tag.hasKey(MAP_SCALING.NBT)) {
            this.scaling = tag.getBoolean(MAP_SCALING.NBT) ? SCALING_TRUE : SCALING_FALSE;
        }
        
        if (tag.hasKey(DISPLAY.NBT)) {
            NBTTagCompound display = tag.getCompound(DISPLAY.NBT);

            if (display.hasKey(MAP_LOC_NAME.NBT)) {
                locName = display.getString(MAP_LOC_NAME.NBT);
            }

            if (display.hasKey(MAP_COLOR.NBT)) {
                mapColor = display.getString(MAP_COLOR.NBT);
            }
        }
    }

    CraftMetaMap(Map<String, Object> map) {
        super(map);

        Boolean scaling = SerializableMeta.getObject(Boolean.class, map, MAP_SCALING.BUKKIT, true);
        if (scaling != null) {
            setScaling(scaling);
        }
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (hasScaling()) {
            tag.setBoolean(MAP_SCALING.NBT, isScaling());
        }

        if (hasLocationName()) {
            setDisplayTag(tag, MAP_LOC_NAME.NBT, new NBTTagString(getLocationName()));
        }

        if (hasMapColor()) {
            setDisplayTag(tag, MAP_COLOR.NBT, new NBTTagString(mapColor));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case MAP:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isMapEmpty();
    }

    boolean isMapEmpty() {
        return !hasScaling();
    }

    boolean hasScaling() {
        return scaling != SCALING_EMPTY;
    }

    public boolean isScaling() {
        return scaling == SCALING_TRUE;
    }

    public void setScaling(boolean scaling) {
        this.scaling = scaling ? SCALING_TRUE : SCALING_FALSE;
    }

    @Override
    public boolean hasLocationName() {
        return this.locName != null;
    }

    @Override
    public String getLocationName() {
        return this.locName;
    }

    @Override
    public void setLocationName(String name) {
        this.locName = name;
    }

    public boolean hasMapColor() {
        return this.mapColor != null;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaMap) {
            CraftMetaMap that = (CraftMetaMap) meta;

            return (this.scaling == that.scaling)
                    && (hasLocationName() ? that.hasLocationName() && this.getLocationName().equals(that.getLocationName()) : !that.hasLocationName())
                    && (hasMapColor() ? that.hasMapColor() && this.mapColor.equals(that.mapColor) : !that.hasMapColor());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaMap || isMapEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasScaling()) {
            hash ^= 0x22222222 << (isScaling() ? 1 : -1);
        }

        return original != hash ? CraftMetaMap.class.hashCode() ^ hash : hash;
    }

    public CraftMetaMap clone() {
        return (CraftMetaMap) super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasScaling()) {
            builder.put(MAP_SCALING.BUKKIT, isScaling());
        }

        if (hasLocationName()) {
            builder.put(MAP_LOC_NAME.BUKKIT, getLocationName());
        }

        if (hasMapColor()) {
            builder.put(MAP_COLOR.BUKKIT, mapColor);
        }

        return builder;
    }
}
