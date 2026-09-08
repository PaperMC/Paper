package org.bukkit.craftbukkit.event.player;

import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class CraftPlayerPickupItemEvent extends CraftPlayerEvent implements PlayerPickupItemEvent {

    private final Item item;
    private final int remaining;
    private boolean flyAtPlayer = true;

    private boolean cancelled;

    public CraftPlayerPickupItemEvent(final Player player, final Item item, final int remaining) {
        super(player);
        this.item = item;
        this.remaining = remaining;
    }

    public CraftPlayerPickupItemEvent(final net.minecraft.world.entity.player.Player player, final ItemEntity item, final int remaining) {
        this((Player) player.getBukkitEntity(), (Item) item.getBukkitEntity(), remaining);
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
    public void setFlyAtPlayer(boolean flyAtPlayer) {
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
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        this.flyAtPlayer = !cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPickupItemEvent.getHandlerList();
    }
}
