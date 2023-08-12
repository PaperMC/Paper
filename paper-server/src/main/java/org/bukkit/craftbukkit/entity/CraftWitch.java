package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.EntityWitch;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Witch;

public class CraftWitch extends CraftRaider implements Witch {
    public CraftWitch(CraftServer server, EntityWitch entity) {
        super(server, entity);
    }

    @Override
    public EntityWitch getHandle() {
        return (EntityWitch) entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    @Override
    public boolean isDrinkingPotion() {
        return getHandle().isDrinkingPotion();
    }
}
