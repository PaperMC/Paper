package io.papermc.paper.statistic;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a statistic of the type {@link StatisticTypes#CUSTOM}.
 *
 * @see CustomStatistics
 */
@ApiStatus.NonExtendable
public interface CustomStatistic extends Keyed, Translatable {

    /**
     * Gets the statistic with the given custom stat.
     *
     * @return the statistic for the custom stat.
     */
    default Statistic<CustomStatistic> stat() {
        return StatisticTypes.CUSTOM.forValue(this);
    }
}
