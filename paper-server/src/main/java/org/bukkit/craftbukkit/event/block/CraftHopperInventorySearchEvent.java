package org.bukkit.craftbukkit.event.block;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftInventory;
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

    public CraftHopperInventorySearchEvent(
        final Level level, final BlockPos hopperPos, final @Nullable Container container, final ContainerType containerType, final BlockPos searchPos
    ) {
        this(
            CraftBlock.at(level, hopperPos),
            Optionull.map(container, CraftInventory::new),
            containerType,
            CraftBlock.at(level, searchPos)
        );
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
