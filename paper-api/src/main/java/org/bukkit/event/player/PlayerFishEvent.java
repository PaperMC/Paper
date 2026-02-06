package org.bukkit.event.player;

import io.papermc.paper.event.entity.FishHookStateChangeEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

/**
 * Thrown when a player is fishing
 *
 * <p>If you want to monitor a fishhooks state transition, you can use {@link FishHookStateChangeEvent}.</p>
 */
public interface PlayerFishEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the entity caught by the player.
     * <p>
     * If player has fished successfully, the result may be cast to {@link org.bukkit.entity.Item}.
     *
     * @return Entity caught by the player, Entity if fishing, and {@code null} if
     *     bobber has gotten stuck in the ground or nothing has been caught
     */
    @Nullable Entity getCaught();

    /**
     * Gets the fishing hook.
     *
     * @return the entity representing the fishing hook/bobber.
     */
    FishHook getHook();

    /**
     * Get the hand that was used in this event.
     * <p>
     * The hand used is only present for player interactions.
     * This means it will be {@code null} if state is set
     * to {@link State#BITE} or {@link State#FAILED_ATTEMPT}.
     *
     * @return the hand
     */
    @Nullable EquipmentSlot getHand();

    /**
     * Gets the state of the fishing
     *
     * @return A State detailing the state of the fishing
     */
    State getState();

    /**
     * Gets the amount of experience received when fishing.
     * <p>
     * Note: This value has no default effect unless the event state is {@link
     * State#CAUGHT_FISH}.
     *
     * @return the amount of experience to drop
     */
    int getExpToDrop();

    /**
     * Sets the amount of experience received when fishing.
     * <p>
     * Note: This value has no default effect unless the event state is {@link
     * State#CAUGHT_FISH}.
     *
     * @param amount the amount of experience to drop
     */
    void setExpToDrop(int amount);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the state of the fishing
     */
    enum State {

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
         * When a player fails to catch a bite while fishing usually due to
         * poor timing.
         */
        FAILED_ATTEMPT,
        /**
         * When a player reels in their hook without receiving any bites.
         */
        REEL_IN,
        /**
         * Called when there is a bite on the hook and it is ready to be reeled
         * in.
         */
        BITE,
        /**
         * Called when a bobber was lured, and is now waiting to be hooked
         * (when a "fish" starts to swim toward the bobber to bite it).
         */
        LURED,
    }
}
