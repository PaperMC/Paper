package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAbstractHorse extends CraftInventory implements AbstractHorseInventory {

    public CraftInventoryAbstractHorse(Container inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getSaddle() {
        return this.getItem(0);
    }

    @Override
    public void setSaddle(ItemStack stack) {
        this.setItem(0, stack);
    }
}
