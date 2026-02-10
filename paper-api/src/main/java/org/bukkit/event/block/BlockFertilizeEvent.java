package org.bukkit.event.block;

import java.util.List;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.StructureGrowEvent;
import org.jspecify.annotations.Nullable;

/**
 * Called with the block changes resulting from a player fertilizing a given
 * block with bonemeal. Will be called after the applicable
 * {@link StructureGrowEvent}.
 */
public interface BlockFertilizeEvent extends BlockEvent, Cancellable {

    /**
     * Gets the player that triggered the fertilization.
     *
     * @return triggering player, or {@code null} if not applicable
     */
    @Nullable Player getPlayer();

    /**
     * Gets a list of all blocks changed by the fertilization.
     *
     * @return list of all changed blocks
     */
    List<BlockState> getBlocks();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
