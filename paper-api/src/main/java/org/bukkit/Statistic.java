package org.bukkit;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a countable statistic, which is tracked by the server.
 * @deprecated use {@link io.papermc.paper.statistic.StatisticType} and {@link io.papermc.paper.statistic.Statistic}
 */
@Deprecated(since = "1.21.4") // Paper
public enum Statistic implements Keyed {
    DAMAGE_DEALT,
    DAMAGE_TAKEN,
    DEATHS,
    MOB_KILLS,
    PLAYER_KILLS,
    FISH_CAUGHT,
    ANIMALS_BRED,
    LEAVE_GAME,
    JUMP,
    DROP_COUNT,
    DROP(Type.ITEM),
    PICKUP(Type.ITEM),
    /**
     * Name is misleading, actually records ticks played.
     */
    PLAY_ONE_MINUTE,
    TOTAL_WORLD_TIME,
    WALK_ONE_CM,
    WALK_ON_WATER_ONE_CM,
    FALL_ONE_CM,
    SNEAK_TIME,
    CLIMB_ONE_CM,
    FLY_ONE_CM,
    WALK_UNDER_WATER_ONE_CM,
    MINECART_ONE_CM,
    BOAT_ONE_CM,
    PIG_ONE_CM,
    HORSE_ONE_CM,
    SPRINT_ONE_CM,
    CROUCH_ONE_CM,
    AVIATE_ONE_CM,
    MINE_BLOCK(Type.BLOCK),
    USE_ITEM(Type.ITEM),
    BREAK_ITEM(Type.ITEM),
    CRAFT_ITEM(Type.ITEM),
    KILL_ENTITY(Type.ENTITY),
    ENTITY_KILLED_BY(Type.ENTITY),
    TIME_SINCE_DEATH,
    TALKED_TO_VILLAGER,
    TRADED_WITH_VILLAGER,
    CAKE_SLICES_EATEN,
    CAULDRON_FILLED,
    CAULDRON_USED,
    ARMOR_CLEANED,
    BANNER_CLEANED,
    BREWINGSTAND_INTERACTION,
    BEACON_INTERACTION,
    DROPPER_INSPECTED,
    HOPPER_INSPECTED,
    DISPENSER_INSPECTED,
    NOTEBLOCK_PLAYED,
    NOTEBLOCK_TUNED,
    FLOWER_POTTED,
    TRAPPED_CHEST_TRIGGERED,
    ENDERCHEST_OPENED,
    ITEM_ENCHANTED,
    RECORD_PLAYED,
    FURNACE_INTERACTION,
    CRAFTING_TABLE_INTERACTION,
    CHEST_OPENED,
    SLEEP_IN_BED,
    SHULKER_BOX_OPENED,
    TIME_SINCE_REST,
    SWIM_ONE_CM,
    DAMAGE_DEALT_ABSORBED,
    DAMAGE_DEALT_RESISTED,
    DAMAGE_BLOCKED_BY_SHIELD,
    DAMAGE_ABSORBED,
    DAMAGE_RESISTED,
    CLEAN_SHULKER_BOX,
    OPEN_BARREL,
    INTERACT_WITH_BLAST_FURNACE,
    INTERACT_WITH_SMOKER,
    INTERACT_WITH_LECTERN,
    INTERACT_WITH_CAMPFIRE,
    INTERACT_WITH_CARTOGRAPHY_TABLE,
    INTERACT_WITH_LOOM,
    INTERACT_WITH_STONECUTTER,
    BELL_RING,
    RAID_TRIGGER,
    RAID_WIN,
    INTERACT_WITH_ANVIL,
    INTERACT_WITH_GRINDSTONE,
    TARGET_HIT,
    INTERACT_WITH_SMITHING_TABLE,
    STRIDER_ONE_CM;

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
    @Deprecated(since = "1.21") // Paper
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
    // Paper start - add legacy conversion methods
    @Deprecated(forRemoval = true)
    @org.jetbrains.annotations.ApiStatus.Internal
    public static Statistic toLegacy(final io.papermc.paper.statistic.Statistic<?> stat) {
        if (stat.type() == io.papermc.paper.statistic.StatisticType.CUSTOM && stat.value() instanceof final io.papermc.paper.statistic.CustomStatistic customStatistic) {
            if (customStatistic == io.papermc.paper.statistic.CustomStatistic.PLAY_TIME) { // special case cause upstream is wrong
                return org.bukkit.Statistic.PLAY_ONE_MINUTE;
            } else {
                return java.util.Objects.requireNonNull(org.bukkit.Registry.STATISTIC.get(customStatistic.getKey()), "Couldn't convert " + stat + " to a legacy stat");
            }
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.BLOCK_MINED) {
            return Statistic.MINE_BLOCK;
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.ITEM_BROKEN) {
            return Statistic.BREAK_ITEM;
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.ITEM_CRAFTED) {
            return Statistic.CRAFT_ITEM;
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.ITEM_DROPPED) {
            return Statistic.DROP;
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.ITEM_USED) {
            return Statistic.USE_ITEM;
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.ITEM_PICKED_UP) {
            return Statistic.PICKUP;
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.ENTITY_KILLED) {
            return Statistic.KILL_ENTITY;
        } else if (stat.type() == io.papermc.paper.statistic.StatisticType.ENTITY_KILLED_BY) {
            return Statistic.ENTITY_KILLED_BY;
        }
        throw new IllegalArgumentException("Couldn't convert " + stat + " to a legacy stat");
    }

