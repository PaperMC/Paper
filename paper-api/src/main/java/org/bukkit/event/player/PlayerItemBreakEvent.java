package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a player's item breaks (such as a shovel or flint and steel).
 * <p>
 * After this event, the item's amount will be set to {@code item amount - 1}
 * and its durability will be reset to 0.
 */
public class PlayerItemBreakEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack brokenItem;

    @ApiStatus.Internal
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
        return this.brokenItem;
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
