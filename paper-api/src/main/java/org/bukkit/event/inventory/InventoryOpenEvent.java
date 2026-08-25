package org.bukkit.event.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player opens an inventory
 */
public interface InventoryOpenEvent extends InventoryEvent, Cancellable {

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    HumanEntity getPlayer(); // todo PlayerEvent?

    /**
     * Gets the title override set by another event or {@code null}
     * if not set.
     *
     * @return the title override or {@code null}
     */
    @Nullable Component titleOverride();

    /**
     * Sets the title override or clears the override.
     * <p>
     * This is only the title sent to the client in the open packet, this doesn't change
     * the title returned by {@link InventoryView#title()}, hence "override".
     * <p>
     * <b>NOTE:</b> Horse and nautilus inventories are a special case where setting this will
     * have no effect. These inventory titles are set by the entity's display name.
     *
     * @param titleOverride the title override or {@code null}
     */
    void titleOverride(@Nullable Component titleOverride);

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the inventory screen will not
     * show.
     */
    @Override
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the inventory screen will not
     * show.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
