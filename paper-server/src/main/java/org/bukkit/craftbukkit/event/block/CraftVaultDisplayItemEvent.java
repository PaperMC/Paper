package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.VaultDisplayItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftVaultDisplayItemEvent extends CraftBlockEvent implements VaultDisplayItemEvent {

    private @Nullable ItemStack displayItem;
    private boolean cancelled;

    public CraftVaultDisplayItemEvent(final Block vault, final ItemStack displayItem) {
        super(vault);
        this.displayItem = displayItem;
    }

    public CraftVaultDisplayItemEvent(final Level level, final BlockPos pos, final net.minecraft.world.item.ItemStack displayItem) {
        this(CraftBlock.at(level, pos), CraftItemStack.asBukkitCopy(displayItem));
    }

    @Override
    public @Nullable ItemStack getDisplayItem() {
        return this.displayItem;
    }

    @Override
    public void setDisplayItem(final @Nullable ItemStack displayItem) {
        this.displayItem = displayItem;
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
        return VaultDisplayItemEvent.getHandlerList();
    }
}
