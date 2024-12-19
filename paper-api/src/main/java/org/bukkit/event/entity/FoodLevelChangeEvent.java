package org.bukkit.event.entity;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a human entity's food level changes
 *
 * @since 1.0.0 R1
 */
public class FoodLevelChangeEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private int level;
    private final ItemStack item;

    public FoodLevelChangeEvent(@NotNull final HumanEntity what, final int level) {
        this(what, level, null);
    }

    public FoodLevelChangeEvent(@NotNull final HumanEntity what, final int level, @Nullable final ItemStack item) {
        super(what);
        this.level = level;
        this.item = item;
    }

    /**
     * @since 1.1.0 R5
     */
    @NotNull
    @Override
    public HumanEntity getEntity() {
        return (HumanEntity) entity;
    }

    /**
     * Gets the item that triggered this event, if any.
     *
     * @return an ItemStack for the item being consumed
     * @since 1.14.2
     */
    @Nullable
    public ItemStack getItem() {
        return (item == null) ? null : item.clone();
    }

    /**
     * Gets the resultant food level that the entity involved in this event
     * should be set to.
     * <p>
     * Where 20 is a full food bar and 0 is an empty one.
     *
     * @return The resultant food level
     */
    public int getFoodLevel() {
        return level;
    }

    /**
     * Sets the resultant food level that the entity involved in this event
     * should be set to
     *
     * @param level the resultant food level that the entity involved in this
     *     event should be set to
     */
    public void setFoodLevel(int level) {
        if (level < 0) level = 0;

        this.level = level;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
