package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.inventory.ItemStack;

public class CraftBlockDamageAbortEvent extends CraftBlockEvent implements BlockDamageAbortEvent {

    private final Player player;
    private final ItemStack itemInHand;

    public CraftBlockDamageAbortEvent(final Player player, final Block block, final ItemStack itemInHand) {
        super(block);
        this.player = player;
        this.itemInHand = itemInHand;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockDamageAbortEvent.getHandlerList();
    }
}
