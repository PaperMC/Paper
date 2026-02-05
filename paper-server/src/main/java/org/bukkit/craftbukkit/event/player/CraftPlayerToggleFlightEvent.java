package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class CraftPlayerToggleFlightEvent extends CraftPlayerEvent implements PlayerToggleFlightEvent {

    private final boolean isFlying;
    private boolean cancelled;

    public CraftPlayerToggleFlightEvent(final Player player, final boolean isFlying) {
        super(player);
        this.isFlying = isFlying;
    }

    @Override
    public boolean isFlying() {
        return this.isFlying;
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
        return PlayerToggleFlightEvent.getHandlerList();
    }
}
