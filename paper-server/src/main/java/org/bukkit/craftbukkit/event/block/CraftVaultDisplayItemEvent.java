package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.VaultDisplayItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftVaultDisplayItemEvent extends CraftBlockEvent implements VaultDisplayItemEvent {

    private @Nullable ItemStack displayItem;
    private boolean cancelled;

    public CraftVaultDisplayItemEvent(final Block vault, final @Nullable ItemStack displayItem) {
        super(vault);
        this.displayItem = displayItem;
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
