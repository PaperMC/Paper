package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityBrewingStand;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftBrewingStand extends CraftBlockState implements BrewingStand {
    private final CraftWorld world;
    private final TileEntityBrewingStand brewingStand;

    public CraftBrewingStand(Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        brewingStand = (TileEntityBrewingStand) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public Inventory getInventory() {
        return new CraftInventory(brewingStand);
    }

    @Override
    public boolean update(boolean force) {
        boolean result = super.update(force);

        if (result) {
            brewingStand.update();
        }

        return result;
    }

    public int getBrewingTime() {
        return brewingStand.b;
    }

    public void setBrewingTime(int brewTime) {
        brewingStand.b = brewTime;
    }
}
