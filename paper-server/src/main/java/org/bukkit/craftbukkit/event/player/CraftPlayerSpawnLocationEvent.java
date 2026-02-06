package org.bukkit.craftbukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class CraftPlayerSpawnLocationEvent extends CraftPlayerEvent implements PlayerSpawnLocationEvent {

    private Location spawnLocation;

    public CraftPlayerSpawnLocationEvent(final Player player, final Location spawnLocation) {
        super(player);
        this.spawnLocation = spawnLocation;
    }

    @Override
    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    @Override
    public void setSpawnLocation(final Location location) {
        this.spawnLocation = location.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerSpawnLocationEvent.getHandlerList();
    }
}
