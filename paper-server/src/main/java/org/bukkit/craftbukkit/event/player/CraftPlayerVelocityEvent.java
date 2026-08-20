package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class CraftPlayerVelocityEvent extends CraftPlayerEvent implements PlayerVelocityEvent {

    private Vector velocity;
    private boolean cancelled;

    public CraftPlayerVelocityEvent(final Player player, final Vector velocity) {
        super(player);
        this.velocity = velocity;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVelocity(final Vector velocity) {
        this.velocity = velocity.clone();
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
        return PlayerVelocityEvent.getHandlerList();
    }
}
