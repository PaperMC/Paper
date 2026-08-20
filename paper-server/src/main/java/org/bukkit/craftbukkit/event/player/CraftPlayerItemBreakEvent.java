package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerItemBreakEvent extends CraftPlayerEvent implements PlayerItemBreakEvent {

    private final ItemStack brokenItem;

    public CraftPlayerItemBreakEvent(final Player player, final ItemStack brokenItem) {
        super(player);
        this.brokenItem = brokenItem;
    }

    @Override
    public ItemStack getBrokenItem() {
        return this.brokenItem;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerItemBreakEvent.getHandlerList();
    }
}
