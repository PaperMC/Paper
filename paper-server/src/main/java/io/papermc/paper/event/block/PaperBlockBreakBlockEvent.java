package io.papermc.paper.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockExpEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperBlockBreakBlockEvent extends CraftBlockExpEvent implements BlockBreakBlockEvent {

    private final Block source;
    private final List<ItemStack> drops;

    public PaperBlockBreakBlockEvent(final Block block, final Block source, final List<ItemStack> drops) {
        super(block, 0);
        this.source = source;
        this.drops = drops;
    }

    @Override
    public Block getSource() {
        return this.source;
    }

    @Override
    public List<ItemStack> getDrops() {
        return this.drops;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockBreakBlockEvent.getHandlerList();
    }
}
