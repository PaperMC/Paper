package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.MinecartHopper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;

public class CraftMinecartHopper extends CraftMinecartContainer implements HopperMinecart, com.destroystokyo.paper.loottable.PaperLootableEntityInventory { // Paper

    private final CraftInventory inventory;

    public CraftMinecartHopper(CraftServer server, MinecartHopper entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public net.minecraft.world.entity.vehicle.MinecartHopper getHandle() {
        return (net.minecraft.world.entity.vehicle.MinecartHopper) this.entity;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean isEnabled() {
        return this.getHandle().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.getHandle().setEnabled(enabled);
    }

    @Override
    public int getPickupCooldown() {
        throw new UnsupportedOperationException("Hopper minecarts don't have cooldowns");
    }

    @Override
    public void setPickupCooldown(int cooldown) {
        throw new UnsupportedOperationException("Hopper minecarts don't have cooldowns");
    }
}
