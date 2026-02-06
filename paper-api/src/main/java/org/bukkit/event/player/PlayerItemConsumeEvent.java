package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * This event will fire when a player is finishing consuming an item (food,
 * potion, milk bucket).
 * <br>
 * If the ItemStack is modified the server will use the effects of the new
 * item and not remove the original one from the player's inventory.
 * <br>
 * If the event is cancelled the effect will not be applied and the item will
 * not be removed from the player's inventory.
 */
public interface PlayerItemConsumeEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the item that is being consumed. Modifying the returned item will
     * have no effect, you must use {@link
     * #setItem(org.bukkit.inventory.ItemStack)} instead.
     *
     * @return an ItemStack for the item being consumed
     */
    ItemStack getItem();

    /**
     * Set the item being consumed
     *
     * @param item the item being consumed
     */
    void setItem(@Nullable ItemStack item);

    /**
     * Get the hand used to consume the item.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    /**
     * Return the custom item stack that will replace the consumed item, or {@code null} if no
     * custom replacement has been set (which means the default replacement will be used).
     *
     * @return The custom item stack that will replace the consumed item or {@code null}
     */
    @Nullable ItemStack getReplacement();

    /**
     * Set a custom item stack to replace the consumed item. Pass {@code null} to clear any custom
     * stack that has been set and use the default replacement.
     *
     * @param replacement Replacement item to set, {@code null} to clear any custom stack and use default
     */
    void setReplacement(@Nullable ItemStack replacement);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
