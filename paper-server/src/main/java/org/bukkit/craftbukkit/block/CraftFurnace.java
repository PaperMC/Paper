package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.BlockFurnace;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import org.bukkit.World;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;

public abstract class CraftFurnace<T extends TileEntityFurnace> extends CraftContainer<T> implements Furnace {

    public CraftFurnace(World world, T tileEntity) {
        super(world, tileEntity);
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
        return (short) this.getSnapshot().litTime;
    }

    @Override
    public void setBurnTime(short burnTime) {
        this.getSnapshot().litTime = burnTime;
        // SPIGOT-844: Allow lighting and relighting using this API
        this.data = this.data.set(BlockFurnace.LIT, burnTime > 0);
    }

    @Override
    public short getCookTime() {
        return (short) this.getSnapshot().cookingProgress;
    }

    @Override
    public void setCookTime(short cookTime) {
        this.getSnapshot().cookingProgress = cookTime;
    }

    @Override
    public int getCookTimeTotal() {
        return this.getSnapshot().cookingTotalTime;
    }

    @Override
    public void setCookTimeTotal(int cookTimeTotal) {
        this.getSnapshot().cookingTotalTime = cookTimeTotal;
    }
}
