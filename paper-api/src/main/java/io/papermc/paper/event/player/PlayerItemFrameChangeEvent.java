package io.papermc.paper.event.player;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// Called when an [ItemFrame] is having an item rotated, added, or removed from it.
@NullMarked
public class PlayerItemFrameChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemFrame itemFrame;
    private final ItemFrameChangeAction action;
    private ItemStack itemStack;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerItemFrameChangeEvent(final Player player, final ItemFrame itemFrame, final ItemStack itemStack, final ItemFrameChangeAction action) {
        super(player);
        this.itemFrame = itemFrame;
        this.itemStack = itemStack;
        this.action = action;
    }

    /// Gets the [ItemFrame] involved in this event.
    ///
    /// @return the [ItemFrame]
    public ItemFrame getItemFrame() {
        return this.itemFrame;
    }

    /// Gets the [ItemStack] involved in this event.
    /// This is the item being added, rotated, or removed from the [ItemFrame].
    ///
    /// If this method returns air, then the resulting item in the ItemFrame will be empty.
    ///
    /// @return the [ItemStack] being added, rotated, or removed
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /// Sets the [ItemStack] that this [ItemFrame] holds.
    /// If `null` is provided, the ItemStack will become air and the result in the ItemFrame will be empty.
    ///
    /// @param itemStack [ItemFrame] item
    public void setItemStack(final @Nullable ItemStack itemStack) {
        this.itemStack = itemStack == null ? ItemStack.empty() : itemStack;
    }

    /// Gets the action that was performed on this [ItemFrame].
    ///
    /// @return action performed on the item frame in this event
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum ItemFrameChangeAction {
        PLACE,
        REMOVE,
        ROTATE
    }
}
