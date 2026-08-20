package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerSpawnChangeEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerSpawnChangeEvent extends CraftPlayerEvent implements PlayerSpawnChangeEvent {

    private final Cause cause;
    private @Nullable Location newSpawn;
    private boolean forced;

    private boolean cancelled;

    public CraftPlayerSpawnChangeEvent(final Player player, final @Nullable Location newSpawn, final boolean forced, final Cause cause) {
        super(player);
        this.newSpawn = newSpawn;
        this.cause = cause;
        this.forced = forced;
    }

    @Override
    public @Nullable Location getNewSpawn() {
        return this.newSpawn;
    }

    @Override
    public void setNewSpawn(final @Nullable Location newSpawn) {
        if (newSpawn != null) {
            Preconditions.checkArgument(newSpawn.getWorld() != null, "Spawn location must have a world set");
            this.newSpawn = newSpawn.clone();
        } else {
            this.newSpawn = null;
        }
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public boolean isForced() {
        return this.forced;
    }

    @Override
    public void setForced(final boolean forced) {
        this.forced = forced;
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
        return PlayerSpawnChangeEvent.getHandlerList();
    }
}
