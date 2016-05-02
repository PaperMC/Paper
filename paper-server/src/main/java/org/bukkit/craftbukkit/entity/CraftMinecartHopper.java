package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.MinecartHopper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;

public final class CraftMinecartHopper extends CraftMinecartContainer implements HopperMinecart, com.destroystokyo.paper.loottable.PaperLootableEntityInventory { // Paper
    private final CraftInventory inventory;

    public CraftMinecartHopper(CraftServer server, MinecartHopper entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartHopper{" + "inventory=" + this.inventory + '}';
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean isEnabled() {
        return ((MinecartHopper) this.getHandle()).isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        ((MinecartHopper) this.getHandle()).setEnabled(enabled);
    }
}
