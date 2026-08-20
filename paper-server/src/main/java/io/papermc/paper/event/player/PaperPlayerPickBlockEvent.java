package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerPickBlockEvent extends PaperPlayerPickItemEvent implements PlayerPickBlockEvent {

    private final Block block;

    public PaperPlayerPickBlockEvent(final Player player, final Block block, final ItemStack item, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player, item, includeData, targetSlot, sourceSlot);
        this.block = block;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }
}
