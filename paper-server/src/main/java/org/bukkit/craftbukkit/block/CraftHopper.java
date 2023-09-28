package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityHopper;
import org.bukkit.World;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftLootable<TileEntityHopper> implements Hopper {

    public CraftHopper(World world, TileEntityHopper tileEntity) {
        super(world, tileEntity);
    }

    protected CraftHopper(CraftHopper state) {
        super(state);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public CraftHopper copy() {
        return new CraftHopper(this);
    }
}
