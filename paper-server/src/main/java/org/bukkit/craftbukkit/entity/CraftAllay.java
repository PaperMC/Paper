package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.allay.Allay;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;

public class CraftAllay extends CraftCreature implements org.bukkit.entity.Allay {

    public CraftAllay(CraftServer server, Allay entity) {
        super(server, entity);
    }

    @Override
    public Allay getHandle() {
        return (Allay) entity;
    }

    @Override
    public String toString() {
        return "CraftAllay";
    }

    @Override
    public EntityType getType() {
        return EntityType.ALLAY;
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(getHandle().getInventory());
    }
}
