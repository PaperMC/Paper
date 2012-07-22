package net.minecraft.server;

public interface CraftingRecipe {

    boolean a(InventoryCrafting inventorycrafting);

    ItemStack b(InventoryCrafting inventorycrafting);

    int a();

    ItemStack b();

    org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit
}
