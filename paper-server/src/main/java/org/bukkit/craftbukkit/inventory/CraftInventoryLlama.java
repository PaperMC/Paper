package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;

public class CraftInventoryLlama extends CraftInventoryAbstractHorse implements LlamaInventory {

    public CraftInventoryLlama(Container inventory, Container bodyArmorInventory, Container saddleInventory) {
        super(inventory, bodyArmorInventory, saddleInventory);
    }

    @Override
    public ItemStack getDecor() {
        return this.getArmor(); // Paper
    }

    @Override
    public void setDecor(ItemStack stack) {
        this.setArmor(stack); // Paper
    }
}
