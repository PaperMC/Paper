package org.bukkit.event.player;

import org.bukkit.entity.Fish;
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
    private int exp;
    private final State state;
    private final Fish hookEntity;

    public PlayerFishEvent(final Player player, final Entity entity, final Fish hookEntity, final State state) {
        super(player);
        this.entity = entity;
        this.hookEntity = hookEntity;
        this.state = state;
    }

    /**
     * Gets the entity caught by the player.
     * <p>
     * If player has fished successfully, the result may be cast to {@link
     * org.bukkit.entity.Item}.
     *
     * @return Entity caught by the player, Entity if fishing, and null if
     *     bobber has gotten stuck in the ground or nothing has been caught
     */
    public Entity getCaught() {
        return entity;
    }

    /**
     * Gets the fishing hook.
     *
     * @return Fish the entity representing the fishing hook/bobber.
     */
    public Fish getHook() {
        return hookEntity;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the amount of experience received when fishing.
     * <p>
     * Note: This value has no default effect unless the event state is {@link
     * State#CAUGHT_FISH}.
     *
     * @return the amount of experience to drop
     */
    public int getExpToDrop() {
        return exp;
    }

    /**
     * Sets the amount of experience received when fishing.
     * <p>
     * Note: This value has no default effect unless the event state is {@link
     * State#CAUGHT_FISH}.
     *
     * @param amount the amount of experience to drop
     */
    public void setExpToDrop(int amount) {
        exp = amount;
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
         * When a player is fishing, ie casting the line out.
         */
        FISHING,
        /**
         * When a player has successfully caught a fish and is reeling it in. In
         * this instance, a "fish" is any item retrieved from water as a result
         * of fishing, ie an item, but not necessarily a fish.
         */
        CAUGHT_FISH,
        /**
         * When a player has successfully caught an entity. This refers to any
         * already spawned entity in the world that has been hooked directly by
         * the rod.
         */
        CAUGHT_ENTITY,
        /**
         * When a bobber is stuck in the ground.
         */
        IN_GROUND,
        /**
         * When a player fails to catch anything while fishing usually due to
         * poor aiming or timing.
         */
        FAILED_ATTEMPT,
        /**
         * Called when there is a bite on the hook and it is ready to be reeled
         * in.
         */
        BITE
    }
}
