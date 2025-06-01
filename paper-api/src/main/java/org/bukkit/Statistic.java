package org.bukkit;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a countable statistic, which is tracked by the server.
 */
public enum Statistic implements Keyed {
    // Start generate - StatisticCustom
    // @GeneratedFrom 1.21.6-pre1
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
    // @GeneratedFrom 1.21.6-pre1
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
     */
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
}
