package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.inventory.ItemCraftResult;
import org.bukkit.inventory.ItemStack;

public final class CraftItemCraftResult implements ItemCraftResult {

    private final ItemStack result;
    private final ItemStack[] resultMatrix;
    private final List<ItemStack> overflowItems;

    public CraftItemCraftResult(ItemStack result) {
        this.result = result;
        this.resultMatrix = new ItemStack[9];
        this.overflowItems = new ArrayList<>();

        for (int i = 0; i < this.resultMatrix.length; i++) {
            this.resultMatrix[i] = ItemStack.empty();
        }
    }

    @Override
    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public ItemStack[] getResultingMatrix() {
        return this.resultMatrix;
    }

    @Override
    public List<ItemStack> getOverflowItems() {
        return this.overflowItems;
    }

    public void setResultMatrix(int i, ItemStack itemStack) {
        this.resultMatrix[i] = itemStack;
    }
}
