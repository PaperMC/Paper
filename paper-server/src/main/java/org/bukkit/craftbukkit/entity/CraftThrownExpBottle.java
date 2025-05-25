package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftThrowableProjectile implements ThrownExpBottle {

    public CraftThrownExpBottle(CraftServer server, ThrownExperienceBottle entity) {
        super(server, entity);
    }

    @Override
    public ThrownExperienceBottle getHandle() {
        return (ThrownExperienceBottle) this.entity;
    }
}
