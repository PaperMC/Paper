package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityBlastFurnace;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlastFurnace;

public class CraftBlastFurnace extends CraftFurnace<TileEntityBlastFurnace> implements BlastFurnace {

    public CraftBlastFurnace(World world, TileEntityBlastFurnace tileEntity) {
        super(world, tileEntity);
    }

    protected CraftBlastFurnace(CraftBlastFurnace state, Location location) {
        super(state, location);
    }

    @Override
    public CraftBlastFurnace copy() {
        return new CraftBlastFurnace(this, null);
    }

    @Override
    public CraftBlastFurnace copy(Location location) {
        return new CraftBlastFurnace(this, location);
    }
}
