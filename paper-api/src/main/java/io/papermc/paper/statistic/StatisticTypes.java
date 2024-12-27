package io.papermc.paper.statistic;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemType;

/**
 * All statistic types.
 */
public final class StatisticTypes {

    // Start generate - StatisticTypes
    public static final StatisticType<BlockType> BLOCK_MINED = get("mined");

    public static final StatisticType<CustomStatistic> CUSTOM = get("custom");

    public static final StatisticType<EntityType> ENTITY_KILLED = get("killed");

    public static final StatisticType<EntityType> ENTITY_KILLED_BY = get("killed_by");

    public static final StatisticType<ItemType> ITEM_BROKEN = get("broken");

    public static final StatisticType<ItemType> ITEM_CRAFTED = get("crafted");

    public static final StatisticType<ItemType> ITEM_DROPPED = get("dropped");

    public static final StatisticType<ItemType> ITEM_PICKED_UP = get("picked_up");

    public static final StatisticType<ItemType> ITEM_USED = get("used");
    // End generate - StatisticTypes

    @SuppressWarnings("unchecked")
    private static <S> StatisticType<S> get(@KeyPattern.Value final String key) {
        return (StatisticType<S>) Registry.STAT_TYPE.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    private StatisticTypes() {
    }
}
