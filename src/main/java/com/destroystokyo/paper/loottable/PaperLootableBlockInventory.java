package com.destroystokyo.paper.loottable;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.TileEntityLootable;
import net.minecraft.server.World;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public interface PaperLootableBlockInventory extends LootableBlockInventory, PaperLootableInventory {

    TileEntityLootable getTileEntity();

    @Override
    default LootableInventory getAPILootableInventory() {
        return this;
    }

    @Override
    default World getNMSWorld() {
        return getTileEntity().getWorld();
    }

    default Block getBlock() {
        final BlockPosition position = getTileEntity().getPosition();
        final Chunk bukkitChunk = getTileEntity().getWorld().getChunkAtWorldCoords(position).bukkitChunk;
        return bukkitChunk.getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    default PaperLootableInventoryData getLootableData() {
        return getTileEntity().lootableData;
    }
}
