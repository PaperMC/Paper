package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player swap items between main hand and off-hand using the
 * hotkey.
 */
public class PlayerSwapHandItemsEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private ItemStack mainHandItem;
    private ItemStack offHandItem;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerSwapHandItemsEvent(@NotNull Player player, @NotNull ItemStack mainHandItem, @NotNull ItemStack offHandItem) {
        super(player);

        this.mainHandItem = mainHandItem;
        this.offHandItem = offHandItem;
    }

    /**
     * Gets the item switched to the main hand.
     *
     * @return item in the main hand
     */
    @NotNull
    public ItemStack getMainHandItem() {
        return this.mainHandItem;
    }

    /**
     * Sets the item in the main hand.
     *
     * @param mainHandItem new item in the main hand
     */
    public void setMainHandItem(@Nullable ItemStack mainHandItem) {
        this.mainHandItem = mainHandItem == null ? ItemStack.empty() : mainHandItem;
    }

    /**
     * Gets the item switched to the off-hand.
     *
     * @return item in the off-hand
     */
    @NotNull // Paper
    public ItemStack getOffHandItem() {
        return this.offHandItem;
    }

    /**
     * Sets the item in the off-hand.
     *
     * @param offHandItem new item in the off-hand
     */
    public void setOffHandItem(@Nullable ItemStack offHandItem) {
        this.offHandItem = offHandItem == null ? ItemStack.empty() : offHandItem;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
