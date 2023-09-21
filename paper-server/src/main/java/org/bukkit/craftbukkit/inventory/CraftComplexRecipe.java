package org.bukkit.craftbukkit.inventory;

import net.minecraft.core.IRegistryCustom;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.IRecipeComplex;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftComplexRecipe implements CraftRecipe, ComplexRecipe {

    private final NamespacedKey key;
    private final IRecipeComplex recipe;

    public CraftComplexRecipe(NamespacedKey key, IRecipeComplex recipe) {
        this.key = key;
        this.recipe = recipe;
    }

    @Override
    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(recipe.getResultItem(IRegistryCustom.EMPTY));
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public void addToCraftingManager() {
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftNamespacedKey.toMinecraft(key), recipe));
    }
}
