package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a bell is rung.
 *
 * @deprecated use {@link org.bukkit.event.block.BellRingEvent}
 */
@Deprecated(since = "1.19.4")
public class BellRingEvent extends org.bukkit.event.block.BellRingEvent {

    @ApiStatus.Internal
    public BellRingEvent(@NotNull Block block, @NotNull BlockFace direction, @Nullable Entity entity) {
        super(block, direction, entity);
    }
}
