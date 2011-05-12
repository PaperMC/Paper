package org.bukkit.entity;

/**
 * Represents a Pig.
 * 
 * @author Cogito
 *
 */
public interface Pig extends Animals {
    /**
     * @author xPaw
     * @return if the pig has been saddled.
     */
    public boolean hasSaddle();
    
    /**
     * @author xPaw
     * @param saddled set if the pig has a saddle or not.
     */
    public void setSaddle(boolean saddled);
}
