package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.SmallFireball;

public class CraftSmallFireball extends CraftSizedFireball implements SmallFireball {

    public CraftSmallFireball(CraftServer server, net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball getHandle() {
        return (net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball) this.entity;
    }
}
