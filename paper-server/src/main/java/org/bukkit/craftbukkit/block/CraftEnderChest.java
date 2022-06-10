package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityEnderChest;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.World;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<TileEntityEnderChest> implements EnderChest {

    public CraftEnderChest(World world, TileEntityEnderChest tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.World) {
            IBlockData block = getTileEntity().getBlockState();
            int openCount = getTileEntity().openersCounter.getOpenerCount();

            getTileEntity().openersCounter.onAPIOpen((net.minecraft.world.level.World) getWorldHandle(), getPosition(), block);
            getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.World) getWorldHandle(), getPosition(), block, openCount, openCount + 1);
        }
        getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.World) {
            IBlockData block = getTileEntity().getBlockState();
            int openCount = getTileEntity().openersCounter.getOpenerCount();

            getTileEntity().openersCounter.onAPIClose((net.minecraft.world.level.World) getWorldHandle(), getPosition(), block);
            getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.World) getWorldHandle(), getPosition(), block, openCount, 0);
        }
        getTileEntity().openersCounter.opened = false;
    }
}
