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
        return (net.minecraft.world.entity.OminousItemSpawner) this.entity;
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        this.getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public long getSpawnItemAfterTicks() {
        return this.getHandle().spawnItemAfterTicks;
    }

    @Override
    public void setSpawnItemAfterTicks(long ticks) {
        this.getHandle().spawnItemAfterTicks = ticks;
    }
}
