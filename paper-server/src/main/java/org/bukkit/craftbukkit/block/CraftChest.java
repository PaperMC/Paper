package org.bukkit.craftbukkit.block;

import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.ITileInventory;
import net.minecraft.world.level.block.BlockChest;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntityChest;
import net.minecraft.world.level.block.state.IBlockData;
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

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().openersCounter.opened) {
            IBlockData block = getTileEntity().getBlock();
            getTileEntity().getWorld().playBlockAction(getPosition(), block.getBlock(), 1, getTileEntity().openersCounter.getOpenerCount() + 1);
            TileEntityChest.playOpenSound(getTileEntity().getWorld(), getPosition(), block, SoundEffects.CHEST_OPEN);
        }
        getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().openersCounter.opened) {
            IBlockData block = getTileEntity().getBlock();
            getTileEntity().getWorld().playBlockAction(getPosition(), block.getBlock(), 1, 0);
            TileEntityChest.playOpenSound(getTileEntity().getWorld(), getPosition(), block, SoundEffects.CHEST_OPEN);
        }
        getTileEntity().openersCounter.opened = false;
    }
}
