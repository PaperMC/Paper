package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftShapedRecipe;
import org.bukkit.inventory.ShapedRecipe;
// CraftBukkit end

public class ShapedRecipes implements CraftingRecipe {

    private int b;
    private int c;
    private ItemStack[] d;
    private ItemStack e;
    public final int a;

    public ShapedRecipes(int i, int j, ItemStack[] aitemstack, ItemStack itemstack) {
        this.a = itemstack.id;
        this.b = i;
        this.c = j;
        this.d = aitemstack;
        this.e = itemstack;
    }

    // CraftBukkit start
    public ShapedRecipe toBukkitRecipe() {
        CraftItemStack result = new CraftItemStack(this.e);
        CraftShapedRecipe recipe = new CraftShapedRecipe(result, this);
        switch (this.b) {
        case 1:
            switch (this.c) {
            case 1:
                recipe.shape("a");
                break;
            case 2:
                recipe.shape("ab");
                break;
            case 3:
                recipe.shape("abc");
                break;
            }
            break;
        case 2:
            switch (this.c) {
            case 1:
                recipe.shape("a","b");
                break;
            case 2:
                recipe.shape("ab","cd");
                break;
            case 3:
                recipe.shape("abc","def");
                break;
            }
            break;
        case 3:
            switch (this.c) {
            case 1:
                recipe.shape("a","b","c");
                break;
            case 2:
                recipe.shape("ab","cd","ef");
                break;
            case 3:
                recipe.shape("abc","def","ghi");
                break;
            }
            break;
        }
        char c = 'a';
        for (ItemStack stack : this.d) {
            if (stack != null) {
                recipe.setIngredient(c, org.bukkit.Material.getMaterial(stack.id), stack.getData());
            }
            c++;
        }
        return recipe;
    }
    // CraftBukkit end

    public ItemStack b() {
        return this.e;
    }

    public boolean a(InventoryCrafting inventorycrafting) {
        for (int i = 0; i <= 3 - this.b; ++i) {
            for (int j = 0; j <= 3 - this.c; ++j) {
                if (this.a(inventorycrafting, i, j, true)) {
                    return true;
                }

                if (this.a(inventorycrafting, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean a(InventoryCrafting inventorycrafting, int i, int j, boolean flag) {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                int i1 = k - i;
                int j1 = l - j;
                ItemStack itemstack = null;

                if (i1 >= 0 && j1 >= 0 && i1 < this.b && j1 < this.c) {
                    if (flag) {
                        itemstack = this.d[this.b - i1 - 1 + j1 * this.b];
                    } else {
                        itemstack = this.d[i1 + j1 * this.b];
                    }
                }

                ItemStack itemstack1 = inventorycrafting.b(k, l);

                if (itemstack1 != null || itemstack != null) {
                    if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null) {
                        return false;
                    }

                    if (itemstack.id != itemstack1.id) {
                        return false;
                    }

                    if (itemstack.getData() != -1 && itemstack.getData() != itemstack1.getData()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public ItemStack b(InventoryCrafting inventorycrafting) {
        return new ItemStack(this.e.id, this.e.count, this.e.getData());
    }

    public int a() {
        return this.b * this.c;
    }
}
