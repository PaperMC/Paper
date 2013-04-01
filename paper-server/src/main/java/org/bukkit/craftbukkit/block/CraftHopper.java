package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityHopper;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftBlockState implements Hopper {
    private final TileEntityHopper hopper;

    public CraftHopper(final Block block) {
        super(block);

        hopper = (TileEntityHopper) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public Inventory getInventory() {
        return new CraftInventory(hopper);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            hopper.update();
        }

        return result;
    }
}
