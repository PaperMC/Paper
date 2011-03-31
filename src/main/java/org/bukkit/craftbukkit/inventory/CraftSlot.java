package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.Slot;

public class CraftSlot implements org.bukkit.inventory.Slot {
    private final Slot slot;

    public CraftSlot(Slot slot) {
        this.slot = slot;
    }

    public Inventory getInventory() {
        return new CraftInventory( slot.e );
    }

    public int getIndex() {
        return slot.d;
    }

    public ItemStack getItem() {
        return new CraftItemStack( slot.a() );
    }
}
