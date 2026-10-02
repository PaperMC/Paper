package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/// This event is fired when the player is leaving a bed.
public class PlayerBedLeaveEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block bed;
    private boolean setBedSpawn;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerBedLeaveEvent(@NotNull final Player player, @NotNull final Block bed, boolean setBedSpawn) {
        super(player);
        this.bed = bed;
        this.setBedSpawn = setBedSpawn;
    }

    /// Returns the bed block involved in this event.
    ///
    /// @return the bed block involved in this event
    @NotNull
    public Block getBed() {
        return this.bed;
    }

    /// Get if this event should set the new spawn location for the
    /// [Player].
    ///
    /// This does not remove any existing spawn location, only prevent it from
    /// being changed (if true).
    ///
    /// To change a [Player]'s spawn location, use
    /// [Player#setBedSpawnLocation(Location)].
    ///
    /// @return `true` if the spawn location will be changed
    /// @deprecated the respawn point is now set when the player enter the bed and
    /// this option doesn't work since MC 1.15.
    @Deprecated(forRemoval = true)
    public boolean shouldSetSpawnLocation() {
        return this.setBedSpawn;
    }

    /// Set if this event should set the new spawn location for the
    /// [Player].
    ///
    /// This will not remove any existing spawn location, only prevent it from
    /// being changed (if true).
    ///
    /// To change a [Player]'s spawn location, use
    /// [Player#setBedSpawnLocation(Location)].
    ///
    /// @param setBedSpawn`true` to change the new spawn location
    /// @deprecated the respawn point is now set when the player enter the bed and
    /// this option doesn't work since MC 1.15.
    @Deprecated(forRemoval = true)
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
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
