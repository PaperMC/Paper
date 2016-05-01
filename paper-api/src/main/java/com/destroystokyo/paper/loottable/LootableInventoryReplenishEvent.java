package com.destroystokyo.paper.loottable;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class LootableInventoryReplenishEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LootableInventory inventory;
    private boolean cancelled;

    @ApiStatus.Internal
    public LootableInventoryReplenishEvent(final Player player, final LootableInventory inventory) {
        super(player);
        this.inventory = inventory;
    }

    public LootableInventory getInventory() {
        return this.inventory;
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
