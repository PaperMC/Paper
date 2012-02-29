package org.bukkit.craftbukkit.inventory;

import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.TileEntityFurnace;

public class CraftInventoryFurnace extends CraftInventory implements FurnaceInventory {
    public CraftInventoryFurnace(TileEntityFurnace inventory) {
        super(inventory);
    }

    public ItemStack getResult() {
        return getItem(2);
    }

    public ItemStack getFuel() {
        return getItem(1);
    }

    public ItemStack getSmelting() {
        return getItem(0);
    }

    public void setFuel(ItemStack stack) {
        setItem(1,stack);
    }

    public void setResult(ItemStack stack) {
        setItem(2,stack);
    }

    public void setSmelting(ItemStack stack) {
        setItem(0,stack);
    }

    @Override
    public Furnace getHolder() {
        return (Furnace) inventory.getOwner();
    }
}
