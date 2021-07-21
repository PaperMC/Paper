package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, EntityGuardian entity) {
        super(server, entity);
    }

    @Override
    public EntityGuardian getHandle() {
        return (EntityGuardian) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(target);

        // clean up laser target, when target is removed
        if (target == null) {
            getHandle().a(0); // PAIL rename setLaserTarget
        }
    }

    @Override
    public boolean setLaser(boolean activated) {
        if (activated) {
            LivingEntity target = getTarget();
            if (target == null) {
                return false;
            }

            getHandle().a(target.getEntityId()); // PAIL rename setLaserTarget
        } else {
            getHandle().a(0); // PAIL rename setLaserTarget
        }

        return true;
    }

    @Override
    public boolean hasLaser() {
        return getHandle().fy(); // PAIL rename hasLaserTarget
    }

    @Override
    public boolean isElder() {
        return false;
    }

    @Override
    public void setElder(boolean shouldBeElder) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
