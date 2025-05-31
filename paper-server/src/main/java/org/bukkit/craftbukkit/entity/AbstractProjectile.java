package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Projectile;

public abstract class AbstractProjectile extends CraftEntity implements Projectile {

    public AbstractProjectile(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.Projectile getHandle() {
        return (net.minecraft.world.entity.projectile.Projectile) this.entity;
    }

    @Override
    public boolean doesBounce() {
        return false;
    }

    @Override
    public void setBounce(boolean doesBounce) {}

    // Paper start - More projectile API
    @Override
    public boolean hasLeftShooter() {
        return this.getHandle().leftOwner;
    }

    @Override
    public void setHasLeftShooter(boolean leftShooter) {
        this.getHandle().leftOwner = leftShooter;
    }

    @Override
    public boolean hasBeenShot() {
        return this.getHandle().hasBeenShot;
    }

    @Override
    public void setHasBeenShot(boolean beenShot) {
        this.getHandle().hasBeenShot = beenShot;
    }

    @Override
    public boolean canHitEntity(org.bukkit.entity.Entity entity) {
        return this.getHandle().canHitEntityPublic(((CraftEntity) entity).getHandle());
    }

    @Override
    public void hitEntity(org.bukkit.entity.Entity entity) {
        this.getHandle().preHitTargetOrDeflectSelf(new net.minecraft.world.phys.EntityHitResult(((CraftEntity) entity).getHandle()));
    }

    @Override
    public void hitEntity(org.bukkit.entity.Entity entity, org.bukkit.util.Vector vector) {
        this.getHandle().preHitTargetOrDeflectSelf(new net.minecraft.world.phys.EntityHitResult(((CraftEntity) entity).getHandle(), new net.minecraft.world.phys.Vec3(vector.getX(), vector.getY(), vector.getZ())));
    }

    @Override
    public final org.bukkit.projectiles.ProjectileSource getShooter() {
        this.getHandle().refreshProjectileSource(true); // Paper - Refresh ProjectileSource for projectiles
        return this.getHandle().projectileSource;
    }

    @Override
    public final void setShooter(org.bukkit.projectiles.ProjectileSource shooter) {
        if (shooter instanceof CraftEntity craftEntity) {
            this.getHandle().setOwner(craftEntity.getHandle());
        } else {
            this.getHandle().setOwner(null);
        }
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public java.util.UUID getOwnerUniqueId() {
        EntityReference<Entity> reference = this.getHandle().owner;
        if (reference == null) {
            return null;
        }

        return reference.getUUID();
    }
    // Paper end - More projectile API
}