    @Deprecated(forRemoval = true)
    @org.jetbrains.annotations.ApiStatus.Internal
    public io.papermc.paper.statistic.Statistic<?> toModern(@org.jetbrains.annotations.Nullable org.bukkit.entity.EntityType entityType, @org.jetbrains.annotations.Nullable Material material) {
        com.google.common.base.Preconditions.checkArgument(entityType == null || material == null, "No stat has an entity type and material value at the same time");
        com.google.common.base.Preconditions.checkArgument(this.type != Type.UNTYPED || (entityType == null && material == null), "no value needed for untyped stats");
        com.google.common.base.Preconditions.checkArgument(this.type != Type.ENTITY || entityType != null);
        com.google.common.base.Preconditions.checkArgument(this.type != Type.BLOCK || material != null && material.isBlock());
        com.google.common.base.Preconditions.checkArgument(this.type != Type.ITEM || material != null && material.isItem());
        return switch (this.type) {
            case UNTYPED -> {
                if (this == PLAY_ONE_MINUTE) { // special case cause upstream is wrong
                    yield io.papermc.paper.statistic.CustomStatistic.PLAY_TIME;
                } else {
                    yield io.papermc.paper.statistic.StatisticType.CUSTOM.of(java.util.Objects.requireNonNull(Registry.CUSTOM_STAT.get(this.key), "Couldn't convert " + this + " to a modern stat"));
                }
            }
            case BLOCK -> io.papermc.paper.statistic.StatisticType.BLOCK_MINED.of(java.util.Objects.requireNonNull(material.asBlockType()));
            case ITEM -> switch (this) {
                case DROP -> io.papermc.paper.statistic.StatisticType.ITEM_DROPPED.of(java.util.Objects.requireNonNull(material.asItemType()));
                case BREAK_ITEM -> io.papermc.paper.statistic.StatisticType.ITEM_BROKEN.of(java.util.Objects.requireNonNull(material.asItemType()));
                case CRAFT_ITEM -> io.papermc.paper.statistic.StatisticType.ITEM_CRAFTED.of(java.util.Objects.requireNonNull(material.asItemType()));
                case USE_ITEM -> io.papermc.paper.statistic.StatisticType.ITEM_USED.of(java.util.Objects.requireNonNull(material.asItemType()));
                case PICKUP -> io.papermc.paper.statistic.StatisticType.ITEM_PICKED_UP.of(java.util.Objects.requireNonNull(material.asItemType()));
                default -> throw new IllegalArgumentException("Couldn't convert " + this + ", mat: " + material + " to a modern stat");
            };
            case ENTITY -> switch (this) {
                case KILL_ENTITY -> io.papermc.paper.statistic.StatisticType.ENTITY_KILLED.of(entityType);
                case ENTITY_KILLED_BY -> io.papermc.paper.statistic.StatisticType.ENTITY_KILLED_BY.of(entityType);
                default -> throw new IllegalArgumentException("Couldn't convert " + this + ", entity_type: " + entityType + " to a modern stat");
            };
        };
    }
    // Paper end
}
