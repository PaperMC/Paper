package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory {
    public CraftInventoryBrewer(Container inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getIngredient() {
        return this.getItem(3);
    }

    @Override
    public void setIngredient(ItemStack ingredient) {
        this.setItem(3, ingredient);
    }

    @Override
    public BrewingStand getHolder() {
        return (BrewingStand) this.inventory.getOwner();
    }

    @Override
    public ItemStack getFuel() {
        return this.getItem(4);
    }

    @Override
    public void setFuel(ItemStack fuel) {
        this.setItem(4, fuel);
    }
}
