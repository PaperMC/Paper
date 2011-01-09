package org.bukkit.craftbukkit;

import java.util.ArrayList;

import net.minecraft.server.InventoryPlayer;

import org.bukkit.ItemStack;
import org.bukkit.PlayerInventory;

public class CraftInventoryPlayer extends CraftInventory implements PlayerInventory {
    public CraftInventoryPlayer(net.minecraft.server.InventoryPlayer inventory) {
        super(inventory);
    }
    
    public InventoryPlayer getInventory() {
        return (InventoryPlayer) inventory;
    }

    public ArrayList<ItemStack> getArmorContents() {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (net.minecraft.server.ItemStack item : getInventory().getArmorContents()) {
            ItemStack i = null;
            if (item != null) {
                i = new CraftItemStack(item);
            }
            items.add(i);
        }

        return items;
    }

    public ItemStack getItemInHand() {
        return new CraftItemStack( getInventory().e() );
    }

    public ItemStack getHelmet() {
        return getItem( getSize() - 4 );
    }

    public ItemStack getChestplate() {
        return getItem( getSize() - 3 );
    }

    public ItemStack getLeggings() {
        return getItem( getSize() - 2 );
    }

    public ItemStack getBoots() {
        return getItem( getSize() - 1 );
    }

    public void setHelmet(ItemStack helmet) {
        setItem( getSize() - 4, helmet );
    }

    public void setChestplate(ItemStack chestplate) {
        setItem( getSize() - 3, chestplate );
    }

    public void setLeggings(ItemStack leggings) {
        setItem( getSize() - 2, leggings );
    }

    public void setBoots(ItemStack boots) {
        setItem( getSize() - 1, boots );
    }
}
