package org.bukkit.event.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.material.MaterialData;
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
    private final io.papermc.paper.statistic.Statistic<?> statistic; // Paper
    private final int initialValue;
    private final int newValue;
    private boolean isCancelled = false;
    @Deprecated(forRemoval = true) // Paper
    private final EntityType entityType;
    @Deprecated(forRemoval = true) // Paper
    private final Material material;

    // Paper start
    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerStatisticIncrementEvent(@NotNull Player player, @NotNull io.papermc.paper.statistic.Statistic<?> statistic, int initialValue, int newValue) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = statistic.value() instanceof EntityType entityType ? entityType : null;
        this.material = statistic.value() instanceof Material material ? material : null;
    }

    /**
     * Gets the statistic that is being incremented.
     *
     * @return the incremented statistic
     */
    public @NotNull io.papermc.paper.statistic.Statistic<?> getStat() {
        return this.statistic;
    }
    // Paper end

    /**
     * Gets the statistic that is being incremented.
     *
     * @return the incremented statistic
     * @deprecated use {@link #getStat()}
     */
    @NotNull
    @Deprecated(since = "1.21.4")
    public Statistic getStatistic() {
        return Statistic.toLegacy(this.statistic); // Paper
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
     * @deprecated use {@link #getStat()}
     */
    @Nullable
    @Deprecated(since = "1.21.4")
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * Gets the Material if {@link #getStatistic() getStatistic()} is a block
     * or item statistic otherwise returns null.
     *
     * @return the Material of the statistic
     * @deprecated use {@link #getStat()}
     */
    @Nullable
    @Deprecated(since = "1.21,4")
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
