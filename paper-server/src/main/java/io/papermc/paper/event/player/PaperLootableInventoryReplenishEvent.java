package io.papermc.paper.event.player;

import com.destroystokyo.paper.loottable.LootableInventory;
import com.destroystokyo.paper.loottable.LootableInventoryReplenishEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperLootableInventoryReplenishEvent extends CraftPlayerEvent implements LootableInventoryReplenishEvent {

    private final LootableInventory inventory;
    private boolean cancelled;

    public PaperLootableInventoryReplenishEvent(final Player player, final LootableInventory inventory) {
        super(player);
        this.inventory = inventory;
    }

    @Override
    public LootableInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return LootableInventoryReplenishEvent.getHandlerList();
    }
}
