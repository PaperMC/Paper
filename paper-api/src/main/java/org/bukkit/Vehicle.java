package org.bukkit;

/**
 * Represents a vehicle entity.
 * 
 * @author sk89q
 */
public interface Vehicle extends Entity {
    /**
     * Gets the vehicle's velocity.
     * 
     * @return velocity vector
     */
    public Vector getVelocity();
    
    /**
     * Sets the vehicle's velocity.
     * 
     * @param vel velocity vector
     */
    public void setVelocity(Vector vel);
    
    /**
     * Gets the primary passenger of a vehicle. For vehicles that could have
     * multiple passengers, this will only return the primary passenger.
     * 
     * @return an entity
     */
    public Entity getPassenger();
    
    /**
     * Returns true if the vehicle has no passengers.
     * 
     * @return
     */
    public boolean isEmpty();
}
