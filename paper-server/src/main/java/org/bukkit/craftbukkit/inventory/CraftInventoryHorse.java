package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftInventory implements HorseInventory {

    public CraftInventoryHorse(IInventory inventory) {
        super(inventory);
    }

    public ItemStack getSaddle() {
        return getItem(0);
    }

    public ItemStack getArmor() {
       return getItem(1);
    }

    public void setSaddle(ItemStack stack) {
        setItem(0, stack);
    }

    public void setArmor(ItemStack stack) {
        setItem(1, stack);
    }
}
