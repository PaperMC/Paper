package com.destroystokyo.paper.event.entity;

/**
 * Fired when a Slime decides to change its facing direction.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to change direction.
 */
public interface SlimeChangeDirectionEvent extends SlimePathfindEvent {

    /**
     * Get the new chosen yaw
     *
     * @return Chosen yaw
     */
    float getNewYaw();

    /**
     * Set the new chosen yaw
     *
     * @param yaw Chosen yaw
     */
    void setNewYaw(float yaw);
}
