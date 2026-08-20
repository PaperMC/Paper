package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class PaperPlayerItemFrameChangeEvent extends CraftPlayerEvent implements PlayerItemFrameChangeEvent {

    private final ItemFrame itemFrame;
    private final ItemFrameChangeAction action;
    private ItemStack itemStack;

    private boolean cancelled;

    public PaperPlayerItemFrameChangeEvent(final Player player, final ItemFrame itemFrame, final ItemStack itemStack, final ItemFrameChangeAction action) {
        super(player);
        this.itemFrame = itemFrame;
        this.itemStack = itemStack;
        this.action = action;
    }

    @Override
    public ItemFrame getItemFrame() {
        return this.itemFrame;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack == null ? ItemStack.empty() : itemStack;
    }

    @Override
    public ItemFrameChangeAction getAction() {
        return this.action;
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
        return PlayerItemFrameChangeEvent.getHandlerList();
    }
}
