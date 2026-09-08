package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerDropItemEvent;

public class CraftPlayerDropItemEvent extends CraftPlayerEvent implements PlayerDropItemEvent {

    private final Item drop;
    private boolean cancelled;

    public CraftPlayerDropItemEvent(final Player player, final Item drop) {
        super(player);
        this.drop = drop;
    }

    public CraftPlayerDropItemEvent(final ServerPlayer player, final ItemEntity drop) {
        this(player.getBukkitEntity(), (Item) drop.getBukkitEntity());
    }

    @Override
    public Item getItemDrop() {
        return this.drop;
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
        return PlayerDropItemEvent.getHandlerList();
    }
}
