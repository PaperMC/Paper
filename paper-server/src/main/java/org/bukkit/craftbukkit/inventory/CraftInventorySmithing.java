package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.IInventory;
import net.minecraft.world.inventory.InventoryCraftResult;
import net.minecraft.world.item.crafting.IRecipe;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmithingInventory;

public class CraftInventorySmithing extends CraftResultInventory implements SmithingInventory {

    private final Location location;

    public CraftInventorySmithing(Location location, IInventory inventory, InventoryCraftResult resultInventory) {
        super(inventory, resultInventory);
        this.location = location;
    }

    @Override
    public InventoryCraftResult getResultInventory() {
        return (InventoryCraftResult) super.getResultInventory();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public ItemStack getResult() {
        return getItem(2);
    }

    @Override
    public void setResult(ItemStack item) {
        setItem(2, item);
    }

    @Override
    public Recipe getRecipe() {
        IRecipe recipe = getResultInventory().getRecipeUsed();
        return (recipe == null) ? null : recipe.toBukkitRecipe();
    }
}
