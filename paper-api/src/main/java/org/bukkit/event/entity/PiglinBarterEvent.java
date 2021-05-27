package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Piglin;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Stores all data related to the bartering interaction with a piglin.
 *
 * This event can be triggered by a piglin picking up an item that's on its
 * bartering list.
 */
public class PiglinBarterEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final List<ItemStack> outcome;
    private final ItemStack input;

    public PiglinBarterEvent(@NotNull Piglin what, @NotNull ItemStack input, @NotNull List<ItemStack> outcome) {
        super(what);

        this.input = input;
        this.outcome = outcome;
    }

    @NotNull
    @Override
    public Piglin getEntity() {
        return (Piglin) super.getEntity();
    }

    /**
     * Gets the input of the barter.
     *
     * @return The item that was used to barter with
     */
    @NotNull
    public ItemStack getInput() {
        return input.clone();
    }

    /**
     * Returns a mutable list representing the outcome of the barter.
     *
     * @return A mutable list of the item the player will receive
     */
    @NotNull
    public List<ItemStack> getOutcome() {
        return outcome;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
