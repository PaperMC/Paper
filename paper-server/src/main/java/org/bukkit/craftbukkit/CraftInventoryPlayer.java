package org.bukkit.craftbukkit;

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
        return new CraftItemStack( getInventory().e() );
    }

    public CraftItemStack getHelmet() {
        return getItem( getSize() + 0 );
    }

    public CraftItemStack getChestplate() {
        return getItem( getSize() + 1 );
    }

    public CraftItemStack getLeggings() {
        return getItem( getSize() + 2 );
    }

    public CraftItemStack getBoots() {
        return getItem( getSize() + 3 );
    }

    public void setHelmet(ItemStack helmet) {
        setItem( getSize() + 0, helmet );
    }

    public void setChestplate(ItemStack chestplate) {
        setItem( getSize() + 1, chestplate );
    }

    public void setLeggings(ItemStack leggings) {
        setItem( getSize() + 2, leggings );
    }

    public void setBoots(ItemStack boots) {
        setItem( getSize() + 3, boots );
    }

    public ItemStack[] getArmorContents() {
        net.minecraft.server.ItemStack[] mcItems = getInventory().getArmorContents();
        ItemStack[] ret = new ItemStack[mcItems.length];

        for (int i = 0; i < mcItems.length; i++ ) {
            ret[i] = new CraftItemStack(mcItems[i]);
        }
        return ret;
    }
}
