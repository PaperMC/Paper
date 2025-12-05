package io.papermc.paper.statistic;

import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * A type of statistic.
 *
 * @param <S> the value type of this statistic type
 * @see StatisticTypes
 */
@ApiStatus.NonExtendable
public interface StatisticType<S> extends Keyed, Translatable {


    /**
     * Creates or gets the statistic from this type for the specified value.
     *
     * @param value what you want the stat of
     * @return the statistic for that thing
     * @throws IllegalArgumentException if the thing is not valid for this type
     */
    Statistic<S> forValue(S value);

    /**
     * Gets the registry key associated with this stat type.
     *
     * @return the registry
     */
    RegistryKey<S> registryKey();

    /**
     * {@inheritDoc}
     * <p>
     * {@link StatisticTypes#CUSTOM} does <b>NOT</b> have a
     * translation key.
     *
     * @throws IllegalArgumentException if used with {@link StatisticTypes#CUSTOM}
     */
    @Override
    String translationKey();
}
