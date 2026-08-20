package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class CraftPlayerToggleSprintEvent extends CraftPlayerEvent implements PlayerToggleSprintEvent {

    private final boolean isSprinting;
    private boolean cancelled;

    public CraftPlayerToggleSprintEvent(final Player player, final boolean isSprinting) {
        super(player);
        this.isSprinting = isSprinting;
    }

    @Override
    public boolean isSprinting() {
        return this.isSprinting;
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
        return PlayerToggleSprintEvent.getHandlerList();
    }
}
