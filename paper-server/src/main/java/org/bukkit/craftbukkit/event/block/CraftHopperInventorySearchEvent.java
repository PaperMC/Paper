package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.Nullable;

public class CraftHopperInventorySearchEvent extends CraftBlockEvent implements HopperInventorySearchEvent {

    private @Nullable Inventory inventory;
    private final ContainerType containerType;
    private final Block searchBlock;

    public CraftHopperInventorySearchEvent(final Block hopper, final @Nullable Inventory inventory, final ContainerType containerType, final Block searchBlock) {
        super(hopper);
        this.inventory = inventory;
        this.containerType = containerType;
        this.searchBlock = searchBlock;
    }

    @Override
    public @Nullable Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void setInventory(@Nullable Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public ContainerType getContainerType() {
        return this.containerType;
    }

    @Override
    public Block getSearchBlock() {
        return this.searchBlock;
    }

    @Override
    public HandlerList getHandlers() {
        return HopperInventorySearchEvent.getHandlerList();
    }
}
