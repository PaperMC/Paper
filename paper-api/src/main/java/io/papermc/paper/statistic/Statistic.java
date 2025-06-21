package io.papermc.paper.statistic;

import org.bukkit.scoreboard.Criteria;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents an individual statistic. Obtained via {@link StatisticType#forValue(Object)}.
 * Can be used as a criteria for {@link org.bukkit.scoreboard.Scoreboard#registerNewObjective(String, Criteria, net.kyori.adventure.text.Component)}
 *
 * @param <S> stat value type.
 */
@ApiStatus.NonExtendable
public interface Statistic<S> extends Criteria {

    /**
     * Gets the owner of the statistic.
     *
     * @return the stat owner
     */
    S owner();

    /**
     * Gets the stat type.
     *
     * @return the stat type
     */
    StatisticType<S> type();
}
