package org.bukkit.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemCraftedEvent extends Event{
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final ItemStack craftedItem;

    public ItemCraftedEvent(@NotNull Player player, @NotNull ItemStack craftedItem) {
        this.player = player;
        this.craftedItem = craftedItem;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public ItemStack getCraftedItem() {
        return craftedItem;
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
