package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PaperPreCreatureSpawnEvent extends CraftEvent implements PreCreatureSpawnEvent {

    private final Location location;
    private final EntityType type;
    private final CreatureSpawnEvent.SpawnReason reason;
    private boolean shouldAbortSpawn;

    private boolean cancelled;

    public PaperPreCreatureSpawnEvent(final Location location, final EntityType type, final CreatureSpawnEvent.SpawnReason reason) {
        this.location = location;
        this.type = type;
        this.reason = reason;
    }

    @Override
    public Location getSpawnLocation() {
        return this.location.clone();
    }

    @Override
    public EntityType getType() {
        return this.type;
    }

    @Override
    public CreatureSpawnEvent.SpawnReason getReason() {
        return this.reason;
    }

    @Override
    public boolean shouldAbortSpawn() {
        return this.shouldAbortSpawn;
    }

    @Override
    public void setShouldAbortSpawn(final boolean shouldAbortSpawn) {
        this.shouldAbortSpawn = shouldAbortSpawn;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PreCreatureSpawnEvent.getHandlerList();
    }
}
