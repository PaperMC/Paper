package org.bukkit.craftbukkit;

import net.minecraft.server.EntityMinecart;
import org.bukkit.LivingEntity;
import org.bukkit.Minecart;
import org.bukkit.Vector;

/**
 * A minecart.
 * 
 * @author sk89q
 */
public class CraftMinecart extends CraftVehicle implements Minecart {
    /**
     * Stores the minecart type ID, which is used by Minecraft to differentiate
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
        
        public int getID() {
            return id;
        }
    }
    
    protected EntityMinecart minecart;

    public CraftMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
        minecart = entity;
    }

    @Override
    public Vector getVelocity() {
        return new Vector(minecart.s, minecart.t, minecart.u);
    }

    public void setVelocity(Vector vel) {
        minecart.s = vel.getX();
        minecart.t = vel.getY();
        minecart.u = vel.getZ();
    }

    public LivingEntity getPassenger() {
        // @TODO: Implement
        return null;
    }

    public boolean isEmpty() {
        return minecart.j == null;
    }

    public void setDamage(int damage) {
        minecart.a = damage;
    }

    public int getDamage() {
        return minecart.a;
    }

}
