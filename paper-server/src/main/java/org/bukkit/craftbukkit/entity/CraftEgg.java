package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.ThrownEgg;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Egg;

public class CraftEgg extends CraftThrowableProjectile implements Egg {
    public CraftEgg(CraftServer server, ThrownEgg entity) {
        super(server, entity);
    }

    @Override
    public ThrownEgg getHandle() {
        return (ThrownEgg) this.entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }
}
