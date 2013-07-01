package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecartAbstract;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Minecart;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {
    public CraftMinecart(CraftServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    public void setDamage(double damage) {
        getHandle().setDamage((float) damage);
    }

    public double getDamage() {
        return getHandle().getDamage();
    }

    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    public boolean isSlowWhenEmpty() {
        return getHandle().slowWhenEmpty;
    }

    public void setSlowWhenEmpty(boolean slow) {
        getHandle().slowWhenEmpty = slow;
    }

    public Vector getFlyingVelocityMod() {
        return getHandle().getFlyingVelocityMod();
    }

    public void setFlyingVelocityMod(Vector flying) {
        getHandle().setFlyingVelocityMod(flying);
    }

    public Vector getDerailedVelocityMod() {
        return getHandle().getDerailedVelocityMod();
    }

    public void setDerailedVelocityMod(Vector derailed) {
        getHandle().setDerailedVelocityMod(derailed);
    }

    @Override
    public EntityMinecartAbstract getHandle() {
        return (EntityMinecartAbstract) entity;
    }

    @Deprecated
    public void _INVALID_setDamage(int damage) {
        setDamage(damage);
    }

    @Deprecated
    public int _INVALID_getDamage() {
        return NumberConversions.ceil(getDamage());
    }
}
