package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.MinecartChest;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;

public class CraftMinecartChest extends CraftMinecartContainer implements StorageMinecart, com.destroystokyo.paper.loottable.PaperLootableEntityInventory { // Paper

    private final CraftInventory inventory;

    public CraftMinecartChest(CraftServer server, MinecartChest entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
