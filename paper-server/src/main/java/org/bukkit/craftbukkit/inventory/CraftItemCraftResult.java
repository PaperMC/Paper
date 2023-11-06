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
        this.result = Objects.requireNonNullElseGet(result, () -> new ItemStack(Material.AIR));
        this.resultMatrix = new ItemStack[9];
        this.overflowItems = new ArrayList<>();

        for (int i = 0; i < resultMatrix.length; i++) {
            resultMatrix[i] = new ItemStack(Material.AIR);
        }
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public ItemStack[] getResultingMatrix() {
        return resultMatrix;
    }

    @Override
    public List<ItemStack> getOverflowItems() {
        return overflowItems;
    }

    public void setResultMatrix(int i, ItemStack itemStack) {
        resultMatrix[i] = Objects.requireNonNullElseGet(itemStack, () -> new ItemStack(Material.AIR));
    }
}
