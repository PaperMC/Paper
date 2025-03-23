package org.bukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an arrow enters or exists an entity's body.
 */
public class ArrowBodyCountChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean reset;
    private final int oldAmount;
    private int newAmount;

    private boolean cancelled;

    @ApiStatus.Internal
    public ArrowBodyCountChangeEvent(@NotNull LivingEntity entity, int oldAmount, int newAmount, boolean reset) {
        super(entity);

        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
        this.reset = reset;
    }

    /**
     * Whether the event was called because the entity was reset.
     *
     * @return was reset
     */
    public boolean isReset() {
        return this.reset;
    }

    /**
     * Gets the old amount of arrows in the entity's body.
     *
     * @return amount of arrows
     */
    public int getOldAmount() {
        return this.oldAmount;
    }

    /**
     * Get the new amount of arrows in the entity's body.
     *
     * @return amount of arrows
     */
    public int getNewAmount() {
        return this.newAmount;
    }

    /**
     * Sets the final amount of arrows in the entity's body.
     *
     * @param newAmount amount of arrows
     */
    public void setNewAmount(int newAmount) {
        Preconditions.checkArgument(newAmount >= 0, "New arrow amount must be >= 0");
        this.newAmount = newAmount;
    }

    @Override
    @NotNull
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
