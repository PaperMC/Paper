package com.destroystokyo.paper.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player's spawn is set, either by themselves or otherwise.
 * <br>
 * Cancelling this event will prevent the spawn from being set.
 */
@NullMarked
public interface PlayerSetSpawnEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the cause of this event.
     *
     * @return the cause
     */
    Cause getCause();

    /**
     * Gets the location that the spawn is set to. The yaw
     * of this location is the spawn angle. Mutating this location
     * will change the resulting spawn point of the player. Use
     * {@link Location#clone()} to get a copy of this location.
     *
     * @return the spawn location, or {@code null} if removing the location
     */
    @Nullable Location getLocation();

    /**
     * Sets the location to be set as the spawn location. The yaw
     * of this location is the spawn angle.
     *
     * @param location the spawn location, or {@code null} to remove the spawn location
     */
    void setLocation(@Nullable Location location);

    /**
     * Gets if this is a force spawn location
     *
     * @return {@code true} if forced
     */
    boolean isForced();

    /**
     * Sets if this is a forced spawn location
     *
     * @param forced {@code true} to force
     */
    void setForced(boolean forced);

    /**
     * Gets if this action will notify the player their spawn
     * has been set.
     *
     * @return {@code true} to notify
     */
    boolean willNotifyPlayer();

    /**
     * Sets if this action will notify the player that their spawn
     * has been set.
     *
     * @param notifyPlayer {@code true} to notify
     */
    void setNotifyPlayer(boolean notifyPlayer);

    /**
     * Gets the notification message that will be sent to the player
     * if {@link #willNotifyPlayer()} returns {@code true}.
     *
     * @return {@code null} if no notification
     */
    @Nullable Component getNotification();

    /**
     * Sets the notification message that will be sent to the player.
     *
     * @param notification {@code null} to send no message
     */
    void setNotification(@Nullable Component notification);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {
        /**
         * When a player interacts successfully with a bed.
         */
        BED,
        /**
         * When a player interacts successfully with a respawn anchor.
         */
        RESPAWN_ANCHOR,
        /**
         * When a player respawns.
         */
        PLAYER_RESPAWN,
        /**
         * When the {@code /spawnpoint} command is used on a player.
         */
        COMMAND,
        /**
         * When a plugin uses {@link Player#setRespawnLocation(Location)} or
         * {@link Player#setRespawnLocation(Location, boolean)}.
         */
        PLUGIN,
        /**
         * Fallback cause.
         */
        UNKNOWN,
    }
}
