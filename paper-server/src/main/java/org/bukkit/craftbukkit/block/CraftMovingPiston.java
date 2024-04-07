package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.piston.TileEntityPiston;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftMovingPiston extends CraftBlockEntityState<TileEntityPiston> {

    public CraftMovingPiston(World world, TileEntityPiston tileEntity) {
        super(world, tileEntity);
    }

    protected CraftMovingPiston(CraftMovingPiston state, Location location) {
        super(state, location);
    }

    @Override
    public CraftMovingPiston copy() {
        return new CraftMovingPiston(this, null);
    }

    @Override
    public CraftMovingPiston copy(Location location) {
        return new CraftMovingPiston(this, location);
    }
}
