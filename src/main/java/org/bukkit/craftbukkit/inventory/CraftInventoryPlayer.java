package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CraftInventoryPlayer extends CraftInventory implements PlayerInventory {
    public CraftInventoryPlayer(net.minecraft.server.InventoryPlayer inventory) {
        super(inventory);
    }

    public InventoryPlayer getInventory() {
        return (InventoryPlayer) inventory;
    }

    public int getSize() {
        return super.getSize() - 4;
    }

    public CraftItemStack getItemInHand() {
        return new CraftItemStack( getInventory().b() );
    }

    public void setItemInHand(ItemStack stack) {
        setItem( getHeldItemSlot(), stack );
    }

    public int getHeldItemSlot() {
        return getInventory().c;
    }

    public CraftItemStack getHelmet() {
        return getItem( getSize() + 3 );
    }

    public CraftItemStack getChestplate() {
        return getItem( getSize() + 2 );
    }

    public CraftItemStack getLeggings() {
        return getItem( getSize() + 1 );
    }

    public CraftItemStack getBoots() {
        return getItem( getSize() + 0 );
    }

    public void setHelmet(ItemStack helmet) {
        setItem( getSize() + 3, helmet );
    }

    public void setChestplate(ItemStack chestplate) {
        setItem( getSize() + 2, chestplate );
    }

    public void setLeggings(ItemStack leggings) {
        setItem( getSize() + 1, leggings );
    }

    public void setBoots(ItemStack boots) {
        setItem( getSize() + 0, boots );
    }

    public CraftItemStack[] getArmorContents() {
        net.minecraft.server.ItemStack[] mcItems = getInventory().getArmorContents();
        CraftItemStack[] ret = new CraftItemStack[mcItems.length];

        for (int i = 0; i < mcItems.length; i++ ) {
            ret[i] = new CraftItemStack(mcItems[i]);
        }
        return ret;
    }
}
