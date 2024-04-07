package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityLightDetector;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<TileEntityLightDetector> implements DaylightDetector {

    public CraftDaylightDetector(World world, TileEntityLightDetector tileEntity) {
        super(world, tileEntity);
    }

    protected CraftDaylightDetector(CraftDaylightDetector state, Location location) {
        super(state, location);
    }

    @Override
    public CraftDaylightDetector copy() {
        return new CraftDaylightDetector(this, null);
    }

    @Override
    public CraftDaylightDetector copy(Location location) {
        return new CraftDaylightDetector(this, location);
    }
}
