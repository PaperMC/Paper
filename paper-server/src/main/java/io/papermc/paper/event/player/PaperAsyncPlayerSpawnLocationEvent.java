package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperAsyncPlayerSpawnLocationEvent extends CraftEvent implements AsyncPlayerSpawnLocationEvent {

    private final PlayerConfigurationConnection connection;
    private final boolean newPlayer;
    private Location spawnLocation;

    public PaperAsyncPlayerSpawnLocationEvent(final PlayerConfigurationConnection connection, final Location spawnLocation, final boolean newPlayer) {
        super(true);
        this.connection = connection;
        this.spawnLocation = spawnLocation;
        this.newPlayer = newPlayer;
    }

    @Override
    public PlayerConfigurationConnection getConnection() {
        return this.connection;
    }

    @Override
    public Location getSpawnLocation() {
        return this.spawnLocation.clone();
    }

    @Override
    public void setSpawnLocation(final Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "World cannot be null");
        this.spawnLocation = location.clone();
    }

    @Override
    public boolean isNewPlayer() {
        return this.newPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncPlayerSpawnLocationEvent.getHandlerList();
    }
}
