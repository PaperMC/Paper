package org.bukkit.craftbukkit.block;

import net.minecraft.server.BlockDropper;
import net.minecraft.server.TileEntityDropper;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper extends CraftBlockState implements Dropper {
    private final CraftWorld world;
    private final TileEntityDropper dropper;

    public CraftDropper(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        dropper = (TileEntityDropper) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public Inventory getInventory() {
        return new CraftInventory(dropper);
    }

    public void drop() {
        Block block = getBlock();

        if (block.getType() == Material.DROPPER) {
            BlockDropper drop = (BlockDropper) net.minecraft.server.Block.DROPPER;

            drop.dispense(world.getHandle(), getX(), getY(), getZ());
        }
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            dropper.update();
        }

        return result;
    }
}
