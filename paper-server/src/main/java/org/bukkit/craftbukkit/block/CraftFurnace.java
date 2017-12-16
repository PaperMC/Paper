package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityFurnace;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;

public class CraftFurnace extends CraftContainer<TileEntityFurnace> implements Furnace {

    public CraftFurnace(final Block block) {
        super(block, TileEntityFurnace.class);
    }

    public CraftFurnace(final Material material, final TileEntityFurnace te) {
        super(material, te);
    }

    @Override
    public FurnaceInventory getSnapshotInventory() {
        return new CraftInventoryFurnace(this.getSnapshot());
    }

    @Override
    public FurnaceInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryFurnace(this.getTileEntity());
    }

    @Override
    public short getBurnTime() {
        return (short) this.getSnapshot().getProperty(0);
    }

    @Override
    public void setBurnTime(short burnTime) {
        this.getSnapshot().setProperty(0, burnTime);
    }

    @Override
    public short getCookTime() {
        return (short) this.getSnapshot().getProperty(2);
    }

    @Override
    public void setCookTime(short cookTime) {
        this.getSnapshot().setProperty(2, cookTime);
    }

    @Override
    public String getCustomName() {
        TileEntityFurnace furnace = this.getSnapshot();
        return furnace.hasCustomName() ? furnace.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(name);
    }

    @Override
    public void applyTo(TileEntityFurnace furnace) {
        super.applyTo(furnace);

        if (!this.getSnapshot().hasCustomName()) {
            furnace.setCustomName(null);
        }
    }
}
