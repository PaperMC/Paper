package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player statistic is incremented.
 * <p>
 * This event is not called for some high frequency statistics, e.g. movement
 * based statistics.
 *
 */
public class PlayerStatisticIncrementEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected final Statistic statistic;
    private final int initialValue;
    private final int newValue;
    private boolean isCancelled = false;
    private final EntityType entityType;
    private final Material material;

    public PlayerStatisticIncrementEvent(@NotNull Player player, @NotNull Statistic statistic, int initialValue, int newValue) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = null;
        this.material = null;
    }

    public PlayerStatisticIncrementEvent(@NotNull Player player, @NotNull Statistic statistic, int initialValue, int newValue, @NotNull EntityType entityType) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = entityType;
        this.material = null;
    }

    public PlayerStatisticIncrementEvent(@NotNull Player player, @NotNull Statistic statistic, int initialValue, int newValue, @NotNull Material material) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = null;
        this.material = material;
    }

    /**
     * Gets the statistic that is being incremented.
     *
     * @return the incremented statistic
     */
    @NotNull
    public Statistic getStatistic() {
        return statistic;
    }

    /**
     * Gets the previous value of the statistic.
     *
     * @return the previous value of the statistic
     */
    public int getPreviousValue() {
        return initialValue;
    }

    /**
     * Gets the new value of the statistic.
     *
     * @return the new value of the statistic
     */
    public int getNewValue() {
        return newValue;
    }

    /**
     * Gets the EntityType if {@link #getStatistic() getStatistic()} is an
     * entity statistic otherwise returns null.
     *
     * @return the EntityType of the statistic
     */
    @Nullable
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * Gets the Material if {@link #getStatistic() getStatistic()} is a block
     * or item statistic otherwise returns null.
     *
     * @return the Material of the statistic
     */
    @Nullable
    public Material getMaterial() {
        return material;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
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
