package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftInventoryAbstractHorse implements HorseInventory {

    private final Container bodyArmorInventory;

    public CraftInventoryHorse(Container inventory, Container bodyArmorInventory) {
        super(inventory);
        this.bodyArmorInventory = bodyArmorInventory;
    }

    @Override
    public ItemStack getArmor() {
        net.minecraft.world.item.ItemStack item = this.bodyArmorInventory.getItem(0);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public void setArmor(ItemStack stack) {
        this.bodyArmorInventory.setItem(0, CraftItemStack.asNMSCopy(stack));
    }
}
