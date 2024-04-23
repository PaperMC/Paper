package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.OminousItemSpawner;
import org.bukkit.inventory.ItemStack;

public class CraftOminousItemSpawner extends CraftEntity implements OminousItemSpawner {

    public CraftOminousItemSpawner(CraftServer server, net.minecraft.world.entity.OminousItemSpawner entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.OminousItemSpawner getHandle() {
        return (net.minecraft.world.entity.OminousItemSpawner) entity;
    }

    @Override
    public String toString() {
        return "CraftOminousItemSpawner";
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public long getSpawnItemAfterTicks() {
        return getHandle().spawnItemAfterTicks;
    }

    @Override
    public void setSpawnItemAfterTicks(long ticks) {
        getHandle().spawnItemAfterTicks = ticks;
    }
}
