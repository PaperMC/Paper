package org.bukkit.block;

import org.bukkit.entity.MobType;

/**
 * Represents a mob spawner.
 * 
 * @author sk89q
 */
public interface MobSpawner extends BlockState {
    /**
     * Get the spawner's mob type.
     * 
     * @return
     */
    public MobType getMobType();
    
    /**
     * Set the spawner mob type.
     * 
     * @param mobType
     */
    public void setMobType(MobType mobType);
    
    /**
     * Get the spawner's mob type.
     * 
     * @return
     */
    public String getMobTypeId();
    
    /**
     * Set the spawner mob type.
     * 
     * @param mobType
     */
    public void setMobTypeId(String mobType);
    
    /**
     * Get the spawner's delay.
     * 
     * @return
     */
    public int getDelay();
    
    /**
     * Set the spawner's delay.
     * 
     * @param delay
     */
    public void setDelay(int delay);
}
