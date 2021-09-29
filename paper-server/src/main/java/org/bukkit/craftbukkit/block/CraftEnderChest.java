package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityEnderChest;
import org.bukkit.World;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<TileEntityEnderChest> implements EnderChest {

    public CraftEnderChest(World world, TileEntityEnderChest tileEntity) {
        super(world, tileEntity);
    }
}
