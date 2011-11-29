package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.PlayerInventory;

import org.bukkit.inventory.ItemStack;

public class CraftInventoryPlayer extends CraftInventory implements org.bukkit.inventory.PlayerInventory {
    public CraftInventoryPlayer(net.minecraft.server.PlayerInventory inventory) {
        super(inventory);
    }

    public PlayerInventory getInventory() {
        return (PlayerInventory) inventory;
    }

    public int getSize() {
        return super.getSize() - 4;
    }

    public ItemStack getItemInHand() {
        return new CraftItemStack(getInventory().getItemInHand());
    }

    public void setItemInHand(ItemStack stack) {
        setItem(getHeldItemSlot(), stack);
    }

    public int getHeldItemSlot() {
        return getInventory().itemInHandIndex;
    }

    public ItemStack getHelmet() {
        return getItem(getSize() + 3);
    }

    public ItemStack getChestplate() {
        return getItem(getSize() + 2);
    }

    public ItemStack getLeggings() {
        return getItem(getSize() + 1);
    }

    public ItemStack getBoots() {
        return getItem(getSize() + 0);
    }

    public void setHelmet(ItemStack helmet) {
        setItem(getSize() + 3, helmet);
    }

    public void setChestplate(ItemStack chestplate) {
        setItem(getSize() + 2, chestplate);
    }

    public void setLeggings(ItemStack leggings) {
        setItem(getSize() + 1, leggings);
    }

    public void setBoots(ItemStack boots) {
        setItem(getSize() + 0, boots);
    }

    public CraftItemStack[] getArmorContents() {
        net.minecraft.server.ItemStack[] mcItems = getInventory().getArmorContents();
        CraftItemStack[] ret = new CraftItemStack[mcItems.length];

        for (int i = 0; i < mcItems.length; i++) {
            ret[i] = new CraftItemStack(mcItems[i]);
        }
        return ret;
    }

    public void setArmorContents(ItemStack[] items) {
        int cnt = getSize();

        if (items == null) {
            items = new ItemStack[4];
        }
        for (ItemStack item : items) {
            if (item == null || item.getTypeId() == 0) {
                clear(cnt++);
            } else {
                setItem(cnt++, item);
            }
        }
    }
}
