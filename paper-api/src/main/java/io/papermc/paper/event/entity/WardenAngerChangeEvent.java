package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Warden;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a Warden's anger level has changed due to another entity.
 * <p>
 * If the event is cancelled, the warden's anger level will not change.
 */
@NullMarked
public class WardenAngerChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity target;
    private final int oldAnger;
    private int newAnger;

    private boolean cancelled;

    @ApiStatus.Internal
    public WardenAngerChangeEvent(final Warden warden, final Entity target, final int oldAnger, final int newAnger) {
        super(warden);
        this.target = target;
        this.oldAnger = oldAnger;
        this.newAnger = newAnger;
    }

    /**
     * Gets the entity which triggered this anger update.
     *
     * @return triggering entity
     */
    public Entity getTarget() {
        return this.target;
    }

    /**
     * Gets the old anger level.
     *
     * @return old anger level
     * @see Warden#getAnger(Entity)
     */
    public @Range(from = 0, to = 150) int getOldAnger() {
        return this.oldAnger;
    }

    /**
     * Gets the new anger level resulting from this event.
     *
     * @return new anger level
     * @see Warden#getAnger(Entity)
     */
    public @Range(from = 0, to = 150) int getNewAnger() {
        return this.newAnger;
    }

    /**
     * Sets the new anger level resulting from this event.
     * <p>
     * The anger of a warden is capped at 150.
     *
     * @param newAnger the new anger level, max 150
     * @throws IllegalArgumentException if newAnger is greater than 150
     * @see Warden#setAnger(Entity, int)
     */
    public void setNewAnger(final @Range(from = 0, to = 150) int newAnger) {
        Preconditions.checkArgument(newAnger <= 150, "newAnger must not be greater than 150");
        this.newAnger = newAnger;
    }

    @Override
    public Warden getEntity() {
        return (Warden) super.getEntity();
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
}
