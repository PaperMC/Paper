package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a player is fishing
 */
public class PlayerFishEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;
    private boolean cancel = false;
    private final State state;

    public PlayerFishEvent(final Player player, final Entity entity, final State state) {
        super(player);
        this.entity = entity;
        this.state = state;
    }

    /**
     * Gets the entity caught by the player
     *
     * @return Entity caught by the player, null if fishing, bobber has gotten stuck in the ground or nothing has been caught
     */
    public Entity getCaught() {
        return entity;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the state of the fishing
     *
     * @return A State detailing the state of the fishing
     */
    public State getState() {
        return state;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * An enum to specify the state of the fishing
     */
    public enum State {

        /**
         * When a player is fishing
         */
        FISHING,
        /**
         * When a player has successfully caught a fish
         */
        CAUGHT_FISH,
        /**
         * When a player has successfully caught an entity
         */
        CAUGHT_ENTITY,
        /**
         * When a bobber is stuck in the grund
         */
        IN_GROUND,
        /**
         * When a player fails to catch anything while fishing usually due to poor aiming or timing
         */
        FAILED_ATTEMPT,
    }
}
