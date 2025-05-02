package org.bukkit.event.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player opens an inventory
 */
public class InventoryOpenEvent extends InventoryEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Component titleOverride;
    private boolean cancelled;

    @ApiStatus.Internal
    public InventoryOpenEvent(@NotNull InventoryView transaction) {
        super(transaction);
    }

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    @NotNull
    public final HumanEntity getPlayer() {
        return this.transaction.getPlayer();
    }

    /**
     * Gets the title override set by another event or {@code null}
     * if not set.
     *
     * @return the title override or {@code null}
     */
    public @Nullable Component titleOverride() {
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
     * @param titleOverride the title override or {@code null}
     */
    public void titleOverride(@Nullable Component titleOverride) {
        this.titleOverride = titleOverride;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the inventory screen will not
     * show.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If this event is cancelled, the inventory screen will not
     * show.
     */
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
}
