package org.spigotmc.event.player;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Called when player is about to spawn in a world after joining the server.
 *
 * @deprecated The spawn location is selected during the configuration phase, before a player entity is normally
 * created. Using the result of {@link #getPlayer()} for anything related to the player entity is unreliable and may
 * cause issues. Retrieving {@link Player#getUniqueId()} and {@link Player#getName()} is safe. Prefer using
 * {@link io.papermc.paper.event.player.AsyncPlayerSpawnLocationEvent}.
 */
@Warning(value = true, reason = "Listening to this event causes the player to be created early. Using the player from this event will result in undefined behavior. Prefer AsyncPlayerSpawnLocationEvent.")
@Deprecated(since = "1.21.9", forRemoval = true)
public interface PlayerSpawnLocationEvent extends PlayerEvent {

    /**
     * Gets player's spawn location.
     * If the player {@link Player#hasPlayedBefore()}, it's going to default to the location inside player.dat file.
     * For new players, the default spawn location is the {@link World#getSpawnLocation() spawn location}
     * of the {@link Server#getRespawnWorld() respawn world}.
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

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
