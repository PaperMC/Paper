package io.papermc.paper.event.entity;

import org.bukkit.entity.Player;
import org.bukkit.entity.SulfurCube;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a SulfurCube swallows an item.
 * <p>
 * If the ItemStack is modified, the SulfurCube will swallow the new item
 * and not remove the original one from the player's inventory.
 * <p>
 * If the event is cancelled, the SulfurCube will not swallow the item, and
 * it will not be removed from the player's inventory.
 */
public interface SulfurCubeSwallowItemEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the player interacting with the SulfurCube.
     *
     * @return the player that interacted with the SulfurCube
     */
    Player getPlayer();

    /**
     * Gets the item that is currently swallowed by the SulfurCube.
     *
     * @return an ItemStack for the item currently swallowed
     */
    ItemStack getOldItem();

    /**
     * Gets the item that is being swallowed. Modifying the returned item will
     * have no effect, you must use {@link
     * #setNewItem(org.bukkit.inventory.ItemStack)} instead.
     *
     * @return an item being swallowed
     */
    ItemStack getNewItem();

    /**
     * Set the item being swallowed.
     *
     * @param newItem the item being swallowed
     */
    void setNewItem(ItemStack newItem);

    @Override
    SulfurCube getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
