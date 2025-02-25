package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull {

    public CraftWitherSkull(CraftServer server, net.minecraft.world.entity.projectile.WitherSkull entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.WitherSkull getHandle() {
        return (net.minecraft.world.entity.projectile.WitherSkull) this.entity;
    }

    @Override
    public void setCharged(boolean charged) {
        this.getHandle().setDangerous(charged);
    }

    @Override
    public boolean isCharged() {
        return this.getHandle().isDangerous();
    }
}
