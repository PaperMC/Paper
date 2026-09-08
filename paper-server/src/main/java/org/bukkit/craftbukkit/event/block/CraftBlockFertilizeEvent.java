package org.bukkit.craftbukkit.event.block;

import java.util.List;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.jspecify.annotations.Nullable;

public class CraftBlockFertilizeEvent extends CraftBlockEvent implements BlockFertilizeEvent {

    private final @Nullable Player player;
    private final List<BlockState> blocks;

    private boolean cancelled;

    public CraftBlockFertilizeEvent(final Block block, final @Nullable Player player, final List<BlockState> blocks) {
        super(block);
        this.player = player;
        this.blocks = blocks;
    }

    public CraftBlockFertilizeEvent(
        final Level level, final BlockPos pos, final net.minecraft.world.entity.player.@Nullable Player player, final List<BlockState> blocks
    ) {
        this(CraftBlock.at(level, pos), (Player) Optionull.map(player, net.minecraft.world.entity.player.Player::getBukkitEntity), blocks);
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public List<BlockState> getBlocks() {
        return this.blocks;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockFertilizeEvent.getHandlerList();
    }
}
