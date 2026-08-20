package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * This event is fired when the spawn point of the player is changed.
 *
 * @deprecated use {@link com.destroystokyo.paper.event.player.PlayerSetSpawnEvent}
 */
@Deprecated(forRemoval = true)
public interface PlayerSpawnChangeEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the new spawn to be set.
     *
     * @return new spawn location
     */
    @Nullable Location getNewSpawn();

    /**
     * Sets the new spawn location.
     *
     * @param newSpawn new spawn location, with non-null world
     */
    void setNewSpawn(@Nullable Location newSpawn);

    /**
     * Gets the cause of spawn change.
     *
     * @return change cause
     */
    Cause getCause();

    /**
     * Gets if the spawn position will be used regardless of bed obstruction
     * rules.
     *
     * @return {@code true} if is forced
     */
    boolean isForced();

    /**
     * Sets if the spawn position will be used regardless of bed obstruction
     * rules.
     *
     * @param forced {@code true} if forced
     */
    void setForced(boolean forced);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {

        /**
         * Indicate the spawn was set by a command.
         */
        COMMAND,
        /**
         * Indicate the spawn was set by the player interacting with a bed.
         */
        BED,
        /**
         * Indicate the spawn was set by the player interacting with a respawn
         * anchor.
         */
        RESPAWN_ANCHOR,
        /**
         * Indicate the spawn was set by the use of plugins.
         */
        PLUGIN,
        /**
         * Indicate the spawn was reset by an invalid bed position or empty
         * respawn anchor.
         */
        RESET,
        /**
         * Indicate the spawn was caused by an unknown reason.
         */
        UNKNOWN
    }
}
