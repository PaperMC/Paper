package org.bukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.StructureGrowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called with the block changes resulting from a player fertilizing a given
 * block with bonemeal. Will be called after the applicable
 * {@link StructureGrowEvent}.
 */
public class BlockFertilizeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private final Player player;
    private final List<BlockState> blocks;

    public BlockFertilizeEvent(@NotNull Block theBlock, @Nullable Player player, @NotNull List<BlockState> blocks) {
        super(theBlock);
        this.player = player;
        this.blocks = blocks;
    }

    /**
     * Gets the player that triggered the fertilization.
     *
     * @return triggering player, or null if not applicable
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets a list of all blocks changed by the fertilization.
     *
     * @return list of all changed blocks
     */
    @NotNull
    public List<BlockState> getBlocks() {
        return blocks;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
