package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.TileEntityBeehive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Beehive;

public class CraftBeehive extends CraftBlockEntityState<TileEntityBeehive> implements Beehive {

    public CraftBeehive(final Block block) {
        super(block, TileEntityBeehive.class);
    }

    public CraftBeehive(final Material material, final TileEntityBeehive te) {
        super(material, te);
    }

    @Override
    public Location getFlower() {
        BlockPosition flower = getSnapshot().flowerPos;
        return (flower == null) ? null : new Location(getWorld(), flower.getX(), flower.getY(), flower.getZ());
    }

    @Override
    public void setFlower(Location location) {
        Preconditions.checkArgument(location == null || this.getWorld().equals(location.getWorld()), "Flower must be in same world");
        getSnapshot().flowerPos = (location == null) ? null : new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
