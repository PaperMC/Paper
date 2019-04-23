package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.server.ContainerAnvil;
import net.minecraft.server.IInventory;
import org.bukkit.Location;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryAnvil extends CraftInventory implements AnvilInventory {

    private final Location location;
    private final IInventory resultInventory;
    private final ContainerAnvil container;

    public CraftInventoryAnvil(Location location, IInventory inventory, IInventory resultInventory, ContainerAnvil container) {
        super(inventory);
        this.location = location;
        this.resultInventory = resultInventory;
        this.container = container;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < getIngredientsInventory().getSize()) {
            net.minecraft.server.ItemStack item = getIngredientsInventory().getItem(slot);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.server.ItemStack item = getResultInventory().getItem(slot - getIngredientsInventory().getSize());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getIngredientsInventory().getSize()) {
            getIngredientsInventory().setItem(index, CraftItemStack.asNMSCopy(item));
        } else {
            getResultInventory().setItem((index - getIngredientsInventory().getSize()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return getResultInventory().getSize() + getIngredientsInventory().getSize();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getRenameText() {
        return container.renameText;
    }

    @Override
    public int getRepairCost() {
        return container.levelCost.b();
    }

    @Override
    public void setRepairCost(int i) {
        container.levelCost.a(i);
    }

    @Override
    public int getMaximumRepairCost() {
        return container.maximumRepairCost;
    }

    @Override
    public void setMaximumRepairCost(int levels) {
        Preconditions.checkArgument(levels >= 0, "Maximum repair cost must be positive (or 0)");
        container.maximumRepairCost = levels;
    }
}
