package io.papermc.paper.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player picks up a crafted item from the result slot of a crafting grid.
 */
@NullMarked
public class ItemCraftedEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final ItemStack craftedItem;

    @ApiStatus.Internal
    public ItemCraftedEvent(final Player player, final ItemStack craftedItem) {
        this.player = player;
        this.craftedItem = craftedItem;
    }

    /**
     * Gets the player who triggered the event by picking up the crafted item.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the item that was crafted and picked up by the player.
     *
     * @return the crafted item
     */
    public ItemStack getCraftedItem() {
        return this.craftedItem.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
