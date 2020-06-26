package org.bukkit.event.entity;

import org.bukkit.entity.Animals;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an entity enters love mode.
 * <br>
 * This can be cancelled but the item will still be consumed that was used to
 * make the entity enter into love mode.
 */
public class EntityEnterLoveModeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final HumanEntity humanEntity;
    private int ticksInLove;

    public EntityEnterLoveModeEvent(@NotNull Animals animalInLove, @Nullable HumanEntity humanEntity, int ticksInLove) {
        super(animalInLove);
        this.humanEntity = humanEntity;
        this.ticksInLove = ticksInLove;
    }

    /**
     * Gets the animal that is entering love mode.
     *
     * @return The animal that is entering love mode
     */
    @NotNull
    @Override
    public Animals getEntity() {
        return (Animals) entity;
    }

    /**
     * Gets the Human Entity that caused the animal to enter love mode.
     *
     * @return The Human entity that caused the animal to enter love mode, or
     * null if there wasn't one.
     */
    @Nullable
    public HumanEntity getHumanEntity() {
        return humanEntity;
    }

    /**
     * Gets the amount of ticks that the animal will fall in love for.
     *
     * @return The amount of ticks that the animal will fall in love for
     */
    public int getTicksInLove() {
        return ticksInLove;
    }

    /**
     * Sets the amount of ticks that the animal will fall in love for.
     *
     * @param ticksInLove The amount of ticks that the animal will fall in love
     * for
     */
    public void setTicksInLove(int ticksInLove) {
        this.ticksInLove = ticksInLove;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
