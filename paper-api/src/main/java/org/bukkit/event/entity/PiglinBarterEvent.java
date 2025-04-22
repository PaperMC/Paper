package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Piglin;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores all data related to the bartering interaction with a piglin.
 * <br>
 * Called when a piglin completes a barter.
 */
public class PiglinBarterEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<ItemStack> outcome;
    private final ItemStack input;

    private boolean cancelled;

    @ApiStatus.Internal
    public PiglinBarterEvent(@NotNull Piglin piglin, @NotNull ItemStack input, @NotNull List<ItemStack> outcome) {
        super(piglin);

        this.input = input;
        this.outcome = outcome;
    }

    @NotNull
    @Override
    public Piglin getEntity() {
        return (Piglin) this.entity;
    }

    /**
     * Gets the input of the barter.
     *
     * @return The item that was used to barter with
     */
    @NotNull
    public ItemStack getInput() {
        return this.input.clone();
    }

    /**
     * Returns a mutable list representing the outcome of the barter.
     *
     * @return A mutable list of the item the player will receive
     */
    @NotNull
    public List<ItemStack> getOutcome() {
        return this.outcome;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
