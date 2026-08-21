package io.papermc.paper.event.entity;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called just before an {@link Entity} spawns due to a pattern of blocks being constructed (golems, the wither, etc.)
 * <p>
 * Note: This event is fired before {@link EntitySpawnEvent}, before the entity is added to the world,
 * the success of this event does not guarantee the entity will actually spawn.
 */
public interface EntityConstructEvent extends EntityEventNew, Cancellable {

    /**
     * Get an immutable list of the blocks required for this construction, including
     * any required air blocks.
     *
     * @return the blocks
     */
    @Unmodifiable List<Block> getBlocks();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
