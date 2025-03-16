package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CraftInventoryCrafting extends CraftInventory implements CraftingInventory {
    private final Container resultInventory;

    public CraftInventoryCrafting(CraftingContainer inventory, Container resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public Container getResultInventory() {
        return this.resultInventory;
    }

    public CraftingContainer getMatrixInventory() {
        return (CraftingContainer) this.inventory;
    }

    @Override
    public int getSize() {
        return this.getResultInventory().getContainerSize() + this.getMatrixInventory().getContainerSize();
    }

    @Override
    public void setContents(ItemStack[] items) {
        Preconditions.checkArgument(items.length <= this.getSize(), "Invalid inventory size (%s); expected %s or less", items.length, this.getSize());
        this.setContents(items[0], Arrays.copyOfRange(items, 1, items.length));
    }

    @Override
    public ItemStack[] getContents() {
        // Create an array with enough space for both result and matrix items.
        ItemStack[] items = new ItemStack[this.getSize()];
        
        // Fill in the result inventory items.
        List<net.minecraft.world.item.ItemStack> mcResultItems = this.getResultInventory().getContents();
        int resultSize = mcResultItems.size();
        for (int i = 0; i < resultSize; i++) {
            items[i] = CraftItemStack.asCraftMirror(mcResultItems.get(i));
        }
        
        // Fill in the matrix inventory items using proper row/column indexing.
        List<net.minecraft.world.item.ItemStack> mcMatrixItems = this.getMatrixInventory().getContents();
        int matrixStart = resultSize; // Offset in the items array where matrix items begin.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                if (index < mcMatrixItems.size()) {
                    items[matrixStart + index] = CraftItemStack.asCraftMirror(mcMatrixItems.get(index));
                }
            }
        }
        
        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        this.setResult(result);
        this.setMatrix(contents);
    }

    @Override
    public CraftItemStack getItem(int index) {
        if (index < this.getResultInventory().getContainerSize()) {
            net.minecraft.world.item.ItemStack item = this.getResultInventory().getItem(index);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.world.item.ItemStack item = this.getMatrixInventory().getItem(index - this.getResultInventory().getContainerSize());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < this.getResultInventory().getContainerSize()) {
            this.getResultInventory().setItem(index, CraftItemStack.asNMSCopy(item));
        } else {
            this.getMatrixInventory().setItem((index - this.getResultInventory().getContainerSize()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public ItemStack[] getMatrix() {
        List<net.minecraft.world.item.ItemStack> matrix = this.getMatrixInventory().getContents();

        return this.asCraftMirror(matrix);
    }

    @Override
    public ItemStack getResult() {
        net.minecraft.world.item.ItemStack item = this.getResultInventory().getItem(0);
        if (!item.isEmpty()) return CraftItemStack.asCraftMirror(item);
        return null;
    }

    @Override
    public void setMatrix(ItemStack[] contents) {
        Preconditions.checkArgument(contents.length <= this.getMatrixInventory().getContainerSize(), "Invalid inventory size (%s); expected %s or less", contents.length, this.getMatrixInventory().getContainerSize());

        for (int i = 0; i < this.getMatrixInventory().getContainerSize(); i++) {
            if (i < contents.length) {
                this.getMatrixInventory().setItem(i, CraftItemStack.asNMSCopy(contents[i]));
            } else {
                this.getMatrixInventory().setItem(i, net.minecraft.world.item.ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void setResult(ItemStack item) {
        List<net.minecraft.world.item.ItemStack> contents = this.getResultInventory().getContents();
        contents.set(0, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public Recipe getRecipe() {
        RecipeHolder<?> recipe = this.getMatrixInventory().getCurrentRecipe();
        return recipe == null ? null : recipe.toBukkitRecipe();
    }
}
