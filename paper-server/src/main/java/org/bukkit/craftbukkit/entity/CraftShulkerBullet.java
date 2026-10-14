package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ShulkerBullet;

public class CraftShulkerBullet extends AbstractProjectile implements ShulkerBullet {

    public CraftShulkerBullet(CraftServer server, net.minecraft.world.entity.projectile.ShulkerBullet entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.ShulkerBullet getHandle() {
        return (net.minecraft.world.entity.projectile.ShulkerBullet) this.entity;
    }

    @Override
    public org.bukkit.entity.Entity getTarget() {
        return this.getHandle().getTarget() != null ? this.getHandle().getTarget().getBukkitEntity() : null;
    }

    @Override
    public void setTarget(org.bukkit.entity.Entity target) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot set target during world generation");

        this.getHandle().setTarget(target == null ? null : ((CraftEntity) target).getHandle());
    }

    @Override
    public org.bukkit.util.Vector getTargetDelta() {
        net.minecraft.world.entity.projectile.ShulkerBullet bullet = this.getHandle();
        return new org.bukkit.util.Vector(bullet.targetDeltaX, bullet.targetDeltaY, bullet.targetDeltaZ);
    }

    @Override
    public void setTargetDelta(org.bukkit.util.Vector vector) {
        net.minecraft.world.entity.projectile.ShulkerBullet bullet = this.getHandle();
        bullet.targetDeltaX = vector.getX();
        bullet.targetDeltaY = vector.getY();
        bullet.targetDeltaZ = vector.getZ();
    }

    @Override
    public org.bukkit.block.BlockFace getCurrentMovementDirection() {
        net.minecraft.core.Direction dir = this.getHandle().currentMoveDirection;
        if (dir == null) {
            return null; // random dir
        }
        return org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(dir);
    }

    @Override
    public void setCurrentMovementDirection(org.bukkit.block.BlockFace movementDirection) {
        this.getHandle().currentMoveDirection = org.bukkit.craftbukkit.block.CraftBlock.blockFaceToNotch(movementDirection);
    }

    @Override
    public int getFlightSteps() {
        return this.getHandle().flightSteps;
    }

    @Override
    public void setFlightSteps(int steps) {
        this.getHandle().flightSteps = steps;
    }
}
