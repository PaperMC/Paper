package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a {@link org.bukkit.entity.Item} is removed from
 * the world because it has existed for 5 minutes.
 * <p>
 * Cancelling the event results in the item being allowed to exist for 5 more
 * minutes. This behavior is not guaranteed and may change in future versions.
 */
public class ItemDespawnEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location location;

    private boolean cancelled;

    @ApiStatus.Internal
    public ItemDespawnEvent(@NotNull final Item despawnee, @NotNull final Location location) {
        super(despawnee);
        this.location = location;
    }

    @NotNull
    @Override
    public Item getEntity() {
        return (Item) this.entity;
    }

    /**
     * Gets the location at which the item is despawning.
     *
     * @return The location at which the item is despawning
     */
    @NotNull
    public Location getLocation() {
        return this.location.clone();
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
