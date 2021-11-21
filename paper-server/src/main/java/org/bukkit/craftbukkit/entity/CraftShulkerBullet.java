package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.projectile.EntityShulkerBullet;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.projectiles.ProjectileSource;

public class CraftShulkerBullet extends AbstractProjectile implements ShulkerBullet {

    public CraftShulkerBullet(CraftServer server, EntityShulkerBullet entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof Entity) {
            getHandle().setOwner(((CraftEntity) shooter).getHandle());
        } else {
            getHandle().setOwner(null);
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public org.bukkit.entity.Entity getTarget() {
        return getHandle().getTarget() != null ? getHandle().getTarget().getBukkitEntity() : null;
    }

    @Override
    public void setTarget(org.bukkit.entity.Entity target) {
        Preconditions.checkState(!getHandle().generation, "Cannot set target during world generation");

        getHandle().setTarget(target == null ? null : ((CraftEntity) target).getHandle());
    }

    @Override
    public String toString() {
        return "CraftShulkerBullet";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHULKER_BULLET;
    }

    @Override
    public EntityShulkerBullet getHandle() {
        return (EntityShulkerBullet) entity;
    }
}
