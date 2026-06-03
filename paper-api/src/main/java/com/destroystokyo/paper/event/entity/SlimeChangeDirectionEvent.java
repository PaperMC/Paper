package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.AbstractCubeMob;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a Slime decides to change its facing direction.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to change direction.
 */
@NullMarked
public class SlimeChangeDirectionEvent extends SlimePathfindEvent {

    private float yaw;

    @ApiStatus.Internal
    public SlimeChangeDirectionEvent(final AbstractCubeMob cubeMob, final float yaw) {
        super(cubeMob);
        this.yaw = yaw;
    }

    /**
     * Get the new chosen yaw
     *
     * @return Chosen yaw
     */
    public float getNewYaw() {
        return this.yaw;
    }

    /**
     * Set the new chosen yaw
     *
     * @param yaw Chosen yaw
     */
    public void setNewYaw(final float yaw) {
        this.yaw = yaw;
    }
}
