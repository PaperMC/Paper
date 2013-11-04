package net.minecraft.server;

import org.bukkit.craftbukkit.entity.CraftHumanEntity; // CraftBukkit

public interface IInventory {

    int getSize();

    ItemStack getItem(int i);

    ItemStack splitStack(int i, int j);

    ItemStack splitWithoutUpdate(int i);

    void setItem(int i, ItemStack itemstack);

    String getInventoryName();

    boolean k_();

    int getMaxStackSize();

    void update();

    boolean a(EntityHuman entityhuman);

    void startOpen();

    void l_();

    boolean b(int i, ItemStack itemstack);

    // CraftBukkit start
    ItemStack[] getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    int MAX_STACK = 64;
    // CraftBukkit end
}
