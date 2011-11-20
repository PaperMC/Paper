package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ThrownPotion;

public class CraftThrownPotion extends CraftProjectile implements ThrownPotion {
    public CraftThrownPotion(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftThrownPotion";
    }
}
