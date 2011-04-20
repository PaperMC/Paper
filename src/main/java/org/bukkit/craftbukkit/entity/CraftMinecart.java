package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

/**
 * A minecart.
 *
 * @author sk89q
 */
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

    protected EntityMinecart minecart;

    public CraftMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
        minecart = entity;
    }

    public void setDamage(int damage) {
        minecart.damage = damage;
    }

    public int getDamage() {
        return minecart.damage;
    }

    public double getMaxSpeed() {
        return minecart.maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            minecart.maxSpeed = speed;
        }
    }

    public boolean isSlowWhenEmpty() {
        return minecart.slowWhenEmpty;
    }

    public void setSlowWhenEmpty(boolean slow) {
        minecart.slowWhenEmpty = slow;
    }

    public Vector getFlyingVelocityMod() {
        return new Vector(minecart.flyingX, minecart.flyingY, minecart.flyingZ);
    }

    public void setFlyingVelocityMod(Vector flying) {
        minecart.flyingX = flying.getX();
        minecart.flyingY = flying.getY();
        minecart.flyingZ = flying.getZ();
    }

    public Vector getDerailedVelocityMod() {
        return new Vector(minecart.derailedX, minecart.derailedY, minecart.derailedZ);
    }

    public void setDerailedVelocityMod(Vector derailed) {
        minecart.derailedX = derailed.getX();
        minecart.derailedY = derailed.getY();
        minecart.derailedZ = derailed.getZ();
    }

    @Override
    public String toString() {
        return "CraftMinecart";
    }

}
