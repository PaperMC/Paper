package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.TileEntityHopper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftLootable<TileEntityHopper> implements Hopper {

    public CraftHopper(final Block block) {
        super(block, TileEntityHopper.class);
    }

    public CraftHopper(final Material material, final TileEntityHopper te) {
        super(material, te);
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
}
