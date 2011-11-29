package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

public class CraftMinecart extends CraftVehicle implements Minecart {
    /**
     * Stores the minecart type id, which is used by Minecraft to differentiate
     * minecart types. Here we use subclasses.
     */
    public enum Type {
        Minecart(0),
        StorageMinecart(1),
        PoweredMinecart(2);

        private final int id;

        private Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public CraftMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
    }

    public void setDamage(int damage) {
        getHandle().setDamage(damage);
    }

    public int getDamage() {
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
    public EntityMinecart getHandle() {
        return (EntityMinecart) entity;
    }

    @Override
    public String toString() {
        return "CraftMinecart";
    }
}
