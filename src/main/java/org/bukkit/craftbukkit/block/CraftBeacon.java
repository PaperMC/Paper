package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityBeacon;

import org.bukkit.block.Block;
import org.bukkit.block.Beacon;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.inventory.Inventory;

public class CraftBeacon extends CraftBlockState implements Beacon {
    private final CraftWorld world;
    private final TileEntityBeacon beacon;

    public CraftBeacon(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        beacon = (TileEntityBeacon) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public Inventory getInventory() {
        return new CraftInventoryBeacon(beacon);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            beacon.update();
        }

        return result;
    }
}

