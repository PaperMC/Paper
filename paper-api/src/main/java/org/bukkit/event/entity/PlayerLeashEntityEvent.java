package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called immediately prior to a creature being leashed by a player.
 */
public class PlayerLeashEntityEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity leashHolder;
    private final Entity entity;
    private boolean cancelled = false;
    private final Player player;

    public PlayerLeashEntityEvent(Entity what, Entity leashHolder, Player leasher) {
        this.leashHolder = leashHolder;
        this.entity = what;
        this.player = leasher;
    }

    /**
     * Returns the entity that is holding the leash.
     *
     * @return The leash holder
     */
    public Entity getLeashHolder() {
        return leashHolder;
    }

    /**
     * Returns the entity being leashed.
     *
     * @return The entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    public final Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled  = cancel;
    }
}
