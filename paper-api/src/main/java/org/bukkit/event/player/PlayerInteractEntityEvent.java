package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Represents an event that is called when a player right clicks an entity.
 */
public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {
    protected Entity clickedEntity;
    boolean cancelled = false;

    public PlayerInteractEntityEvent(Player who, Entity clickedEntity) {
        super(Type.PLAYER_INTERACT_ENTITY, who);
        this.clickedEntity = clickedEntity;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the entity that was rightclicked by the player.
     *
     * @return entity right clicked by player
     */
    public Entity getRightClicked() {
        return this.clickedEntity;
    }
}
