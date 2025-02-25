package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.Pillager;
import org.bukkit.inventory.Inventory;

public class CraftPillager extends CraftIllager implements Pillager, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.Pillager> { // Paper

    public CraftPillager(CraftServer server, net.minecraft.world.entity.monster.Pillager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Pillager getHandle() {
        return (net.minecraft.world.entity.monster.Pillager) this.entity;
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().inventory);
    }
}
