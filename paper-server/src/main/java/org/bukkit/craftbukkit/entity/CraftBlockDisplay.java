package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.BlockDisplay;

public class CraftBlockDisplay extends CraftDisplay implements BlockDisplay {

    public CraftBlockDisplay(CraftServer server, net.minecraft.world.entity.Display.BlockDisplay entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Display.BlockDisplay getHandle() {
        return (net.minecraft.world.entity.Display.BlockDisplay) this.entity;
    }

    @Override
    public BlockData getBlock() {
        return CraftBlockData.fromData(this.getHandle().getBlockState());
    }

    @Override
    public void setBlock(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        this.getHandle().setBlockState(((CraftBlockData) block).getState());
    }
}
