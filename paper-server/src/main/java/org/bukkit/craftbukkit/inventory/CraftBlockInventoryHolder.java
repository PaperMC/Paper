package org.bukkit.craftbukkit.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.LevelAccessor;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

public class CraftBlockInventoryHolder implements BlockInventoryHolder {

    private final Block block;
    private final Inventory inventory;

    public CraftBlockInventoryHolder(LevelAccessor world, BlockPos pos, Container inv) {
        this.block = CraftBlock.at(world, pos);
        this.inventory = new CraftInventory(inv);
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
