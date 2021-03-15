package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.IInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftInventoryAbstractHorse implements HorseInventory {

    public CraftInventoryHorse(IInventory inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getArmor() {
       return getItem(1);
    }

    @Override
    public void setArmor(ItemStack stack) {
        setItem(1, stack);
    }
}
