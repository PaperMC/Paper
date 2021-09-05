package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds information for player teleport events
 */
public class PlayerTeleportEvent extends PlayerMoveEvent {
    private static final HandlerList handlers = new HandlerList();
    private TeleportCause cause = TeleportCause.UNKNOWN;

    // Paper start - Teleport API
    private boolean dismounted = true;
    private final java.util.Set<io.papermc.paper.entity.TeleportFlag.Relative> teleportFlagSet;
    // Paper end

    public PlayerTeleportEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to) {
        super(player, from, to);
        teleportFlagSet = java.util.Collections.emptySet(); // Paper - Teleport API
    }

    public PlayerTeleportEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to, @NotNull final TeleportCause cause) {
        this(player, from, to);

        this.cause = cause;
    }

    // Paper start - Teleport API
    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerTeleportEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to, @NotNull final TeleportCause cause, @NotNull java.util.Set<io.papermc.paper.entity.TeleportFlag.@NotNull Relative> teleportFlagSet) {
        super(player, from, to);
        this.teleportFlagSet = teleportFlagSet;
        this.cause = cause;
    }
    // Paper end

    /**
     * Gets the cause of this teleportation event
     *
     * @return Cause of the event
     */
    @NotNull
    public TeleportCause getCause() {
        return cause;
    }

    public enum TeleportCause {
        /**
         * Indicates the teleporation was caused by a player throwing an Ender
         * Pearl
         */
        ENDER_PEARL,
        /**
         * Indicates the teleportation was caused by a player executing a
         * command
         */
        COMMAND,
        /**
         * Indicates the teleportation was caused by a plugin
         */
        PLUGIN,
        /**
         * Indicates the teleportation was caused by a player entering a
         * Nether portal
         */
        NETHER_PORTAL,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * portal
         */
        END_PORTAL,
        /**
         * Indicates the teleportation was caused by a player teleporting to a
         * Entity/Player via the spectator menu
         */
        SPECTATE,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * gateway
         */
        END_GATEWAY,
        /**
         * Indicates the teleportation was caused by a player consuming chorus
         * fruit
         */
        CHORUS_FRUIT,
        /**
         * Indicates the teleportation was caused by a player exiting a vehicle
         */
        DISMOUNT,
        /**
         * Indicates the teleportation was caused by a player exiting a bed
         */
        EXIT_BED,
        /**
         * Indicates the teleportation was caused by an event not covered by
         * this enum
         */
        UNKNOWN;
    }

    // Paper start - Teleport API
    /**
     * Gets if the player will be dismounted in this teleportation.
     *
     * @return dismounted or not
     * @deprecated dismounting on tp is no longer controlled by the server
     */
    @Deprecated(forRemoval = true)
    public boolean willDismountPlayer() {
        return this.dismounted;
    }

    /**
     * Returns the relative teleportation flags used in this teleportation.
     * This determines which axis the player will not lose their velocity in.
     *
     * @return an immutable set of relative teleportation flags
     */
    @NotNull
    public java.util.Set<io.papermc.paper.entity.TeleportFlag.@NotNull Relative> getRelativeTeleportationFlags() {
        return this.teleportFlagSet;
    }
    // Paper end

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
