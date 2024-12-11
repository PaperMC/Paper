package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryFurnace extends CraftInventory implements FurnaceInventory {
    public CraftInventoryFurnace(AbstractFurnaceBlockEntity inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getResult() {
        return this.getItem(2);
    }

    @Override
    public ItemStack getFuel() {
        return this.getItem(1);
    }

    @Override
    public ItemStack getSmelting() {
        return this.getItem(0);
    }

    @Override
    public void setFuel(ItemStack stack) {
        this.setItem(1, stack);
    }

    @Override
    public void setResult(ItemStack stack) {
        this.setItem(2, stack);
    }

    @Override
    public void setSmelting(ItemStack stack) {
        this.setItem(0, stack);
    }

    @Override
    public Furnace getHolder() {
        return (Furnace) this.inventory.getOwner();
    }
}
