package io.papermc.paper.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperBlockPreDispenseEvent extends CraftBlockEvent implements BlockPreDispenseEvent {

    private final ItemStack itemStack;
    private final int slot;

    private boolean cancelled;

    public PaperBlockPreDispenseEvent(final Block block, final ItemStack itemStack, final int slot) {
        super(block);
        this.itemStack = itemStack;
        this.slot = slot;
    }

    public PaperBlockPreDispenseEvent(final Level level, final BlockPos pos, final net.minecraft.world.item.ItemStack itemStack, final int slot) {
        this(CraftBlock.at(level, pos), CraftItemStack.asCraftMirror(itemStack), slot);
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public int getSlot() {
        return this.slot;
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
        return BlockPreDispenseEvent.getHandlerList();
    }
}
