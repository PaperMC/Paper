package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PaperPlayerPickBlockEvent extends PaperPlayerPickItemEvent implements PlayerPickBlockEvent {

    private final Block block;

    public PaperPlayerPickBlockEvent(final Player player, final Block block, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player, includeData, targetSlot, sourceSlot);
        this.block = block;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }
}
