package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * WARNING: This event only fires for a limited number of cases, and not for every case that {@link CreatureSpawnEvent} does.
 * <p>
 * You should still listen to {@link CreatureSpawnEvent} as a backup, and only use this event as an "enhancement".
 * The intent of this event is to improve server performance, so it fires even if the spawning might fail later, for
 * example when the entity would be unable to spawn due to limited space or lighting.
 * <p>
 * Currently: NATURAL and SPAWNER based reasons. <!-- Please submit a Pull Request for future additions. -->
 * Also, Plugins that replace Entity Registrations with their own custom entities might not fire this event.
 */
public interface PreCreatureSpawnEvent extends Event, Cancellable {

    /**
     * @return The location this creature is being spawned at
     */
    Location getSpawnLocation();

    /**
     * @return The type of creature being spawned
     */
    EntityType getType();

    /**
     * @return Reason this creature is spawning (ie, NATURAL vs SPAWNER)
     */
    CreatureSpawnEvent.SpawnReason getReason();

    /**
     * @return If the spawn process should be aborted vs trying more attempts
     */
    boolean shouldAbortSpawn();

    /**
     * Set this if you are more blanket blocking all types of these spawns, and wish to abort the spawn process from
     * trying more attempts after this cancellation.
     *
     * @param shouldAbortSpawn Set if the spawn process should be aborted vs trying more attempts
     */
    void setShouldAbortSpawn(boolean shouldAbortSpawn);

    /**
     * @return If the spawn of this creature is cancelled or not
     */
    @Override
    boolean isCancelled();

    /**
     * Cancelling this event is more efficient than cancelling {@link CreatureSpawnEvent}
     *
     * @param cancel {@code true} if you wish to cancel this event, and abort the spawn of this creature
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
