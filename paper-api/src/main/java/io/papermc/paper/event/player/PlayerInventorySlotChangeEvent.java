package io.papermc.paper.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a slot contents change in a player's inventory.
 */
public interface PlayerInventorySlotChangeEvent extends PlayerEvent {

    /**
     * The raw slot number that was changed.
     *
     * @return The raw slot number.
     */
    int getRawSlot();

    /**
     * The slot number that was changed, ready for passing to
     * {@link Inventory#getItem(int)}. Note that there may be two slots with
     * the same slot number, since a view links two different inventories.
     * <p>
     * If no inventory is opened, internal crafting view is used for conversion.
     *
     * @return The slot number.
     */
    int getSlot();

    /**
     * Clone of ItemStack that was in the slot before the change.
     *
     * @return The old ItemStack in the slot.
     */
    ItemStack getOldItemStack();

    /**
     * Clone of ItemStack that is in the slot after the change.
     *
     * @return The new ItemStack in the slot.
     */
    ItemStack getNewItemStack();

    /**
     * Gets whether the slot change advancements will be triggered.
     *
     * @return Whether the slot change advancements will be triggered.
     */
    boolean shouldTriggerAdvancements();

    /**
     * Sets whether the slot change advancements will be triggered.
     *
     * @param triggerAdvancements Whether the slot change advancements will be triggered.
     */
    void setShouldTriggerAdvancements(boolean triggerAdvancements);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
