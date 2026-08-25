package io.papermc.paper.event.inventory;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperItemCraftedEvent extends CraftEvent implements ItemCraftedEvent {

    private final Player player;
    private final ItemStack craftedItem;

    public PaperItemCraftedEvent(final Player player, final ItemStack craftedItem) {
        this.player = player;
        this.craftedItem = craftedItem;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getCraftedItem() {
        return this.craftedItem.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return ItemCraftedEvent.getHandlerList();
    }
}
