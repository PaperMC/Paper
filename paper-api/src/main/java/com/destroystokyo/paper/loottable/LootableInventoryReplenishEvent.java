package com.destroystokyo.paper.loottable;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface LootableInventoryReplenishEvent extends PlayerEventNew, Cancellable { // todo javadocs?

    LootableInventory getInventory();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
