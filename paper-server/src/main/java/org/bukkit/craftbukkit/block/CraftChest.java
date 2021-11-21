package org.bukkit.craftbukkit.block;

import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.ITileInventory;
import net.minecraft.world.level.block.BlockChest;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntityChest;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftLootable<TileEntityChest> implements Chest {

    public CraftChest(World world, TileEntityChest tileEntity) {
        super(world, tileEntity);
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
        if (!isPlaced() || isWorldGeneration()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in BlockChest.interact
        CraftWorld world = (CraftWorld) this.getWorld();

        BlockChest blockChest = (BlockChest) (this.getType() == Material.CHEST ? Blocks.CHEST : Blocks.TRAPPED_CHEST);
        ITileInventory nms = blockChest.getMenuProvider(data, world.getHandle(), this.getPosition(), true);

        if (nms instanceof BlockChest.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((BlockChest.DoubleInventory) nms);
        }
        return inventory;
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.World) {
            IBlockData block = getTileEntity().getBlockState();
            getTileEntity().getLevel().blockEvent(getPosition(), block.getBlock(), 1, getTileEntity().openersCounter.getOpenerCount() + 1);
            TileEntityChest.playSound(getTileEntity().getLevel(), getPosition(), block, SoundEffects.CHEST_OPEN);
        }
        getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.World) {
            IBlockData block = getTileEntity().getBlockState();
            getTileEntity().getLevel().blockEvent(getPosition(), block.getBlock(), 1, 0);
            TileEntityChest.playSound(getTileEntity().getLevel(), getPosition(), block, SoundEffects.CHEST_CLOSE);
        }
        getTileEntity().openersCounter.opened = false;
    }
}
