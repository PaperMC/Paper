package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.DataResult;
import java.util.Map;
import java.util.Optional;
import net.minecraft.server.DynamicOpsNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.ResourceKey;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.inventory.meta.CompassMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaCompass extends CraftMetaItem implements CompassMeta {

    static final ItemMetaKey LODESTONE_DIMENSION = new ItemMetaKey("LodestoneDimension");
    static final ItemMetaKey LODESTONE_POS = new ItemMetaKey("LodestonePos", "lodestone");
    static final ItemMetaKey LODESTONE_TRACKED = new ItemMetaKey("LodestoneTracked");

    private Location lodestone;
    private Boolean tracked;

    CraftMetaCompass(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaCompass)) {
            return;
        }
        CraftMetaCompass compassMeta = (CraftMetaCompass) meta;
        lodestone = compassMeta.lodestone.clone();
        tracked = compassMeta.tracked;
    }

    CraftMetaCompass(NBTTagCompound tag) {
        super(tag);
        if (tag.hasKey(LODESTONE_DIMENSION.NBT) && tag.hasKey(LODESTONE_POS.NBT)) {
            Optional<ResourceKey<net.minecraft.server.World>> key = net.minecraft.server.World.f.parse(DynamicOpsNBT.a, tag.get(LODESTONE_DIMENSION.NBT)).result();

            if (key.isPresent()) {
                World world = MinecraftServer.getServer().getWorldServer(key.get()).getWorld();

                NBTTagCompound pos = tag.getCompound(LODESTONE_POS.NBT);
                lodestone = new Location(world, pos.getInt("X"), pos.getInt("Y"), pos.getInt("Z"));
            }
        }
        if (tag.hasKey(LODESTONE_TRACKED.NBT)) {
            tracked = tag.getBoolean(LODESTONE_TRACKED.NBT);
        }
    }

    CraftMetaCompass(Map<String, Object> map) {
        super(map);

        lodestone = SerializableMeta.getObject(Location.class, map, LODESTONE_POS.BUKKIT, true);
        tracked = SerializableMeta.getBoolean(map, LODESTONE_TRACKED.BUKKIT);
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (lodestone != null) {
            NBTTagCompound pos = new NBTTagCompound();
            pos.setInt("X", lodestone.getBlockX());
            pos.setInt("Y", lodestone.getBlockY());
            pos.setInt("Z", lodestone.getBlockZ());

            tag.set(LODESTONE_POS.NBT, pos);

            ResourceKey<net.minecraft.server.World> key = ((CraftWorld) lodestone.getWorld()).getHandle().getDimensionKey();
            DataResult<NBTBase> dataresult = net.minecraft.server.World.f.encodeStart(DynamicOpsNBT.a, key);
            tag.set(LODESTONE_DIMENSION.NBT, dataresult.get().orThrow());
        }

        if (tracked != null) {
            tag.setBoolean(LODESTONE_TRACKED.NBT, tracked);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isCompassEmpty();
    }

    boolean isCompassEmpty() {
        return !(hasLodestone() || hasLodestoneTracked());
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.COMPASS;
    }

    @Override
    public CraftMetaCompass clone() {
        CraftMetaCompass clone = ((CraftMetaCompass) super.clone());
        clone.lodestone = (lodestone == null) ? null : lodestone.clone();
        clone.tracked = tracked;
        return clone;
    }

    @Override
    public boolean hasLodestone() {
        return lodestone != null;
    }

    @Override
    public Location getLodestone() {
        return lodestone;
    }

    @Override
    public void setLodestone(Location lodestone) {
        Preconditions.checkArgument(lodestone != null && lodestone.getWorld() != null, "Location or world null");
        this.lodestone = lodestone;
    }

    boolean hasLodestoneTracked() {
        return tracked != null;
    }

    @Override
    public boolean isLodestoneTracked() {
        return hasLodestoneTracked() && tracked;
    }

    @Override
    public void setLodestoneTracked(boolean tracked) {
        this.tracked = tracked;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasLodestone()) {
            hash = 73 * hash + lodestone.hashCode();
        }
        if (hasLodestoneTracked()) {
            hash = 73 * hash + (isLodestoneTracked() ? 1231 : 1237);
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

            return (this.hasLodestone() ? that.hasLodestone() && this.lodestone.equals(that.lodestone) : !that.hasLodestone())
                    && this.tracked == that.tracked;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCompass || isCompassEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasLodestone()) {
            builder.put(LODESTONE_POS.BUKKIT, lodestone);
        }
        if (hasLodestoneTracked()) {
            builder.put(LODESTONE_TRACKED.BUKKIT, tracked);
        }

        return builder;
    }
}
