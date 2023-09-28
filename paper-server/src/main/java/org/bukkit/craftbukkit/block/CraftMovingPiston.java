package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.piston.TileEntityPiston;
import org.bukkit.World;

public class CraftMovingPiston extends CraftBlockEntityState<TileEntityPiston> {

    public CraftMovingPiston(World world, TileEntityPiston tileEntity) {
        super(world, tileEntity);
    }

    protected CraftMovingPiston(CraftMovingPiston state) {
        super(state);
    }

    @Override
    public CraftMovingPiston copy() {
        return new CraftMovingPiston(this);
    }
}
