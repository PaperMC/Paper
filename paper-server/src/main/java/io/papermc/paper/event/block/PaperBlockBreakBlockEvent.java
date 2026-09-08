package io.papermc.paper.event.block;

import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockExpEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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

    public PaperBlockBreakBlockEvent(final Level level, final BlockPos pos, final BlockPos sourcePos, final List<net.minecraft.world.item.ItemStack> drops) {
        this(
            CraftBlock.at(level, pos),
            CraftBlock.at(level, sourcePos),
            MCUtil.mutableTransform(drops, CraftItemStack::asCraftMirror, CraftItemStack::asNMSCopy)
        );
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
