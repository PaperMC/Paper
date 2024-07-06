package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;

public class CraftInventoryLlama extends CraftInventoryAbstractHorse implements LlamaInventory {

    // Paper start - properly combine both inventories
    public CraftInventoryLlama(Container inventory, Container bodyArmorInventory) {
        super(inventory, bodyArmorInventory);
        // Paper end - properly combine both inventories
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
