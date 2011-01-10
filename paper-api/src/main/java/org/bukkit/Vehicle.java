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
     * Set the passenger of a vehicle.
     * 
     * @param passenger
     * @return false if it could not be done for whatever reason
     */
    public boolean setPassenger(Entity passenger);
    
    /**
     * Returns true if the vehicle has no passengers.
     * 
     * @return
     */
    public boolean isEmpty();
    
    /**
     * Eject any passenger. True if there was a passenger.
     * 
     * @return
     */
    public boolean eject();
}
