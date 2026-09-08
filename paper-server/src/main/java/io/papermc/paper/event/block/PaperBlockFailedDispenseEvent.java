package io.papermc.paper.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.event.HandlerList;

public class PaperBlockFailedDispenseEvent extends CraftBlockEvent implements BlockFailedDispenseEvent {

    private boolean shouldPlayEffect = true;

    public PaperBlockFailedDispenseEvent(final Block block) {
        super(block);
    }

    public PaperBlockFailedDispenseEvent(final Level level, final BlockPos pos) {
        this(CraftBlock.at(level, pos));
    }

    @Override
    public boolean shouldPlayEffect() {
        return this.shouldPlayEffect;
    }

    @Override
    public void shouldPlayEffect(final boolean playEffect) {
        this.shouldPlayEffect = playEffect;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockFailedDispenseEvent.getHandlerList();
    }
}
