package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LlamaSpit;

public class CraftLlamaSpit extends AbstractProjectile implements LlamaSpit {

    public CraftLlamaSpit(CraftServer server, net.minecraft.world.entity.projectile.LlamaSpit entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.LlamaSpit getHandle() {
        return (net.minecraft.world.entity.projectile.LlamaSpit) this.entity;
    }
}
