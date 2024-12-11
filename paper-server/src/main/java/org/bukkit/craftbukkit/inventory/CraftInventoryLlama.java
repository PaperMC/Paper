package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;

public class CraftInventoryLlama extends CraftInventoryAbstractHorse implements LlamaInventory {

    private final Container bodyArmorInventory;

    public CraftInventoryLlama(Container inventory, Container bodyArmorInventory) {
        super(inventory);
        this.bodyArmorInventory = bodyArmorInventory;
    }

    @Override
    public ItemStack getDecor() {
        net.minecraft.world.item.ItemStack item = this.bodyArmorInventory.getItem(0);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public void setDecor(ItemStack stack) {
        this.bodyArmorInventory.setItem(0, CraftItemStack.asNMSCopy(stack));
    }
}
