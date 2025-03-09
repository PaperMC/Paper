package io.papermc.paper.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;


/**
 * Called when a player picks up a crafted item from the result slot of a crafting grid.
 */
@NullMarked
public class ItemCraftedEvent extends Event{
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final ItemStack craftedItemStack;

    public ItemCraftedEvent(Player player,ItemStack craftedItemStack) {
        this.player = player;
        this.craftedItemStack = craftedItemStack;
    }

    /**
     * Gets the Player who triggered the event by picking up the crafted item.
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the ItemStack that was crafted and picked up by the Player.
     *
     * @return ItemStack
     */
    public ItemStack getCraftedItemStack() {
        return craftedItemStack;
    }

    @Override
    public  HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
