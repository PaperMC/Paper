package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.component.LodestoneTracker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.inventory.meta.CompassMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaCompass extends CraftMetaItem implements CompassMeta {

    static final ItemMetaKeyType<LodestoneTracker> LODESTONE_TARGET = new ItemMetaKeyType<>(DataComponents.LODESTONE_TRACKER, "LodestoneDimension");
    static final ItemMetaKey LODESTONE_POS = new ItemMetaKey("lodestone");
    static final ItemMetaKey LODESTONE_POS_WORLD = new ItemMetaKey("LodestonePosWorld");
    static final ItemMetaKey LODESTONE_POS_X = new ItemMetaKey("LodestonePosX");
    static final ItemMetaKey LODESTONE_POS_Y = new ItemMetaKey("LodestonePosY");
    static final ItemMetaKey LODESTONE_POS_Z = new ItemMetaKey("LodestonePosZ");
    static final ItemMetaKey LODESTONE_TRACKED = new ItemMetaKey("LodestoneTracked");

    private ResourceKey<net.minecraft.world.level.Level> lodestoneWorld;
    private int lodestoneX;
    private int lodestoneY;
    private int lodestoneZ;
    private boolean tracked = true;

    CraftMetaCompass(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaCompass)) {
            return;
        }
        CraftMetaCompass compassMeta = (CraftMetaCompass) meta;
        this.lodestoneWorld = compassMeta.lodestoneWorld;
        this.lodestoneX = compassMeta.lodestoneX;
        this.lodestoneY = compassMeta.lodestoneY;
        this.lodestoneZ = compassMeta.lodestoneZ;
        this.tracked = compassMeta.tracked;
    }

    CraftMetaCompass(DataComponentPatch tag) {
        super(tag);
        getOrEmpty(tag, CraftMetaCompass.LODESTONE_TARGET).ifPresent((lodestoneTarget) -> {
            lodestoneTarget.target().ifPresent((target) -> {
                this.lodestoneWorld = target.dimension();
                BlockPos pos = target.pos();
                this.lodestoneX = pos.getX();
                this.lodestoneY = pos.getY();
                this.lodestoneZ = pos.getZ();
            });
            this.tracked = lodestoneTarget.tracked();
        });
    }

    CraftMetaCompass(Map<String, Object> map) {
        super(map);
        String lodestoneWorldString = SerializableMeta.getString(map, CraftMetaCompass.LODESTONE_POS_WORLD.BUKKIT, true);
        if (lodestoneWorldString != null) {
            this.lodestoneWorld = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(lodestoneWorldString));
            this.lodestoneX = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_X.BUKKIT);
            this.lodestoneY = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_Y.BUKKIT);
            this.lodestoneZ = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_Z.BUKKIT);
        } else {
            // legacy
            Location lodestone = SerializableMeta.getObject(Location.class, map, CraftMetaCompass.LODESTONE_POS.BUKKIT, true);
            if (lodestone != null && lodestone.getWorld() != null) {
                this.setLodestone(lodestone);
            }
        }
        this.tracked = SerializableMeta.getBoolean(map, CraftMetaCompass.LODESTONE_TRACKED.BUKKIT);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        Optional<GlobalPos> target = Optional.empty();
        if (this.lodestoneWorld != null) {
            target = Optional.of(new GlobalPos(this.lodestoneWorld, new BlockPos(this.lodestoneX, this.lodestoneY, this.lodestoneZ)));
        }

        if (target.isPresent() || this.hasLodestoneTracked()) {
            tag.put(CraftMetaCompass.LODESTONE_TARGET, new LodestoneTracker(target, this.tracked));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isCompassEmpty();
    }

    boolean isCompassEmpty() {
        return !(this.hasLodestone() || this.hasLodestoneTracked());
    }

    @Override
    public CraftMetaCompass clone() {
        CraftMetaCompass clone = ((CraftMetaCompass) super.clone());
        return clone;
    }

    @Override
    public boolean hasLodestone() {
        return this.lodestoneWorld != null;
    }

    @Override
    public Location getLodestone() {
        if (this.lodestoneWorld == null) {
            return null;
        }
        ServerLevel worldServer = MinecraftServer.getServer().getLevel(this.lodestoneWorld);
        World world = worldServer != null ? worldServer.getWorld() : null;
        return new Location(world, this.lodestoneX, this.lodestoneY, this.lodestoneZ); // world may be null here, if the referenced world is not loaded
    }

    @Override
    public void setLodestone(Location lodestone) {
        Preconditions.checkArgument(lodestone == null || lodestone.getWorld() != null, "world is null");
        if (lodestone == null) {
            this.lodestoneWorld = null;
        } else {
            this.lodestoneWorld = ((CraftWorld) lodestone.getWorld()).getHandle().dimension();
            this.lodestoneX = lodestone.getBlockX();
            this.lodestoneY = lodestone.getBlockY();
            this.lodestoneZ = lodestone.getBlockZ();
        }
    }

    boolean hasLodestoneTracked() {
        return !this.tracked;
    }

    @Override
    public boolean isLodestoneTracked() {
        return this.tracked;
    }

    @Override
    public void setLodestoneTracked(boolean tracked) {
        this.tracked = tracked;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasLodestone()) {
            hash = 73 * hash + this.lodestoneWorld.hashCode();
            hash = 73 * hash + this.lodestoneX;
            hash = 73 * hash + this.lodestoneY;
            hash = 73 * hash + this.lodestoneZ;
        }
        if (this.hasLodestoneTracked()) {
            hash = 73 * hash + (this.isLodestoneTracked() ? 1231 : 1237);
        }

        return original != hash ? CraftMetaCompass.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCompass) {
            CraftMetaCompass that = (CraftMetaCompass) meta;

            return (this.hasLodestone() ? that.hasLodestone() && this.lodestoneWorld.equals(that.lodestoneWorld)
                    && this.lodestoneX == that.lodestoneX && this.lodestoneY == that.lodestoneY
                    && this.lodestoneZ == that.lodestoneZ : !that.hasLodestone())
                    && this.tracked == that.tracked;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCompass || this.isCompassEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasLodestone()) {
            builder.put(CraftMetaCompass.LODESTONE_POS_WORLD.BUKKIT, this.lodestoneWorld.location().toString());
            builder.put(CraftMetaCompass.LODESTONE_POS_X.BUKKIT, this.lodestoneX);
            builder.put(CraftMetaCompass.LODESTONE_POS_Y.BUKKIT, this.lodestoneY);
            builder.put(CraftMetaCompass.LODESTONE_POS_Z.BUKKIT, this.lodestoneZ);
        }
        if (this.hasLodestoneTracked()) {
            builder.put(CraftMetaCompass.LODESTONE_TRACKED.BUKKIT, this.tracked);
        }

        return builder;
    }
}
