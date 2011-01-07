package org.bukkit.event.vehicle;

import org.bukkit.Vector;
import org.bukkit.Vehicle;

/**
 * Raised when a vehicle is created.
 * 
 * @author sk89q
 */
public class VehicleCreateEvent extends VehicleEvent {
    private boolean slowWhenEmpty;
    private Vector derailedVelocityFactor;
    private Vector flyingVelocityFactor;
    
    public VehicleCreateEvent(Type type, Vehicle vehicle,
            boolean slowWhenEmpty, Vector derailedVelocityFactor,
            Vector flyingVelocityFactor) {
        
        super(type, vehicle);
        this.slowWhenEmpty = slowWhenEmpty;
        this.derailedVelocityFactor = derailedVelocityFactor;
        this.flyingVelocityFactor = flyingVelocityFactor;
    }
    
    public void setSlowWhenEmpty(boolean setting) {
        slowWhenEmpty = setting;
    }
    
    public boolean shouldSlowWhenEmpty() {
        return slowWhenEmpty;
    }
    
    public void setDerailedVelocityFactor(Vector setting) {
        derailedVelocityFactor = setting.clone();
    }
    
    public Vector getDerailedVelocityFactor() {
        return derailedVelocityFactor.clone();
    }
    
    public void setFlyingVelocityFactor(Vector setting) {
        flyingVelocityFactor = setting.clone();
    }
    
    public Vector getFlyingVelocityFactor() {
        return flyingVelocityFactor.clone();
    }
}
