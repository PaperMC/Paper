package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player opens an inventory
 */
public class InventoryOpenEvent extends InventoryEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private net.kyori.adventure.text.Component titleOverride; // Paper

    public InventoryOpenEvent(@NotNull InventoryView transaction) {
        super(transaction);
        this.cancelled = false;
    }

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    @NotNull
    public final HumanEntity getPlayer() {
        return transaction.getPlayer();
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     * <p>
     * If an inventory open event is cancelled, the inventory screen will not
     * show.
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     * <p>
     * If an inventory open event is cancelled, the inventory screen will not
     * show.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    // Paper start
    /**
     * Gets the title override set by another event or null
     * if not set.
     *
     * @return the title override or null
     */
    public net.kyori.adventure.text.@org.jetbrains.annotations.Nullable Component titleOverride() {
        return this.titleOverride;
    }

    /**
     * Sets the title override or clears the override.
     * <p>
     * This is only the title sent to the client in the open packet, this doesn't change
     * the title returned by {@link InventoryView#title()}, hence "override".
     * <p>
     * <b>NOTE:</b> Horse inventories are a special case where setting this will
     * have no effect. Horse inventory titles are set by the horse display name.
     *
     * @param titleOverride the title override or null
     */
    public void titleOverride(net.kyori.adventure.text.@org.jetbrains.annotations.Nullable Component titleOverride) {
        this.titleOverride = titleOverride;
    }
    // Paper end

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
