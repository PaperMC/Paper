package net.minecraft.server;

public class RecipeBookClone extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    // CraftBukkit start - Delegate to new parent class
    public RecipeBookClone() {
        super(new ItemStack(Items.WRITTEN_BOOK, 0, -1), java.util.Arrays.asList(new ItemStack(Items.BOOK_AND_QUILL, 0, 0)));
    }
    // CraftBukkit end

    public boolean a(InventoryCrafting inventoryCrafting, World paramWorld) {
        int i = 0;
        ItemStack itemStack = null;
        for (int j = 0; j < inventoryCrafting.getSize(); j++) {
            ItemStack itemStack1 = inventoryCrafting.getItem(j);
            if (itemStack1 != null) {
                if (itemStack1.getItem() == Items.WRITTEN_BOOK) {
                    if (itemStack != null) {
                        return false;
                    }
                    itemStack = itemStack1;
                } else if (itemStack1.getItem() == Items.BOOK_AND_QUILL) {
                    i++;
                } else {
                    return false;
                }
            }
        }
        return (itemStack != null) && (i > 0);
    }

    public ItemStack a(InventoryCrafting inventoryCrafting) {
        int i = 0;
        ItemStack itemStack = null;
        for (int j = 0; j < inventoryCrafting.getSize(); j++) {
            ItemStack itemStack2 = inventoryCrafting.getItem(j);
            if (itemStack2 != null) {
                if (itemStack2.getItem() == Items.WRITTEN_BOOK) {
                    if (itemStack != null) {
                        return null;
                    }
                    itemStack = itemStack2;
                } else if (itemStack2.getItem() == Items.BOOK_AND_QUILL) {
                    i++;
                } else {
                    return null;
                }
            }
        }
        if ((itemStack == null) || (i < 1)) {
            return null;
        }
        ItemStack itemStack1 = new ItemStack(Items.WRITTEN_BOOK, i + 1);
        itemStack1.setTag((NBTTagCompound) itemStack.getTag().clone());
        if (itemStack.hasName()) {
            itemStack1.c(itemStack.getName());
        }
        return itemStack1;
    }

    public int a() {
        return 9;
    }

    public ItemStack b() {
        return null;
    }
}
