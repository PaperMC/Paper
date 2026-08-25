package io.papermc.paper.event.inventory;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperItemCraftedEvent extends CraftPlayerEvent implements ItemCraftedEvent {

    private final ItemStack craftedItem;

    public PaperItemCraftedEvent(final Player player, final ItemStack craftedItem) {
        super(player);
        this.craftedItem = craftedItem;
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
