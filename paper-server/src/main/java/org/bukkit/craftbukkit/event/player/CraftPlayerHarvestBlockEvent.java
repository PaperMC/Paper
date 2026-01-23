package org.bukkit.craftbukkit.event.player;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerHarvestBlockEvent extends CraftPlayerEvent implements PlayerHarvestBlockEvent {

    private final Block harvestedBlock;
    private final EquipmentSlot hand;
    private final List<ItemStack> itemsHarvested;

    private boolean cancelled;

    public CraftPlayerHarvestBlockEvent(final Player player, final Block harvestedBlock, final EquipmentSlot hand, final List<ItemStack> itemsHarvested) {
        super(player);
        this.harvestedBlock = harvestedBlock;
        this.hand = hand;
        this.itemsHarvested = itemsHarvested;
    }

    @Override
    public Block getHarvestedBlock() {
        return this.harvestedBlock;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public List<ItemStack> getItemsHarvested() {
        return this.itemsHarvested;
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
        return PlayerHarvestBlockEvent.getHandlerList();
    }
}
