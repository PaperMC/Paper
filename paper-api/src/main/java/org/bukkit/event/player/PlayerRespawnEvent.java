package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player respawns.
 * <p>
 * If changing player state, see {@link com.destroystokyo.paper.event.player.PlayerPostRespawnEvent}
 * because the player is "reset" between this event and that event and some changes won't persist.
 */
public class PlayerRespawnEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Location respawnLocation;
    private final boolean isBedSpawn;
    private final boolean isAnchorSpawn;
    private final boolean missingRespawnBlock;
    private final RespawnReason respawnReason;
    private final java.util.Set<RespawnFlag> respawnFlags;

    @Deprecated(since = "1.16.1", forRemoval = true)
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn) {
        this(respawnPlayer, respawnLocation, isBedSpawn, false);
    }

    @Deprecated(since = "1.19.4", forRemoval = true)
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn) {
        this(respawnPlayer, respawnLocation, isBedSpawn, false, RespawnReason.PLUGIN);
    }

    @Deprecated(forRemoval = true)
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn, @NotNull final RespawnReason respawnReason) {
        this(respawnPlayer, respawnLocation, isBedSpawn, isAnchorSpawn, false, respawnReason, com.google.common.collect.ImmutableSet.builder());
    }

    @ApiStatus.Internal
    public PlayerRespawnEvent(@NotNull final Player respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn, final boolean missingRespawnBlock, @NotNull final RespawnReason respawnReason, @NotNull final com.google.common.collect.ImmutableSet.Builder<org.bukkit.event.player.PlayerRespawnEvent.RespawnFlag> respawnFlags) {
        super(respawnPlayer);
        this.respawnLocation = respawnLocation;
        this.isBedSpawn = isBedSpawn;
        this.isAnchorSpawn = isAnchorSpawn;
        this.respawnReason = respawnReason;
        this.missingRespawnBlock = missingRespawnBlock;
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
     * @return true if the respawn location is the player's bed.
     */
    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }

    /**
     * Gets whether the respawn location is the player's respawn anchor.
     *
     * @return true if the respawn location is the player's respawn anchor.
     */
    public boolean isAnchorSpawn() {
        return isAnchorSpawn;
    }

    /**
     * Gets whether the player is missing a valid respawn block.
     * <p>
     * This will occur if the players respawn block is obstructed,
     * or it is the first death after it was either destroyed or
     * in case of a respawn anchor, ran out of charges.
     *
     * @return whether the player is missing a valid respawn block
     */
    public boolean isMissingRespawnBlock() {
        return this.missingRespawnBlock;
    }

    /**
     * Gets the reason this respawn event was called.
     *
     * @return the reason the event was called.
     */
    @NotNull
    public RespawnReason getRespawnReason() {
        return respawnReason;
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
        PLUGIN;
    }

    /**
     * Get the set of flags that apply to this respawn.
     *
     * @return an immutable set of the flags that apply to this respawn
     */
    @NotNull
    public java.util.Set<RespawnFlag> getRespawnFlags() {
        return respawnFlags;
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
        END_PORTAL,
    }
}
