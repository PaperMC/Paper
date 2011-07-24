package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftShapelessRecipe;
import org.bukkit.inventory.ShapelessRecipe;
// CraftBukkit end

public class ShapelessRecipes implements CraftingRecipe {

    private final ItemStack a;
    private final List b;

    public ShapelessRecipes(ItemStack itemstack, List list) {
        this.a = itemstack;
        this.b = list;
    }

    // CraftBukkit start
    @SuppressWarnings("unchecked")
    public ShapelessRecipe toBukkitRecipe() {
        CraftItemStack result = new CraftItemStack(this.a);
        CraftShapelessRecipe recipe = new CraftShapelessRecipe(result, this);
        for (ItemStack stack : (List<ItemStack>) this.b) {
            if (stack != null) {
                recipe.addIngredient(org.bukkit.Material.getMaterial(stack.id), stack.getData());
            }
        }
        return recipe;
    }
    // CraftBukkit end

    public ItemStack b() {
        return this.a;
    }

    public boolean a(InventoryCrafting inventorycrafting) {
        ArrayList arraylist = new ArrayList(this.b);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inventorycrafting.b(j, i);

                if (itemstack != null) {
                    boolean flag = false;
                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext()) {
                        ItemStack itemstack1 = (ItemStack) iterator.next();

                        if (itemstack.id == itemstack1.id && (itemstack1.getData() == -1 || itemstack.getData() == itemstack1.getData())) {
                            flag = true;
                            arraylist.remove(itemstack1);
                            break;
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }
            }
        }

        return arraylist.isEmpty();
    }

    public ItemStack b(InventoryCrafting inventorycrafting) {
        return this.a.cloneItemStack();
    }

    public int a() {
        return this.b.size();
    }
}
