package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a player's item breaks (such as a shovel or flint and steel).
 * <p>
 * After this event, the item's amount will be set to {@code item amount - 1}
 * and its durability will be reset to 0.
 */
public class PlayerItemBreakEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack brokenItem;

    public PlayerItemBreakEvent(@NotNull final Player player, @NotNull final ItemStack brokenItem) {
        super(player);
        this.brokenItem = brokenItem;
    }

    /**
     * Gets the item that broke
     *
     * @return The broken item
     */
    @NotNull
    public ItemStack getBrokenItem() {
        return brokenItem;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
