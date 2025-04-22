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
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final ItemStack craftedItemStack;

    @ApiStatus.Internal
    public ItemCraftedEvent(final Player player, final ItemStack craftedItemStack) {
        this.player = player;
        this.craftedItemStack = craftedItemStack;
    }

    /**
     * Gets the player who triggered the event by picking up the crafted item.
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the itemstack that was crafted and picked up by the player.
     *
     * @return the itemstack
     */
    public ItemStack getCraftedItemStack() {
        return craftedItemStack.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
