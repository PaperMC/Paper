package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class CraftPlayerAttemptPickupItemEvent extends CraftPlayerEvent implements PlayerAttemptPickupItemEvent {

    private final Item item;
    private final int remaining;
    private boolean flyAtPlayer = true;

    private boolean cancelled;

    public CraftPlayerAttemptPickupItemEvent(final Player player, final Item item, final int remaining) {
        super(player);
        this.item = item;
        this.remaining = remaining;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public int getRemaining() {
        return this.remaining;
    }

    @Override
    public void setFlyAtPlayer(final boolean flyAtPlayer) {
        this.flyAtPlayer = flyAtPlayer;
    }

    @Override
    public boolean getFlyAtPlayer() {
        return this.flyAtPlayer;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
        this.flyAtPlayer = !cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerAttemptPickupItemEvent.getHandlerList();
    }
}
