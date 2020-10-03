package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.craftbukkit.inventory.CraftStonecuttingRecipe;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;
// CraftBukkit end

public class RecipeStonecutting extends RecipeSingleItem {

    public RecipeStonecutting(MinecraftKey minecraftkey, String s, RecipeItemStack recipeitemstack, ItemStack itemstack) {
        super(Recipes.STONECUTTING, RecipeSerializer.t, minecraftkey, s, recipeitemstack, itemstack);
    }

    @Override
    public boolean a(IInventory iinventory, World world) {
        return this.ingredient.test(iinventory.getItem(0));
    }

    // CraftBukkit start
    @Override
    public Recipe toBukkitRecipe() {
        CraftItemStack result = CraftItemStack.asCraftMirror(this.result);

        CraftStonecuttingRecipe recipe = new CraftStonecuttingRecipe(CraftNamespacedKey.fromMinecraft(this.key), result, CraftRecipe.toBukkit(this.ingredient));
        recipe.setGroup(this.group);

        return recipe;
    }
    // CraftBukkit end
}
