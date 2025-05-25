package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a Zombified piglin is angered by another entity.
 * <p>
 * If the event is cancelled, the zombified piglin will not be angered.
 */
public class PigZombieAngerEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity target;
    private int newAnger;

    private boolean cancelled;

    @ApiStatus.Internal
    public PigZombieAngerEvent(@NotNull final PigZombie zombifiedPiglin, @Nullable final Entity target, final int newAnger) {
        super(zombifiedPiglin);
        this.target = target;
        this.newAnger = newAnger;
    }

    /**
     * Gets the entity (if any) which triggered this anger update.
     *
     * @return triggering entity, or {@code null}
     */
    @Nullable
    public Entity getTarget() {
        return this.target;
    }

    /**
     * Gets the new anger resulting from this event.
     *
     * @return new anger
     * @see PigZombie#getAnger()
     */
    public int getNewAnger() {
        return this.newAnger;
    }

    /**
     * Sets the new anger resulting from this event.
     *
     * @param newAnger the new anger
     * @see PigZombie#setAnger(int)
     */
    public void setNewAnger(int newAnger) {
        this.newAnger = newAnger;
    }

    @NotNull
    @Override
    public PigZombie getEntity() {
        return (PigZombie) this.entity;
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
