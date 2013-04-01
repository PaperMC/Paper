package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityBrewingStand;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftBlockState implements BrewingStand {
    private final TileEntityBrewingStand brewingStand;

    public CraftBrewingStand(Block block) {
        super(block);

        brewingStand = (TileEntityBrewingStand) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public BrewerInventory getInventory() {
        return new CraftInventoryBrewer(brewingStand);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            brewingStand.update();
        }

        return result;
    }

    public int getBrewingTime() {
        return brewingStand.brewTime;
    }

    public void setBrewingTime(int brewTime) {
        brewingStand.brewTime = brewTime;
    }
}
