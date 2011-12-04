package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Holds information for player teleport events
 */
public class PlayerTeleportEvent extends PlayerMoveEvent {
    private TeleportCause cause = TeleportCause.UNKNOWN;

    public PlayerTeleportEvent(Player player, Location from, Location to) {
        super(Type.PLAYER_TELEPORT, player, from, to);
    }

    public PlayerTeleportEvent(Player player, Location from, Location to, TeleportCause cause) {
        super(Type.PLAYER_TELEPORT, player, from, to);

        this.cause = cause;
    }

    public PlayerTeleportEvent(final Event.Type type, Player player, Location from, Location to) {
        super(type, player, from, to);
    }

    public PlayerTeleportEvent(final Event.Type type, Player player, Location from, Location to, TeleportCause cause) {
        super(type, player, from, to);

        this.cause = cause;
    }

    /**
     * Gets the cause of this teleportation event
     * @return Cause of the event
     */
    public TeleportCause getCause() {
        return cause;
    }

    public enum TeleportCause {
        /**
         * Indicates the teleporation was caused by a player throwing an Ender Pearl
         */
        ENDER_PEARL,
        /**
         * Indicates the teleportation was caused by a player executing a command
         */
        COMMAND,
        /**
         * Indicates the teleportation was caused by a plugin
         */
        PLUGIN,
        /**
         * Indicates the teleportation was caused by an event not covered by this enum
         */
        UNKNOWN;
    }
}
