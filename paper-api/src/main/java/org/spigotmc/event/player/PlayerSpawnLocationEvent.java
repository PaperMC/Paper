package org.spigotmc.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when player is about to spawn in a world after joining the server.
 */
public class PlayerSpawnLocationEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Location spawnLocation;

    public PlayerSpawnLocationEvent(@NotNull final Player who, @NotNull Location spawnLocation) {
        super(who);
        this.spawnLocation = spawnLocation;
    }


    /**
     * Gets player's spawn location.
     * If the player {@link Player#hasPlayedBefore()}, it's going to default to the location inside player.dat file.
     * For new players, the default spawn location is spawn of the main Bukkit world.
     *
     * @return the spawn location
     */
    @NotNull
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * Sets player's spawn location.
     *
     * @param location the spawn location
     */
    public void setSpawnLocation(@NotNull Location location) {
        this.spawnLocation = location;
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
