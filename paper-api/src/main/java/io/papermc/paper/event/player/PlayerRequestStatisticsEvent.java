package io.papermc.paper.event.player;

import io.papermc.paper.statistic.Statistic;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the player requests their statistics.
 */
@NullMarked
public class PlayerRequestStatisticsEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Object2IntMap<Statistic<?>> statisticMap;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerRequestStatisticsEvent(final Player player, final Object2IntMap<Statistic<?>> statisticMap) {
        super(player);
        this.statisticMap = statisticMap;
    }

    /**
     * Gets the statistic map to be sent to the player.
     *
     * @return the mutable statistic map
     */
    public Object2IntMap<Statistic<?>> getStatisticMap() {
        return this.statisticMap;
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
