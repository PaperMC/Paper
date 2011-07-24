package net.minecraft.server;

import org.bukkit.inventory.Recipe; // CraftBukkit

public interface CraftingRecipe {

    boolean a(InventoryCrafting inventorycrafting);

    ItemStack b(InventoryCrafting inventorycrafting);

    int a();

    ItemStack b();
    
    Recipe toBukkitRecipe(); // CraftBukkit
}
