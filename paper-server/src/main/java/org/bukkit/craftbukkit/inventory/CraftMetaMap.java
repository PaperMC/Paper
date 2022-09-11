package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaMap extends CraftMetaItem implements MapMeta {
    static final ItemMetaKey MAP_SCALING = new ItemMetaKey("map_is_scaling", "scaling");
    static final ItemMetaKey MAP_LOC_NAME = new ItemMetaKey("LocName", "display-loc-name");
    static final ItemMetaKey MAP_COLOR = new ItemMetaKey("MapColor", "display-map-color");
    static final ItemMetaKey MAP_ID = new ItemMetaKey("map", "map-id");
    static final byte SCALING_EMPTY = (byte) 0;
    static final byte SCALING_TRUE = (byte) 1;
    static final byte SCALING_FALSE = (byte) 2;

    private Integer mapId;
    private byte scaling = SCALING_EMPTY;
    private String locName;
    private Color color;

    CraftMetaMap(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaMap)) {
            return;
        }

        CraftMetaMap map = (CraftMetaMap) meta;
        this.mapId = map.mapId;
        this.scaling = map.scaling;
        this.locName = map.locName;
        this.color = map.color;
    }

    CraftMetaMap(NBTTagCompound tag) {
        super(tag);

        if (tag.contains(MAP_ID.NBT, CraftMagicNumbers.NBT.TAG_ANY_NUMBER)) {
            this.mapId = tag.getInt(MAP_ID.NBT);
        }

        if (tag.contains(MAP_SCALING.NBT)) {
            this.scaling = tag.getBoolean(MAP_SCALING.NBT) ? SCALING_TRUE : SCALING_FALSE;
        }

        if (tag.contains(DISPLAY.NBT)) {
            NBTTagCompound display = tag.getCompound(DISPLAY.NBT);

            if (display.contains(MAP_LOC_NAME.NBT)) {
                locName = display.getString(MAP_LOC_NAME.NBT);
            }

            if (display.contains(MAP_COLOR.NBT)) {
                try {
                    color = Color.fromRGB(display.getInt(MAP_COLOR.NBT));
                } catch (IllegalArgumentException ex) {
                    // Invalid colour
                }
            }
        }
    }

    CraftMetaMap(Map<String, Object> map) {
        super(map);

        Integer id = SerializableMeta.getObject(Integer.class, map, MAP_ID.BUKKIT, true);
        if (id != null) {
            setMapId(id);
        }

        Boolean scaling = SerializableMeta.getObject(Boolean.class, map, MAP_SCALING.BUKKIT, true);
        if (scaling != null) {
            setScaling(scaling);
        }

        String locName = SerializableMeta.getString(map, MAP_LOC_NAME.BUKKIT, true);
        if (locName != null) {
            setLocationName(locName);
        }

        Color color = SerializableMeta.getObject(Color.class, map, MAP_COLOR.BUKKIT, true);
        if (color != null) {
            setColor(color);
        }
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (hasMapId()) {
            tag.putInt(MAP_ID.NBT, getMapId());
        }

        if (hasScaling()) {
            tag.putBoolean(MAP_SCALING.NBT, isScaling());
        }

        if (hasLocationName()) {
            setDisplayTag(tag, MAP_LOC_NAME.NBT, NBTTagString.valueOf(getLocationName()));
        }

        if (hasColor()) {
            setDisplayTag(tag, MAP_COLOR.NBT, NBTTagInt.valueOf(color.asRGB()));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.FILLED_MAP;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isMapEmpty();
    }

    boolean isMapEmpty() {
        return !(hasMapId() || hasScaling() | hasLocationName() || hasColor());
    }

    @Override
    public boolean hasMapId() {
        return mapId != null;
    }

    @Override
    public int getMapId() {
        return mapId;
    }

    @Override
    public void setMapId(int id) {
        this.mapId = id;
    }

    @Override
    public boolean hasMapView() {
        return mapId != null;
    }

    @Override
    public MapView getMapView() {
        Preconditions.checkState(hasMapView(), "Item does not have map associated - check hasMapView() first!");
        return Bukkit.getMap(mapId);
    }

    @Override
    public void setMapView(MapView map) {
        this.mapId = (map != null) ? map.getId() : null;
    }

    boolean hasScaling() {
        return scaling != SCALING_EMPTY;
    }

    @Override
    public boolean isScaling() {
        return scaling == SCALING_TRUE;
    }

    @Override
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

    @Override
    public boolean hasColor() {
        return this.color != null;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaMap) {
            CraftMetaMap that = (CraftMetaMap) meta;

            return (this.scaling == that.scaling)
                    && (hasMapId() ? that.hasMapId() && this.mapId.equals(that.mapId) : !that.hasMapId())
                    && (hasLocationName() ? that.hasLocationName() && this.locName.equals(that.locName) : !that.hasLocationName())
                    && (hasColor() ? that.hasColor() && this.color.equals(that.color) : !that.hasColor());
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

        if (hasMapId()) {
            hash = 61 * hash + mapId.hashCode();
        }
        if (hasScaling()) {
            hash ^= 0x22222222 << (isScaling() ? 1 : -1);
        }
        if (hasLocationName()) {
            hash = 61 * hash + locName.hashCode();
        }
        if (hasColor()) {
            hash = 61 * hash + color.hashCode();
        }

        return original != hash ? CraftMetaMap.class.hashCode() ^ hash : hash;
    }


    @Override
    public CraftMetaMap clone() {
        return (CraftMetaMap) super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasMapId()) {
            builder.put(MAP_ID.BUKKIT, getMapId());
        }

        if (hasScaling()) {
            builder.put(MAP_SCALING.BUKKIT, isScaling());
        }

        if (hasLocationName()) {
            builder.put(MAP_LOC_NAME.BUKKIT, getLocationName());
        }

        if (hasColor()) {
            builder.put(MAP_COLOR.BUKKIT, getColor());
        }

        return builder;
    }
}
