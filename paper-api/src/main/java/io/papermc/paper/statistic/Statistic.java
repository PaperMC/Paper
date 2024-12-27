package io.papermc.paper.statistic;

import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.RenderType;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents an individual statistic
 *
 * @param <S> stat (one of {@link org.bukkit.entity.EntityType}, {@link org.bukkit.Material} or {@link CustomStatistic}).
 */
@ApiStatus.NonExtendable
public interface Statistic<S> extends Criteria {

    /**
     * Gets the statistic.
     *
     * @return the stat
     */
    S value();

    /**
     * Get the stat type.
     *
     * @return the stat type
     */
    StatisticType<S> type();

    @Override
    default boolean isReadOnly() {
        return false;
    }

    @Override
    default RenderType getDefaultRenderType() {
        return RenderType.INTEGER;
    }
}
