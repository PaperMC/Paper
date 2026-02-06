package io.papermc.paper.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jetbrains.annotations.Range;

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
public interface PlayerPickItemEvent extends PlayerEventNew, Cancellable {

    /**
     * Checks whether the player wants block/entity data included.
     *
     * @return {@code true} if data is included, otherwise {@code false}.
     */
    boolean isIncludeData();

    /**
     * Returns the slot the item that is being picked goes into.
     *
     * @return hotbar slot (0-8 inclusive)
     */
    @Range(from = 0, to = 8) int getTargetSlot();

    /**
     * Changes the slot the item that is being picked goes into.
     *
     * @param targetSlot hotbar slot (0-8 inclusive)
     */
    void setTargetSlot(@Range(from = 0, to = 8) int targetSlot);

    /**
     * Returns the slot in which the item that will be put into the players hotbar is located.
     * <p>
     * Returns {@code -1} if the item is not in the player's inventory.
     * If this is the case and the player is in creative mode, the item will be spawned in.
     *
     * @return player inventory slot (0-35 inclusive, or {@code -1} if not in the player inventory)
     */
    @Range(from = -1, to = 35) int getSourceSlot();

    /**
     * Change the source slot from which the item that will be put in the players hotbar will be taken.
     * <p>
     * If set to {@code -1} and the player is in creative mode, the item will be spawned in.
     *
     * @param sourceSlot player inventory slot (0-35 inclusive, or {@code -1} if not in the player inventory)
     */
    void setSourceSlot(@Range(from = -1, to = 35) int sourceSlot);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
