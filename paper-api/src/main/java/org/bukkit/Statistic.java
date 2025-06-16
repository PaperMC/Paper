package org.bukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.statistic.CustomStatistic;
import io.papermc.paper.statistic.StatisticType;
import java.util.Locale;
import java.util.Objects;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents a countable statistic, which is tracked by the server.
 *
 * @deprecated use {@link io.papermc.paper.statistic.StatisticType} and {@link io.papermc.paper.statistic.Statistic}
 */
@Deprecated(since = "1.21.6")
public enum Statistic implements Keyed {
    // Start generate - StatisticCustom
    // @GeneratedFrom 1.21.6-rc1
    ANIMALS_BRED,
    AVIATE_ONE_CM,
    BELL_RING,
    BOAT_ONE_CM,
    ARMOR_CLEANED,
    BANNER_CLEANED,
    CLEAN_SHULKER_BOX,
    CLIMB_ONE_CM,
    CROUCH_ONE_CM,
    DAMAGE_ABSORBED,
    DAMAGE_BLOCKED_BY_SHIELD,
    DAMAGE_DEALT,
    DAMAGE_DEALT_ABSORBED,
    DAMAGE_DEALT_RESISTED,
    DAMAGE_RESISTED,
    DAMAGE_TAKEN,
    DEATHS,
    DROP_COUNT,
    CAKE_SLICES_EATEN,
    ITEM_ENCHANTED,
    FALL_ONE_CM,
    CAULDRON_FILLED,
    FISH_CAUGHT,
    FLY_ONE_CM,
    HAPPY_GHAST_ONE_CM,
    HORSE_ONE_CM,
    DISPENSER_INSPECTED,
    DROPPER_INSPECTED,
    HOPPER_INSPECTED,
    INTERACT_WITH_ANVIL,
    BEACON_INTERACTION,
    INTERACT_WITH_BLAST_FURNACE,
    BREWINGSTAND_INTERACTION,
    INTERACT_WITH_CAMPFIRE,
    INTERACT_WITH_CARTOGRAPHY_TABLE,
    CRAFTING_TABLE_INTERACTION,
    FURNACE_INTERACTION,
    INTERACT_WITH_GRINDSTONE,
    INTERACT_WITH_LECTERN,
    INTERACT_WITH_LOOM,
    INTERACT_WITH_SMITHING_TABLE,
    INTERACT_WITH_SMOKER,
    INTERACT_WITH_STONECUTTER,
    JUMP,
    LEAVE_GAME,
    MINECART_ONE_CM,
    MOB_KILLS,
    OPEN_BARREL,
    CHEST_OPENED,
    ENDERCHEST_OPENED,
    SHULKER_BOX_OPENED,
    PIG_ONE_CM,
    NOTEBLOCK_PLAYED,
    RECORD_PLAYED,
    PLAY_ONE_MINUTE,
    PLAYER_KILLS,
    FLOWER_POTTED,
    RAID_TRIGGER,
    RAID_WIN,
    SLEEP_IN_BED,
    SNEAK_TIME,
    SPRINT_ONE_CM,
    STRIDER_ONE_CM,
    SWIM_ONE_CM,
    TALKED_TO_VILLAGER,
    TARGET_HIT,
    TIME_SINCE_DEATH,
    TIME_SINCE_REST,
    TOTAL_WORLD_TIME,
    TRADED_WITH_VILLAGER,
    TRAPPED_CHEST_TRIGGERED,
    NOTEBLOCK_TUNED,
    CAULDRON_USED,
    WALK_ON_WATER_ONE_CM,
    WALK_ONE_CM,
    WALK_UNDER_WATER_ONE_CM,
    // End generate - StatisticCustom
    // Start generate - StatisticType
    // @GeneratedFrom 1.21.6-rc1
    BREAK_ITEM(Type.ITEM),
    CRAFT_ITEM(Type.ITEM),
    DROP(Type.ITEM),
    KILL_ENTITY(Type.ENTITY),
    ENTITY_KILLED_BY(Type.ENTITY),
    MINE_BLOCK(Type.BLOCK),
    PICKUP(Type.ITEM),
    USE_ITEM(Type.ITEM);
    // End generate - StatisticType

    private final Type type;
    private final NamespacedKey key;

    private Statistic() {
        this(Type.UNTYPED);
    }

    private Statistic(/*@NotNull*/ Type type) {
        this.type = type;
        this.key = NamespacedKey.minecraft(name().toLowerCase(Locale.ROOT));
    }

    /**
     * Gets the type of this statistic.
     *
     * @return the type of this statistic
     */
    @NotNull
    public Type getType() {
        return type;
    }

    /**
     * Checks if this is a substatistic.
     * <p>
     * A substatistic exists en masse for each block, item, or entitytype, depending on
     * {@link #getType()}.
     * <p>
     * This is a redundant method and equivalent to checking
     * <code>getType() != Type.UNTYPED</code>
     *
     * @return true if this is a substatistic
     */
    public boolean isSubstatistic() {
        return type != Type.UNTYPED;
    }

