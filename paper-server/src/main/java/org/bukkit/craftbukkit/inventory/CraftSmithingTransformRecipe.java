package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.TransmuteResult;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;

public class CraftSmithingTransformRecipe extends SmithingTransformRecipe implements CraftRecipe {
    public CraftSmithingTransformRecipe(NamespacedKey key, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition) {
        super(key, result, template, base, addition);
    }
    // Paper start - Option to prevent data components copy
    public CraftSmithingTransformRecipe(NamespacedKey key, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition, boolean copyDataComponents) {
        super(key, result, template, base, addition, copyDataComponents);
    }
    // Paper end - Option to prevent data components copy

    public static CraftSmithingTransformRecipe fromBukkitRecipe(SmithingTransformRecipe recipe) {
        if (recipe instanceof CraftSmithingTransformRecipe) {
            return (CraftSmithingTransformRecipe) recipe;
        }
        CraftSmithingTransformRecipe ret = new CraftSmithingTransformRecipe(recipe.getKey(), recipe.getResult(), recipe.getTemplate(), recipe.getBase(), recipe.getAddition(), recipe.willCopyDataComponents()); // Paper - Option to prevent data components copy
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        final net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(result);
        final net.minecraft.world.item.crafting.SmithingTransformRecipe recipe = new net.minecraft.world.item.crafting.SmithingTransformRecipe(
            this.toNMSOptional(this.getTemplate(), false),
            this.toNMS(this.getBase(), false),
            this.toNMSOptional(this.getAddition(), false),
            new TransmuteResult(nmsStack.getItemHolder(), nmsStack.getCount(), nmsStack.getComponentsPatch())
            , this.willCopyDataComponents()
        );
        MinecraftServer.getServer().getRecipeManager().addRecipe(new RecipeHolder<>(CraftRecipe.toMinecraft(this.getKey()), recipe)); // Paper - Option to prevent data components copy
    }
}
