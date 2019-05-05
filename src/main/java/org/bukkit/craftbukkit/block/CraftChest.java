package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockChest;
import net.minecraft.server.Blocks;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.TileEntityChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftLootable<TileEntityChest> implements Chest {

    public CraftChest(final Block block) {
        super(block, TileEntityChest.class);
    }

    public CraftChest(final Material material, final TileEntityChest te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getBlockInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = (CraftInventory) this.getBlockInventory();
        if (!isPlaced()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in BlockChest.interact
        CraftWorld world = (CraftWorld) this.getWorld();

        BlockChest blockChest = (BlockChest) (this.getType() == Material.CHEST ? Blocks.CHEST : Blocks.TRAPPED_CHEST);
        ITileInventory nms = blockChest.getInventory(data, world.getHandle(), this.getPosition());

        if (nms instanceof BlockChest.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((BlockChest.DoubleInventory) nms);
        }
        return inventory;
    }
}
