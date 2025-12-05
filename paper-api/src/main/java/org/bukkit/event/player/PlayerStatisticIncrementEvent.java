package org.bukkit.event.player;

import io.papermc.paper.statistic.Statistic;
import org.bukkit.Material;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player statistic is incremented.
 * <p>
 * This event is not called for some high frequency statistics, e.g. movement
 * based statistics.
 */
@NullMarked
public class PlayerStatisticIncrementEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Statistic<?> statistic;
    private final int initialValue;
    private final int newValue;
    private final @Nullable EntityType entityType;
    private final @Nullable Material material;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerStatisticIncrementEvent(final Player player, final Statistic<?> statistic, final int initialValue, final int newValue) {
        super(player);
        this.statistic = statistic;
        this.initialValue = initialValue;
        this.newValue = newValue;
        this.entityType = statistic.owner() instanceof final EntityType et ? et : null;
        if (statistic.owner() instanceof final ItemType it) {
            this.material = it.asMaterial();
        } else if (statistic.owner() instanceof final BlockType bt) {
            this.material = bt.asMaterial();
        } else {
            this.material = null;
        }
    }

    /**
     * Gets the statistic that is being incremented.
     *
     * @return the incremented statistic
     */
    public Statistic<?> getStat() {
        return this.statistic;
    }

    /**
     * Gets the statistic that is being incremented.
     *
     * @return the incremented statistic
     * @deprecated use {@link #getStat()}
     */
    @Deprecated(since = "1.21.11")
    public org.bukkit.Statistic getStatistic() {
        return org.bukkit.Statistic.toLegacy(this.statistic);
    }

    /**
     * Gets the previous value of the statistic.
     *
     * @return the previous value of the statistic
     */
    public int getPreviousValue() {
        return this.initialValue;
    }

    /**
     * Gets the new value of the statistic.
     *
     * @return the new value of the statistic
     */
    public int getNewValue() {
        return this.newValue;
    }

    /**
     * Gets the EntityType if {@link #getStatistic()} is an
     * entity statistic otherwise returns {@code null}.
     *
     * @return the EntityType of the statistic
     * @deprecated use {@link #getStat()}
     */
    @Deprecated(since = "1.21.11")
    public @Nullable EntityType getEntityType() {
        return this.entityType;
    }

    /**
     * Gets the Material if {@link #getStatistic()} is a block
     * or item statistic otherwise returns {@code null}.
     *
     * @return the Material of the statistic
     * @deprecated use {@link #getStat()}
     */
    @Deprecated(since = "1.21.11")
    public @Nullable Material getMaterial() {
        return this.material;
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
