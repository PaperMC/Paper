package io.papermc.paper.event.player;

import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when an {@link ItemFrame} is having an item rotated, added, or removed from it.
 */
public interface PlayerItemFrameChangeEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the {@link ItemFrame} involved in this event.
     *
     * @return the {@link ItemFrame}
     */
    ItemFrame getItemFrame();

    /**
     * Gets the {@link ItemStack} involved in this event.
     * This is the item being added, rotated, or removed from the {@link ItemFrame}.
     * <p>
     * If this method returns air, then the resulting item in the ItemFrame will be empty.
     *
     * @return the {@link ItemStack} being added, rotated, or removed
     */
    ItemStack getItemStack();

    /**
     * Sets the {@link ItemStack} that this {@link ItemFrame} holds.
     * If {@code null} is provided, the ItemStack will become air and the result in the ItemFrame will be empty.
     *
     * @param itemStack {@link ItemFrame} item
     */
    void setItemStack(@Nullable ItemStack itemStack);

    /**
     * Gets the action that was performed on this {@link ItemFrame}.
     *
     * @return action performed on the item frame in this event
     */
    ItemFrameChangeAction getAction();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum ItemFrameChangeAction {
        PLACE,
        REMOVE,
        ROTATE
    }
}
