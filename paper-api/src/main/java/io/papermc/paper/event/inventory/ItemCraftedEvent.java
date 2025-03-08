package io.papermc.paper.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player picks up a crafted item from the result slot of a crafting grid.
 */
public class ItemCraftedEvent extends Event{
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final ItemStack craftedItemStack;

    public ItemCraftedEvent(@NotNull Player player, @NotNull ItemStack craftedItemStack) {
        this.player = player;
        this.craftedItemStack = craftedItemStack;
    }

    /**
     * Gets the Player who triggered the event by picking up the crafted item.
     *
     * @return Player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the ItemStack that was crafted and picked up by the Player.
     *
     * @return ItemStack
     */
    @NotNull
    public ItemStack getCraftedItemStack() {
        return craftedItemStack;
    }

    @NotNull
    @Override
    public  HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
