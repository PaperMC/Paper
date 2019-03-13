package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a Pig Zombie is angered by another entity.
 * <p>
 * If the event is cancelled, the pig zombie will not be angered.
 */
public class PigZombieAngerEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final Entity target;
    private int newAnger;

    public PigZombieAngerEvent(@NotNull final PigZombie pigZombie, @Nullable final Entity target, final int newAnger) {
        super(pigZombie);
        this.target = target;
        this.newAnger = newAnger;
    }

    /**
     * Gets the entity (if any) which triggered this anger update.
     *
     * @return triggering entity, or null
     */
    @Nullable
    public Entity getTarget() {
        return target;
    }

    /**
     * Gets the new anger resulting from this event.
     *
     * @return new anger
     * @see PigZombie#getAnger()
     */
    public int getNewAnger() {
        return newAnger;
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
        return (PigZombie) entity;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
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
