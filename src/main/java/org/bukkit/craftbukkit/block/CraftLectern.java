package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityLectern;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftLectern extends CraftBlockEntityState<TileEntityLectern> implements Lectern {

    public CraftLectern(Block block) {
        super(block, TileEntityLectern.class);
    }

    public CraftLectern(Material material, TileEntityLectern te) {
        super(material, te);
    }

    @Override
    public int getPage() {
        return getSnapshot().getPage();
    }

    @Override
    public void setPage(int page) {
        getSnapshot().setPage(page);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot().inventory);
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity().inventory);
    }
}
