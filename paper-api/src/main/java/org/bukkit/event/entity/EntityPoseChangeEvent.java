package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity changes its pose.
 *
 * @see Entity#getPose()
 */
public interface EntityPoseChangeEvent extends EntityEventNew {

    /**
     * Gets the entity's new pose.
     *
     * @return the new pose
     */
    Pose getPose();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
