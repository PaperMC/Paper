package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.AbstractChestBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public abstract class CraftChestBoat extends CraftBoat implements org.bukkit.entity.ChestBoat, com.destroystokyo.paper.loottable.PaperLootableEntityInventory { // Paper
    private final Inventory inventory;

    public CraftChestBoat(CraftServer server, AbstractChestBoat entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public AbstractChestBoat getHandle() {
        return (AbstractChestBoat) this.entity;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
