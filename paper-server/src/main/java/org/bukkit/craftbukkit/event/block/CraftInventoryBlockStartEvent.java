package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.InventoryBlockStartEvent;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBlockStartEvent extends CraftBlockEvent implements InventoryBlockStartEvent {

    protected ItemStack source;

    public CraftInventoryBlockStartEvent(final Block block, final ItemStack source) {
        super(block);
        this.source = source;
    }

    @Override
    public ItemStack getSource() {
        return this.source;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryBlockStartEvent.getHandlerList();
    }
}
