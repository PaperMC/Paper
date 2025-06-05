package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

/**
 * Event that is fired when a player uses the pick item functionality
 * (middle-clicking a {@link PlayerPickBlockEvent block}
 * or {@link PlayerPickEntityEvent entity} to get the appropriate item).
 * After the handling of this event, the contents of the source and the target slot will be swapped,
 * and the currently selected hotbar slot of the player will be set to the target slot.
 *
 * @see PlayerPickEntityEvent
 * @see PlayerPickBlockEvent
 */
@NullMarked
public abstract class PlayerPickItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean includeData;

    private int targetSlot;
    private int sourceSlot;

    private boolean cancelled;

    @ApiStatus.Internal
    protected PlayerPickItemEvent(final Player player, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player);
        this.includeData = includeData;
        this.targetSlot = targetSlot;
        this.sourceSlot = sourceSlot;
    }

    /**
     * Checks whether the player wants block/entity data included.
     *
     * @return {@code true} if data is included, otherwise {@code false}.
     */
    public boolean isIncludeData() {
        return includeData;
    }

    /**
     * Returns the slot the item that is being picked goes into.
     *
     * @return hotbar slot (0-8 inclusive)
     */
    public @Range(from = 0, to = 8) int getTargetSlot() {
        return this.targetSlot;
    }

    /**
     * Changes the slot the item that is being picked goes into.
     *
     * @param targetSlot hotbar slot (0-8 inclusive)
     */
    public void setTargetSlot(final @Range(from = 0, to = 8) int targetSlot) {
        Preconditions.checkArgument(targetSlot >= 0 && targetSlot <= 8, "Target slot must be in range 0 - 8 (inclusive)");
        this.targetSlot = targetSlot;
    }

    /**
     * Returns the slot in which the item that will be put into the players hotbar is located.
     * <p>
     * Returns {@code -1} if the item is not in the player's inventory.
     * If this is the case and the player is in creative mode, the item will be spawned in.
     *
     * @return player inventory slot (0-35 inclusive, or {@code -1} if not in the player inventory)
     */
    public @Range(from = -1, to = 35) int getSourceSlot() {
        return this.sourceSlot;
    }

    /**
     * Change the source slot from which the item that will be put in the players hotbar will be taken.
     * <p>
     * If set to {@code -1} and the player is in creative mode, the item will be spawned in.
     *
     * @param sourceSlot player inventory slot (0-35 inclusive, or {@code -1} if not in the player inventory)
     */
    public void setSourceSlot(final @Range(from = -1, to = 35) int sourceSlot) {
        Preconditions.checkArgument(sourceSlot >= -1 && sourceSlot <= 35, "Source slot must be in range of the player's inventory slot, or -1");
        this.sourceSlot = sourceSlot;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
