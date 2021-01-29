package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when the player is leaving a bed.
 */
public class PlayerBedLeaveEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block bed;
    private boolean setBedSpawn;
    private boolean cancelled;

    public PlayerBedLeaveEvent(@NotNull final Player who, @NotNull final Block bed, boolean setBedSpawn) {
        super(who);
        this.bed = bed;
        this.setBedSpawn = setBedSpawn;
    }

    /**
     * Returns the bed block involved in this event.
     *
     * @return the bed block involved in this event
     */
    @NotNull
    public Block getBed() {
        return bed;
    }

    /**
     * Get if this event should set the new spawn location for the
     * {@link Player}.
     * <br>
     * This does not remove any existing spawn location, only prevent it from
     * being changed (if true).
     * <br>
     * To change a {@link Player}'s spawn location, use
     * {@link Player#setBedSpawnLocation(Location)}.
     *
     * @return true if the spawn location will be changed
     */
    public boolean shouldSetSpawnLocation() {
        return setBedSpawn;
    }

    /**
     * Set if this event should set the new spawn location for the
     * {@link Player}.
     * <br>
     * This will not remove any existing spawn location, only prevent it from
     * being changed (if true).
     * <br>
     * To change a {@link Player}'s spawn location, use
     * {@link Player#setBedSpawnLocation(Location)}.
     *
     * @param setBedSpawn true to change the new spawn location
     */
    public void setSpawnLocation(boolean setBedSpawn) {
        this.setBedSpawn = setBedSpawn;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
}
