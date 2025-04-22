package org.bukkit.event.inventory;

import org.bukkit.Warning;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is put in a slot and the result is calculated.
 *
 * @deprecated use {@link com.destroystokyo.paper.event.inventory.PrepareResultEvent}
 */
@Deprecated @Warning
public class PrepareInventoryResultEvent extends InventoryEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private ItemStack result;

    @ApiStatus.Internal
    public PrepareInventoryResultEvent(@NotNull InventoryView inventory, @Nullable ItemStack result) {
        super(inventory);
        this.result = result;
    }

    /**
     * Get result item, may be {@code null}.
     *
     * @return result item
     */
    @Nullable
    public ItemStack getResult() {
        return this.result;
    }

    /**
     * Set result item, may be {@code null}.
     *
     * @param result result item
     */
    public void setResult(@Nullable ItemStack result) {
        this.result = result;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
