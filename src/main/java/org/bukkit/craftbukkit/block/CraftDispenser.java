package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityDispenser;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

/**
 * Represents a dispenser.
 * 
 * @author sk89q
 */
public class CraftDispenser extends CraftBlockState implements Dispenser {
    private final CraftWorld world;
    private final TileEntityDispenser dispenser;

    public CraftDispenser(final Block block) {
        super(block);

        world = (CraftWorld)block.getWorld();
        dispenser = (TileEntityDispenser)world.getTileEntityAt(getX(), getY(), getZ());
    }

    public Inventory getInventory() {
        return new CraftInventory(dispenser);
    }

    @Override
    public boolean update(boolean force) {
        boolean result = super.update(force);

        if (result) {
            dispenser.d();
        }

        return result;
    }
}
