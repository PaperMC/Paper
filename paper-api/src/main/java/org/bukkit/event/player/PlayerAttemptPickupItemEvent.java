package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Thrown when a player attempts to pick an item up from the ground
 *
 * @since 1.12
 */
@NullMarked
public class PlayerAttemptPickupItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Item item;
    private final int remaining;
    private boolean flyAtPlayer = true;

    private boolean cancelled;

    @Deprecated // Remove in 1.13 // Remove in 1.14?
    @ApiStatus.Internal
    public PlayerAttemptPickupItemEvent(final Player player, final Item item) {
        this(player, item, 0);
    }

    @ApiStatus.Internal
    public PlayerAttemptPickupItemEvent(final Player player, final Item item, final int remaining) {
        super(player);
        this.item = item;
        this.remaining = remaining;
    }

    /**
     * Gets the Item attempted by the player.
     *
     * @return Item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Gets the amount that will remain on the ground, if any
     *
     * @return amount that will remain on the ground
     * @since 1.12.2
     */
    public int getRemaining() {
        return this.remaining;
    }

    /**
     * Set if the item will fly at the player
     * <p>Cancelling the event will set this value to false.</p>
     *
     * @param flyAtPlayer {@code true} for item to fly at player
     * @since 1.12.2
     */
    public void setFlyAtPlayer(boolean flyAtPlayer) {
        this.flyAtPlayer = flyAtPlayer;
    }

    /**
     * Gets if the item will fly at the player
     *
     * @return {@code true} if the item will fly at the player
     * @since 1.12.2
     */
    public boolean getFlyAtPlayer() {
        return this.flyAtPlayer;
    }

    /**
     * @since 1.12.2
     */

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @since 1.12.2
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        this.flyAtPlayer = !cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
