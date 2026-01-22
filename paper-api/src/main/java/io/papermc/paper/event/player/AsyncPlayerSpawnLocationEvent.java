package io.papermc.paper.event.player;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when selecting a player's spawn location (i.e. joining the server or reconfiguring).
 *
 * <p>The player will be kept in the configuration phase until all event handlers return and
 * the spawn location is loaded.</p>
 */
@ApiStatus.Experimental
@NullMarked
public interface AsyncPlayerSpawnLocationEvent extends Event {

    /**
     * Gets the spawning player's connection.
     *
     * @return the player connection
     */
    PlayerConfigurationConnection getConnection();

    /**
     * Gets the player's spawn location.
     *
     * <p>If the player has played on this server before, defaults to the location inside the player data file.
     * For new players, the default spawn location is the {@link World#getSpawnLocation() spawn location}
     * of the {@link Server#getRespawnWorld() respawn world}.</p>
     *
     * @return the spawn location
     */
    Location getSpawnLocation();

    /**
     * Sets player's spawn location.
     *
     * @param location the spawn location
     */
    void setSpawnLocation(Location location);

    /**
     * Returns true if the player is joining the server for the first time.
     *
     * @return whether the player is new
     */
    boolean isNewPlayer();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
