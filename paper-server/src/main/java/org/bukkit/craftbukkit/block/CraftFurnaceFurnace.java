package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityFurnaceFurnace;
import org.bukkit.World;

public class CraftFurnaceFurnace extends CraftFurnace<TileEntityFurnaceFurnace> {

    public CraftFurnaceFurnace(World world, TileEntityFurnaceFurnace tileEntity) {
        super(world, tileEntity);
    }
}
