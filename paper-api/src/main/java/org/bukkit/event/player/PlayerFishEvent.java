package org.bukkit.event.player;

import io.papermc.paper.event.entity.FishHookStateChangeEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown when a player is fishing
 *
 * <p>If you want to monitor a fishhooks state transition, you can use {@link FishHookStateChangeEvent}.</p>
 */
public class PlayerFishEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity entity;
    private final FishHook hookEntity;
    private final EquipmentSlot hand;
    private final State state;
    private int exp;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerFishEvent(@NotNull final Player player, @Nullable final Entity entity, @NotNull final FishHook hookEntity, @Nullable EquipmentSlot hand, @NotNull final State state) {
        super(player);
        this.entity = entity;
        this.hookEntity = hookEntity;
        this.hand = hand;
        this.state = state;
    }

    @ApiStatus.Internal
    public PlayerFishEvent(@NotNull final Player player, @Nullable final Entity entity, @NotNull final FishHook hookEntity, @NotNull final State state) {
        this(player, entity, hookEntity, null, state);
    }

    /**
     * Gets the entity caught by the player.
     * <p>
     * If player has fished successfully, the result may be cast to {@link
     * org.bukkit.entity.Item}.
     *
     * @return Entity caught by the player, Entity if fishing, and {@code null} if
     *     bobber has gotten stuck in the ground or nothing has been caught
     */
    @Nullable
    public Entity getCaught() {
        return this.entity;
    }

    /**
     * Gets the fishing hook.
     *
     * @return the entity representing the fishing hook/bobber.
     */
    @NotNull
    public FishHook getHook() {
        return this.hookEntity;
    }

    /**
     * Get the hand that was used in this event.
     * <p>
     * The hand used is only present for player interactions.
     * This means it will be {@code null} if state is set
     * to {@link State#BITE} or {@link State#FAILED_ATTEMPT}.
     *
     * @return the hand
     */
    @Nullable
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Gets the state of the fishing
     *
     * @return A State detailing the state of the fishing
     */
    @NotNull
    public State getState() {
        return this.state;
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
        return this.exp;
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
        this.exp = amount;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
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
