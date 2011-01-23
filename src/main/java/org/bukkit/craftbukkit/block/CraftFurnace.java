package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityFurnace;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

/**
 * Represents a furnace.
 * 
 * @author sk89q
 */
public class CraftFurnace extends CraftBlockState implements Furnace {
    private final CraftWorld world;
    private final TileEntityFurnace furnace;

    public CraftFurnace(final Block block) {
        super(block);

        world = (CraftWorld)block.getWorld();
        furnace = (TileEntityFurnace)world.getTileEntityAt(getX(), getY(), getZ());
    }

    public Inventory getInventory() {
        return new CraftInventory(furnace);
    }

    @Override
    public boolean update(boolean force) {
        boolean result = super.update(force);

        if (result) {
            furnace.d();
        }

        return result;
    }

    public short getBurnTime() {
        return (short)furnace.e;
    }

    public void setBurnTime(short burnTime) {
        furnace.e = burnTime;
    }

    public short getCookTime() {
        return (short)furnace.g;
    }

    public void setCookTime(short cookTime) {
        furnace.g = cookTime;
    }
}
