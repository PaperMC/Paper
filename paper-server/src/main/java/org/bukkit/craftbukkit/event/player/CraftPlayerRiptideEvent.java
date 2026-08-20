package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CraftPlayerRiptideEvent extends CraftPlayerEvent implements PlayerRiptideEvent {

    private final ItemStack item;
    private final Vector velocity;

    private boolean cancelled;

    public CraftPlayerRiptideEvent(final Player player, final ItemStack item, final Vector velocity) {
        super(player);
        this.item = item;
        this.velocity = velocity;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity.clone();
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
        return PlayerRiptideEvent.getHandlerList();
    }
}
