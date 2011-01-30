package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Minecart;

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
        minecart.a = damage;
    }

    public int getDamage() {
        return minecart.a;
    }
    
    /**
     * Internal function to convert an MC entity to an appropriate CraftBukkit
     * entity.
     * 
     * @param server
     * @param minecart
     * @return
     * 
     * @deprecated
     */
    public static CraftMinecart getCraftMinecart(CraftServer server,
            EntityMinecart minecart) {
        if (minecart.d == Type.StorageMinecart.getId()) {
            return new CraftStorageMinecart(server, minecart);
        } else if (minecart.d == Type.PoweredMinecart.getId()) {
            return new CraftPoweredMinecart(server, minecart);
        } else {
            return new CraftMinecart(server, minecart);
        }
    }

    @Override
    public String toString() {
        return "CraftMinecart";
    }

}
