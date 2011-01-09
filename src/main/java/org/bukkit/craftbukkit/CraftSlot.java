package org.bukkit.craftbukkit;

import org.bukkit.Inventory;
import org.bukkit.ItemStack;
import net.minecraft.server.Slot;

public class CraftSlot implements org.bukkit.Slot {
    private final Slot slot;

    public CraftSlot(Slot slot) {
        this.slot = slot;
    }

    public Inventory getInventory() {
        return new CraftInventory( slot.b );
    }

    public int getIndex() {
        return slot.a;
    }

    public ItemStack getItem() {
        return new CraftItemStack( slot.c() );
    }
}
