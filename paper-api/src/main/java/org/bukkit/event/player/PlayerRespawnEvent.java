package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import java.util.Set;

/**
 * Called when a player respawns.
 * <p>
 * If changing player state, see {@link com.destroystokyo.paper.event.player.PlayerPostRespawnEvent}
 * because the player is "reset" between this event and that event and some changes won't persist.
 */
public class PlayerRespawnEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean isBedSpawn;
    private final boolean isAnchorSpawn;
    private final RespawnReason respawnReason;
    private final Set<RespawnFlag> respawnFlags;
    private Location respawnLocation;

    @ApiStatus.Internal
    @Deprecated(since = "1.16.1", forRemoval = true)
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn) {
        this(respawnPlayer, respawnLocation, isBedSpawn, false);
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.19.4", forRemoval = true)
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn) {
        this(respawnPlayer, respawnLocation, isBedSpawn, isAnchorSpawn, RespawnReason.PLUGIN);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn, @NotNull final RespawnReason respawnReason) {
        this(respawnPlayer, respawnLocation, isBedSpawn, isAnchorSpawn, respawnReason, com.google.common.collect.ImmutableSet.builder());
    }

    @ApiStatus.Internal
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn, @NotNull final RespawnReason respawnReason, @NotNull final com.google.common.collect.ImmutableSet.Builder<org.bukkit.event.player.PlayerRespawnEvent.RespawnFlag> respawnFlags) {
        super(respawnPlayer);
        this.respawnLocation = respawnLocation;
        this.isBedSpawn = isBedSpawn;
        this.isAnchorSpawn = isAnchorSpawn;
        this.respawnReason = respawnReason;
        if (this.isBedSpawn) { respawnFlags.add(RespawnFlag.BED_SPAWN); }
        if (this.isAnchorSpawn) { respawnFlags.add(RespawnFlag.ANCHOR_SPAWN); }
        this.respawnFlags = respawnFlags.build();
    }

    /**
     * Gets the current respawn location
     *
     * @return Location current respawn location
     */
    @NotNull
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }

    /**
     * Sets the new respawn location
     *
     * @param respawnLocation new location for the respawn
     */
    public void setRespawnLocation(@NotNull Location respawnLocation) {
        Preconditions.checkArgument(respawnLocation != null, "Respawn location can not be null");
        Preconditions.checkArgument(respawnLocation.getWorld() != null, "Respawn world can not be null");

        this.respawnLocation = respawnLocation.clone();
    }

    /**
     * Gets whether the respawn location is the player's bed.
     *
     * @return {@code true} if the respawn location is the player's bed.
     */
    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }

    /**
     * Gets whether the respawn location is the player's respawn anchor.
     *
     * @return {@code true} if the respawn location is the player's respawn anchor.
     */
    public boolean isAnchorSpawn() {
        return this.isAnchorSpawn;
    }

    /**
     * Gets the reason this respawn event was called.
     *
     * @return the reason the event was called.
     */
    @NotNull
    public RespawnReason getRespawnReason() {
        return this.respawnReason;
    }

    /**
     * Get the set of flags that apply to this respawn.
     *
     * @return an immutable set of the flags that apply to this respawn
     */
    @NotNull
    public @Unmodifiable Set<RespawnFlag> getRespawnFlags() {
        return this.respawnFlags;
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

    /**
     * An enum to specify the reason a respawn event was called.
     */
    public enum RespawnReason {

        /**
         * When the player dies and presses the respawn button.
         */
        DEATH,
        /**
         * When the player exits the end through the end portal.
         */
        END_PORTAL,
        /**
         * When a plugin respawns the player.
         */
        PLUGIN
    }

    public enum RespawnFlag {
        /**
         * Will use the bed spawn location
         */
        BED_SPAWN,
        /**
         * Will use the respawn anchor location
         */
        ANCHOR_SPAWN,
        /**
         * Is caused by going to the end portal in the end.
         */
        END_PORTAL
    }
}
