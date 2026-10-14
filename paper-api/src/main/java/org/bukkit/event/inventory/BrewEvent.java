package org.bukkit.event.inventory;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the brewing of the contents inside the Brewing Stand is
 * complete.
 */
public class BrewEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BrewerInventory contents;
    private final List<ItemStack> results;
    private final int fuelLevel;

    private boolean cancelled;

    @ApiStatus.Internal
    public BrewEvent(@NotNull Block brewer, @NotNull BrewerInventory contents, @NotNull List<ItemStack> results, int fuelLevel) {
        super(brewer);
        this.contents = contents;
        this.results = results;
        this.fuelLevel = fuelLevel;
    }

    /**
     * Gets the contents of the Brewing Stand.
     *
     * @return the contents
     * @apiNote The brewer inventory still holds the items found prior to
     * the finalization of the brewing process, e.g. the plain water bottles.
     */
    @NotNull
    public BrewerInventory getContents() {
        return this.contents;
    }

    /**
     * Gets the resulting items in the Brewing Stand.
     * <p>
     * The returned list, in case of a server-created event instance, is
     * mutable. Any changes in the returned list will reflect in the brewing
     * result if the event is not cancelled. If the size of the list is reduced,
     * remaining items will be set to air.
     *
     * @return List of {@link ItemStack} resulting for this operation
     */
    @NotNull
    public List<ItemStack> getResults() {
        return this.results;
    }

    /**
     * Gets the remaining fuel level.
     *
     * @return the remaining fuel
     */
    public int getFuelLevel() {
        return this.fuelLevel;
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
