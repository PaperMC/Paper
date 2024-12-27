package io.papermc.paper.statistic;

import org.bukkit.scoreboard.Criteria;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents an individual statistic. Obtained via {@link StatisticType#of(Object)}.
 * Can be used as a criteria for {@link org.bukkit.scoreboard.Scoreboard#registerNewObjective(String, Criteria, net.kyori.adventure.text.Component)}
 *
 * @param <S> stat value type.
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
}
