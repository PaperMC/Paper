package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CraftPlayerToggleSneakEvent extends CraftPlayerEvent implements PlayerToggleSneakEvent {

    private final boolean isSneaking;
    private boolean cancelled;

    public CraftPlayerToggleSneakEvent(final Player player, final boolean isSneaking) {
        super(player);
        this.isSneaking = isSneaking;
    }

    @Override
    public boolean isSneaking() {
        return this.isSneaking;
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
        return PlayerToggleSneakEvent.getHandlerList();
    }
}
