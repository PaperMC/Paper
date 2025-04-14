package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.MapItemColor;
import net.minecraft.world.item.component.MapPostProcessing;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaMap extends CraftMetaItem implements MapMeta {
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<MapPostProcessing> MAP_POST_PROCESSING = new ItemMetaKeyType<>(DataComponents.MAP_POST_PROCESSING);
    static final ItemMetaKey MAP_SCALING = new ItemMetaKey("scaling");
    @Deprecated // SPIGOT-6308
    static final ItemMetaKey MAP_LOC_NAME = new ItemMetaKey("display-loc-name");
    static final ItemMetaKeyType<MapItemColor> MAP_COLOR = new ItemMetaKeyType<>(DataComponents.MAP_COLOR, "display-map-color");
    static final ItemMetaKeyType<MapId> MAP_ID = new ItemMetaKeyType<>(DataComponents.MAP_ID, "map-id");
    static final byte SCALING_EMPTY = (byte) 0;
    static final byte SCALING_TRUE = (byte) 1;
    static final byte SCALING_FALSE = (byte) 2;

    private Integer mapId;
    private byte scaling = CraftMetaMap.SCALING_EMPTY;
    private Integer color; // Paper - keep color component consistent with vanilla (top byte is ignored)

    CraftMetaMap(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof final CraftMetaMap mapMeta)) {
            return;
        }

        this.mapId = mapMeta.mapId;
        this.scaling = mapMeta.scaling;
        this.color = mapMeta.color;
    }

    CraftMetaMap(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaMap.MAP_ID).ifPresent((mapId) -> {
            this.mapId = mapId.id();
        });

        getOrEmpty(tag, CraftMetaMap.MAP_POST_PROCESSING).ifPresent((mapPostProcessing) -> {
            this.scaling = (mapPostProcessing == MapPostProcessing.SCALE) ? CraftMetaMap.SCALING_TRUE : CraftMetaMap.SCALING_FALSE;
        });

        getOrEmpty(tag, CraftMetaMap.MAP_COLOR).ifPresent((mapColor) -> {
            try {
                this.color = mapColor.rgb();
            } catch (IllegalArgumentException ex) {
                // Invalid colour
            }
        });
    }

    CraftMetaMap(Map<String, Object> map) {
        super(map);

        Integer id = SerializableMeta.getObject(Integer.class, map, CraftMetaMap.MAP_ID.BUKKIT, true);
        if (id != null) {
            this.setMapId(id);
        }

        Boolean scaling = SerializableMeta.getObject(Boolean.class, map, CraftMetaMap.MAP_SCALING.BUKKIT, true);
        if (scaling != null) {
            this.setScaling(scaling);
        }

        String locName = SerializableMeta.getString(map, CraftMetaMap.MAP_LOC_NAME.BUKKIT, true);
        if (locName != null) {
            this.setLocationName(locName);
        }

        Color color = SerializableMeta.getObject(Color.class, map, CraftMetaMap.MAP_COLOR.BUKKIT, true);
        if (color != null) {
            this.setColor(color);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasMapId()) {
            tag.put(CraftMetaMap.MAP_ID, new MapId(this.getMapId()));
        }

        if (this.hasScaling()) {
            tag.put(CraftMetaMap.MAP_POST_PROCESSING, (this.isScaling()) ? MapPostProcessing.SCALE : MapPostProcessing.LOCK);
        }

        if (this.hasColor()) {
            tag.put(CraftMetaMap.MAP_COLOR, new MapItemColor(this.color));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isMapEmpty();
    }

    boolean isMapEmpty() {
        return !(this.hasMapId() || this.hasScaling() | this.hasLocationName() || this.hasColor());
    }

    @Override
    public boolean hasMapId() {
        return this.mapId != null;
    }

    @Override
    public int getMapId() {
        Preconditions.checkState(this.hasMapId(), "Item does not have map associated - check hasMapId() first!");
        return this.mapId;
    }

    @Override
    public void setMapId(int id) {
        this.mapId = id;
    }

    @Override
    public boolean hasMapView() {
        return this.mapId != null;
    }

    @Override
    public MapView getMapView() {
        Preconditions.checkState(this.hasMapView(), "Item does not have map associated - check hasMapView() first!");
        return Bukkit.getMap(this.mapId);
    }

    @Override
    public void setMapView(MapView map) {
        this.mapId = (map != null) ? map.getId() : null;
    }

    boolean hasScaling() {
        return this.scaling != CraftMetaMap.SCALING_EMPTY;
    }

    @Override
    public boolean isScaling() {
        return this.scaling == CraftMetaMap.SCALING_TRUE;
    }

    @Override
    public void setScaling(boolean scaling) {
        this.scaling = scaling ? CraftMetaMap.SCALING_TRUE : CraftMetaMap.SCALING_FALSE;
    }

    @Override
    public boolean hasLocationName() {
        return this.hasLocalizedName(); // SPIGOT-6308
    }

    @Override
    public String getLocationName() {
        return this.getLocalizedName(); // SPIGOT-6308
    }

    @Override
    public void setLocationName(String name) {
        this.setLocalizedName(name); // SPIGOT-6308
    }

    @Override
    public boolean hasColor() {
        return this.color != null;
    }

    @Override
    public Color getColor() {
        return this.color == null ? null : Color.fromRGB(this.color & 0x00FFFFFF);
    }

    @Override
    public void setColor(Color color) {
        this.color = color == null ? null : color.asRGB();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaMap other) {
            return this.scaling == other.scaling
                    && Objects.equals(this.mapId, other.mapId)
                    && Objects.equals(this.color, other.color);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaMap || this.isMapEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasMapId()) {
            hash = 61 * hash + this.mapId.hashCode();
        }
        if (this.hasScaling()) {
            hash ^= 0x22222222 << (this.isScaling() ? 1 : -1);
        }
        if (this.hasColor()) {
            hash = 61 * hash + this.color.hashCode();
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

        if (this.hasMapId()) {
            builder.put(CraftMetaMap.MAP_ID.BUKKIT, this.getMapId());
        }

        if (this.hasScaling()) {
            builder.put(CraftMetaMap.MAP_SCALING.BUKKIT, this.isScaling());
        }

        if (this.hasColor()) {
            builder.put(CraftMetaMap.MAP_COLOR.BUKKIT, this.getColor());
        }

        return builder;
    }
}
