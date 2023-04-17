package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event is fired when the spawn point of the player is changed.
 * @apiNote draft API
 */
@ApiStatus.Experimental
public class PlayerSpawnChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Cause cause;
    private Location newSpawn;
    private boolean forced;
    private boolean cancelled;

    public PlayerSpawnChangeEvent(@NotNull final Player player, @Nullable Location newSpawn, boolean forced, @NotNull final Cause cause) {
        super(player);
        this.newSpawn = newSpawn;
        this.cause = cause;
        this.forced = forced;
    }

    /**
     * Gets the cause of spawn change.
     *
     * @return change cause
     */
    @NotNull
    public Cause getCause() {
        return this.cause;
    }

    /**
     * Gets if the spawn position will be used regardless of bed obstruction
     * rules.
     *
     * @return true if is forced
     */
    public boolean isForced() {
        return this.forced;
    }

    /**
     * Sets if the spawn position will be used regardless of bed obstruction
     * rules.
     *
     * @param forced true if forced
     */
    public void setForced(boolean forced) {
        this.forced = forced;
    }

    /**
     * Gets the new spawn to be set.
     *
     * @return new spawn location
     */
    @Nullable
    public Location getNewSpawn() {
        return this.newSpawn;
    }

    /**
     * Sets the new spawn location.
     *
     * @param newSpawn new spawn location, with non-null world
     */
    public void setNewSpawn(@Nullable Location newSpawn) {
        if (newSpawn != null) {
            Preconditions.checkArgument(newSpawn.getWorld() != null, "Spawn location must have a world set");
            this.newSpawn = newSpawn.clone();
        } else {
            this.newSpawn = null;
        }
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum Cause {

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
        UNKNOWN;
    }
}
