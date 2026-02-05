package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftPlayerSwapHandItemsEvent extends CraftPlayerEvent implements PlayerSwapHandItemsEvent {

    private ItemStack mainHandItem;
    private ItemStack offHandItem;

    private boolean cancelled;

    public CraftPlayerSwapHandItemsEvent(final Player player, final ItemStack mainHandItem, final ItemStack offHandItem) {
        super(player);

        this.mainHandItem = mainHandItem;
        this.offHandItem = offHandItem;
    }

    @Override
    public ItemStack getMainHandItem() {
        return this.mainHandItem;
    }

    @Override
    public void setMainHandItem(final @Nullable ItemStack mainHandItem) {
        this.mainHandItem = mainHandItem == null ? ItemStack.empty() : mainHandItem;
    }

    @Override
    public ItemStack getOffHandItem() {
        return this.offHandItem;
    }

    @Override
    public void setOffHandItem(final @Nullable ItemStack offHandItem) {
        this.offHandItem = offHandItem == null ? ItemStack.empty() : offHandItem;
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
        return PlayerSwapHandItemsEvent.getHandlerList();
    }
}
