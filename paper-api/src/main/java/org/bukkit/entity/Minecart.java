package org.bukkit.entity;

/**
 * Represents a minecart entity.
 * 
 * @author sk89q
 */
public interface Minecart extends Vehicle {
    /**
     * Sets a minecart's damage.
     * 
     * @param damage over 40 to "kill" a minecart
     */
    public void setDamage(int damage);
    
    /**
     * Gets a minecart's damage.
     * 
     * @param damage
     */
    public int getDamage();
}
