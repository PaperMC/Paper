package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.event.block.CraftBellRingEvent;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.Nullable;

public class PaperBellRingEvent extends CraftBellRingEvent implements BellRingEvent {

    public PaperBellRingEvent(final Block block, final BlockFace direction, final @Nullable Entity entity) {
        super(block, direction, entity);
    }
}
