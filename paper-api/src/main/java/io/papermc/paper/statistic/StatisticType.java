package io.papermc.paper.statistic;

import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface StatisticType<S> extends Keyed, Translatable {

    // Start generate - StatisticType
    // @GeneratedFrom 1.21.6-rc1
    StatisticType<BlockType> BLOCK_MINED = get("mined");

    StatisticType<CustomStatistic> CUSTOM = get("custom");

    StatisticType<EntityType> ENTITY_KILLED = get("killed");

    StatisticType<EntityType> ENTITY_KILLED_BY = get("killed_by");

    StatisticType<ItemType> ITEM_BROKEN = get("broken");

    StatisticType<ItemType> ITEM_CRAFTED = get("crafted");

    StatisticType<ItemType> ITEM_DROPPED = get("dropped");

    StatisticType<ItemType> ITEM_PICKED_UP = get("picked_up");

    StatisticType<ItemType> ITEM_USED = get("used");
    // End generate - StatisticType

    @SuppressWarnings("unchecked")
    private static <S> StatisticType<S> get(@KeyPattern.Value final String key) {
        return (StatisticType<S>) Registry.STAT_TYPE.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    /**
     * Creates or gets the statistic from this type for the specified value.
     *
     * @param value what you want the stat of
     * @return the statistic for that thing
     * @throws IllegalArgumentException if the thing is not valid for this {@link StatisticType}
     */
    Statistic<S> of(S value);

    /**
     * Gets the registry key associated with this stat type.
     *
     * @return the registry
     */
    RegistryKey<S> registryKey();

    /**
     * {@inheritDoc}
     * <p>
     * {@link StatisticType#CUSTOM} does <b>NOT</b> have a
     * translation key.
     *
     * @throws IllegalArgumentException if used with {@link StatisticType#CUSTOM}
     * @see CustomStatistic#translationKey()
     */
    @Override
    String translationKey();
}
