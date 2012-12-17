package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IRecipe;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCrafting;

import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.Java15Compat;

public class CraftInventoryCrafting extends CraftInventory implements CraftingInventory {
    private final IInventory resultInventory;

    public CraftInventoryCrafting(InventoryCrafting inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getMatrixInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getResultInventory().getSize() + getMatrixInventory().getSize();
    }

    @Override
    public void setContents(ItemStack[] items) {
        int resultLen = getResultInventory().getContents().length;
        int len = getMatrixInventory().getContents().length + resultLen;
        if (len > items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + len + " or less");
        }
        setContents(items[0], Java15Compat.Arrays_copyOfRange(items, 1, items.length));
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[getSize()];
        net.minecraft.server.ItemStack[] mcResultItems = getResultInventory().getContents();

        int i = 0;
        for (i = 0; i < mcResultItems.length; i++ ) {
            items[i] = CraftItemStack.asCraftMirror(mcResultItems[i]);
        }

        net.minecraft.server.ItemStack[] mcItems = getMatrixInventory().getContents();

        for (int j = 0; j < mcItems.length; j++) {
            items[i + j] = CraftItemStack.asCraftMirror(mcItems[j]);
        }

        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        setResult(result);
        setMatrix(contents);
    }

    @Override
    public CraftItemStack getItem(int index) {
        if (index < getResultInventory().getSize()) {
            net.minecraft.server.ItemStack item = getResultInventory().getItem(index);
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.server.ItemStack item = getMatrixInventory().getItem(index - getResultInventory().getSize());
            return item == null ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getResultInventory().getSize()) {
            getResultInventory().setItem(index, (item == null ? null : CraftItemStack.asNMSCopy(item)));
        } else {
            getMatrixInventory().setItem((index - getResultInventory().getSize()), (item == null ? null : CraftItemStack.asNMSCopy(item)));
        }
    }

    public ItemStack[] getMatrix() {
        ItemStack[] items = new ItemStack[getSize()];
        net.minecraft.server.ItemStack[] matrix = getMatrixInventory().getContents();

        for (int i = 0; i < matrix.length; i++ ) {
            items[i] = CraftItemStack.asCraftMirror(matrix[i]);
        }

        return items;
    }

    public ItemStack getResult() {
        net.minecraft.server.ItemStack item = getResultInventory().getItem(0);
        if(item != null) return CraftItemStack.asCraftMirror(item);
        return null;
    }

    public void setMatrix(ItemStack[] contents) {
        if (getMatrixInventory().getContents().length > contents.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getMatrixInventory().getContents().length + " or less");
        }

        net.minecraft.server.ItemStack[] mcItems = getMatrixInventory().getContents();

        for (int i = 0; i < mcItems.length; i++ ) {
            if (i < contents.length) {
                ItemStack item = contents[i];
                if (item == null || item.getTypeId() <= 0) {
                    mcItems[i] = null;
                } else {
                    mcItems[i] = CraftItemStack.asNMSCopy(item);
                }
            } else {
                mcItems[i] = null;
            }
        }
    }

    public void setResult(ItemStack item) {
        net.minecraft.server.ItemStack[] contents = getResultInventory().getContents();
        if (item == null || item.getTypeId() <= 0) {
            contents[0] = null;
        } else {
            contents[0] = CraftItemStack.asNMSCopy(item);
        }
    }

    public Recipe getRecipe() {
        IRecipe recipe = ((InventoryCrafting)getInventory()).currentRecipe;
        return recipe == null ? null : recipe.toBukkitRecipe();
    }
}