    /**
     * Checks if this is a substatistic dealing with blocks.
     * <p>
     * This is a redundant method and equivalent to checking
     * <code>getType() == Type.BLOCK</code>
     *
     * @return true if this deals with blocks
     */
    public boolean isBlock() {
        return type == Type.BLOCK;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * The type of statistic.
     *
     * @deprecated use {@link io.papermc.paper.statistic.StatisticType}
     */
    @Deprecated(since = "1.21.6")
    public enum Type {
        /**
         * Statistics of this type do not require a qualifier.
         */
        UNTYPED,

        /**
         * Statistics of this type require an Item Material qualifier.
         */
        ITEM,

        /**
         * Statistics of this type require a Block Material qualifier.
         */
        BLOCK,

        /**
         * Statistics of this type require an EntityType qualifier.
         */
        ENTITY;
    }

    @Deprecated(forRemoval = true)
    @ApiStatus.Internal
    public static Statistic toLegacy(final io.papermc.paper.statistic.Statistic<?> stat) {
        if (stat.type() == StatisticType.CUSTOM && stat.value() instanceof final CustomStatistic customStatistic) {
            if (customStatistic == CustomStatistic.PLAY_TIME) { // special case cause upstream is wrong
                return Statistic.PLAY_ONE_MINUTE;
            } else {
                return Objects.requireNonNull(org.bukkit.Registry.STATISTIC.get(customStatistic.getKey()), "Couldn't convert " + stat + " to a legacy stat");
            }
        } else if (stat.type() == StatisticType.BLOCK_MINED) {
            return Statistic.MINE_BLOCK;
        } else if (stat.type() == StatisticType.ITEM_BROKEN) {
            return Statistic.BREAK_ITEM;
        } else if (stat.type() == StatisticType.ITEM_CRAFTED) {
            return Statistic.CRAFT_ITEM;
        } else if (stat.type() == StatisticType.ITEM_DROPPED) {
            return Statistic.DROP;
        } else if (stat.type() == StatisticType.ITEM_USED) {
            return Statistic.USE_ITEM;
        } else if (stat.type() == StatisticType.ITEM_PICKED_UP) {
            return Statistic.PICKUP;
        } else if (stat.type() == StatisticType.ENTITY_KILLED) {
            return Statistic.KILL_ENTITY;
        } else if (stat.type() == StatisticType.ENTITY_KILLED_BY) {
            return Statistic.ENTITY_KILLED_BY;
        }
        throw new IllegalArgumentException("Couldn't convert " + stat + " to a legacy stat");
    }

    @Deprecated(forRemoval = true)
    @ApiStatus.Internal
    public io.papermc.paper.statistic.Statistic<?> toModern(@Nullable EntityType entityType, @Nullable Material material) {
        Preconditions.checkArgument(entityType == null || material == null, "No stat has an entity type and material value at the same time");
        Preconditions.checkArgument(this.type != Type.UNTYPED || (entityType == null && material == null), "no value needed for untyped stats");
        Preconditions.checkArgument(this.type != Type.ENTITY || entityType != null);
        Preconditions.checkArgument(this.type != Type.BLOCK || material != null && material.isBlock());
        Preconditions.checkArgument(this.type != Type.ITEM || material != null && material.isItem());
        return switch (this.type) {
            case UNTYPED -> {
                if (this == PLAY_ONE_MINUTE) { // special case cause upstream is wrong
                    yield CustomStatistic.PLAY_TIME.stat();
                } else {
                    yield StatisticType.CUSTOM.of(Objects.requireNonNull(Registry.CUSTOM_STAT.get(this.key), "Couldn't convert " + this + " to a modern stat"));
                }
            }
            case BLOCK -> StatisticType.BLOCK_MINED.of(Objects.requireNonNull(material.asBlockType()));
            case ITEM -> switch (this) {
                case DROP -> StatisticType.ITEM_DROPPED.of(Objects.requireNonNull(material.asItemType()));
                case BREAK_ITEM -> StatisticType.ITEM_BROKEN.of(Objects.requireNonNull(material.asItemType()));
                case CRAFT_ITEM -> StatisticType.ITEM_CRAFTED.of(Objects.requireNonNull(material.asItemType()));
                case USE_ITEM -> StatisticType.ITEM_USED.of(Objects.requireNonNull(material.asItemType()));
                case PICKUP -> StatisticType.ITEM_PICKED_UP.of(Objects.requireNonNull(material.asItemType()));
                default -> throw new IllegalArgumentException("Couldn't convert " + this + ", mat: " + material + " to a modern stat");
            };
            case ENTITY -> switch (this) {
                case KILL_ENTITY -> StatisticType.ENTITY_KILLED.of(entityType);
                case ENTITY_KILLED_BY -> StatisticType.ENTITY_KILLED_BY.of(entityType);
                default -> throw new IllegalArgumentException("Couldn't convert " + this + ", entity_type: " + entityType + " to a modern stat");
            };
        };
    }
}
