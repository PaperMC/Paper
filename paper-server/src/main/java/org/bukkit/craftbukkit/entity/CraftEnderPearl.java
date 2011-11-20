package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderPearl;

public class CraftEnderPearl extends CraftProjectile implements EnderPearl {
    public CraftEnderPearl(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftEnderPearl";
    }
}
