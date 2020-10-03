package net.minecraft.server;

import java.util.Set;
import org.bukkit.craftbukkit.entity.CraftHumanEntity; // CraftBukkit

public interface IInventory extends Clearable {

    int getSize();

    boolean isEmpty();

    ItemStack getItem(int i);

    ItemStack splitStack(int i, int j);

    ItemStack splitWithoutUpdate(int i);

    void setItem(int i, ItemStack itemstack);

    int getMaxStackSize(); // CraftBukkit

    void update();

    boolean a(EntityHuman entityhuman);

    default void startOpen(EntityHuman entityhuman) {}

    default void closeContainer(EntityHuman entityhuman) {}

    default boolean b(int i, ItemStack itemstack) {
        return true;
    }

    default int a(Item item) {
        int i = 0;

        for (int j = 0; j < this.getSize(); ++j) {
            ItemStack itemstack = this.getItem(j);

            if (itemstack.getItem().equals(item)) {
                i += itemstack.getCount();
            }
        }

        return i;
    }

    default boolean a(Set<Item> set) {
        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack itemstack = this.getItem(i);

            if (set.contains(itemstack.getItem()) && itemstack.getCount() > 0) {
                return true;
            }
        }

        return false;
    }

    // CraftBukkit start
    java.util.List<ItemStack> getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    org.bukkit.Location getLocation();

    default IRecipe getCurrentRecipe() {
        return null;
    }

    default void setCurrentRecipe(IRecipe recipe) {
    }

    int MAX_STACK = 64;
    // CraftBukkit end
}
