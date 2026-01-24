package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class CraftPlayerItemHeldEvent extends CraftPlayerEvent implements PlayerItemHeldEvent {

    private final int previousSlot;
    private final int newSlot;

    private boolean cancelled;

    public CraftPlayerItemHeldEvent(final Player player, final int previousSlot, final int newSlot) {
        super(player);
        this.previousSlot = previousSlot;
        this.newSlot = newSlot;
    }

    @Override
    public int getPreviousSlot() {
        return this.previousSlot;
    }

    @Override
    public int getNewSlot() {
        return this.newSlot;
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
        return PlayerItemHeldEvent.getHandlerList();
    }
}
