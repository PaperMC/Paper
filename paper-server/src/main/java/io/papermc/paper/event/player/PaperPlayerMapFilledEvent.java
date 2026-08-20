package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerMapFilledEvent extends CraftPlayerEvent implements PlayerMapFilledEvent {

    private final ItemStack originalItem;
    private ItemStack createdMap;

    public PaperPlayerMapFilledEvent(final Player player, final ItemStack originalItem, final ItemStack createdMap) {
        super(player);
        this.originalItem = originalItem;
        this.createdMap = createdMap;
    }

    @Override
    public ItemStack getOriginalItem() {
        return this.originalItem.clone();
    }

    @Override
    public ItemStack getCreatedMap() {
        return this.createdMap.clone();
    }

    @Override
    public void setCreatedMap(final ItemStack createdMap) {
        this.createdMap = createdMap.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerMapFilledEvent.getHandlerList();
    }
}
