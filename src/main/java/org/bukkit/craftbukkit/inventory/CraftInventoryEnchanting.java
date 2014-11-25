package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.ContainerEnchantTableInventory;

public class CraftInventoryEnchanting extends CraftInventory implements EnchantingInventory {
    public CraftInventoryEnchanting(ContainerEnchantTableInventory inventory) {
        super(inventory);
    }

    @Override
    public void setItem(ItemStack item) {
        setItem(0,item);
    }

    @Override
    public ItemStack getItem() {
        return getItem(0);
    }

    @Override
    public ContainerEnchantTableInventory getInventory() {
        return (ContainerEnchantTableInventory)inventory;
    }

    @Override
    public void setSecondary(ItemStack item) {
        setItem(1, item);
    }

    @Override
    public ItemStack getSecondary() {
        return getItem(1);
    }
}
