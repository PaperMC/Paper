package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryWrapper implements IInventory {

    private final Inventory inventory;
    private final List<HumanEntity> viewers = new ArrayList<HumanEntity>();

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public ItemStack getItem(int i) {
        return CraftItemStack.asNMSCopy(inventory.getItem(i));
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        // Copied from CraftItemStack
        ItemStack stack = getItem(i);
        ItemStack result;
        if (stack.isEmpty()) {
            return stack;
        }
        if (stack.getCount() <= j) {
            this.setItem(i, ItemStack.a);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, j);
            stack.subtract(j);
        }
        this.update();
        return result;
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        // Copied from CraftItemStack
        ItemStack stack = getItem(i);
        ItemStack result;
        if (stack.isEmpty()) {
            return stack;
        }
        if (stack.getCount() <= 1) {
            this.setItem(i, ItemStack.a);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, 1);
            stack.subtract(1);
        }
        return result;
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        inventory.setItem(i, CraftItemStack.asBukkitCopy(itemstack));
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void update() {
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public void startOpen(EntityHuman entityhuman) {
    }

    @Override
    public void closeContainer(EntityHuman entityhuman) {
    }

    @Override
    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int getProperty(int i) {
        return 0;
    }

    @Override
    public void setProperty(int i, int j) {
    }

    @Override
    public int h() {
        return 0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public List<ItemStack> getContents() {
        int size = getSize();
        List<ItemStack> items = new ArrayList<ItemStack>(size);

        for (int i = 0; i < size; i++) {
            items.set(i, getItem(i));
        }

        return items;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        viewers.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        viewers.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return viewers;
    }

    @Override
    public InventoryHolder getOwner() {
        return inventory.getHolder();
    }

    @Override
    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return getName() != null;
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return CraftChatMessage.fromString(getName())[0];
    }

    @Override
    public Location getLocation() {
        return inventory.getLocation();
    }

    @Override
    public boolean x_() {
        return Iterables.any(inventory, Predicates.notNull());
    }
}
