package org.bukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.statistic.CustomStatistic;
import io.papermc.paper.statistic.StatisticType;
import io.papermc.paper.statistic.StatisticTypes;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a countable statistic, which is tracked by the server.
 *
 * @deprecated use {@link StatisticType} and {@link io.papermc.paper.statistic.Statistic}
 */
@NullMarked
@Deprecated(since = "1.21.11")
public enum Statistic implements Keyed {
    // Start generate - StatisticCustom
    ANIMALS_BRED("animals_bred"),
    AVIATE_ONE_CM("aviate_one_cm"),
    BELL_RING("bell_ring"),
    BOAT_ONE_CM("boat_one_cm"),
    ARMOR_CLEANED("clean_armor"),
    BANNER_CLEANED("clean_banner"),
    CLEAN_SHULKER_BOX("clean_shulker_box"),
    CLIMB_ONE_CM("climb_one_cm"),
    CROUCH_ONE_CM("crouch_one_cm"),
    DAMAGE_ABSORBED("damage_absorbed"),
    DAMAGE_BLOCKED_BY_SHIELD("damage_blocked_by_shield"),
    DAMAGE_DEALT("damage_dealt"),
    DAMAGE_DEALT_ABSORBED("damage_dealt_absorbed"),
    DAMAGE_DEALT_RESISTED("damage_dealt_resisted"),
    DAMAGE_RESISTED("damage_resisted"),
    DAMAGE_TAKEN("damage_taken"),
    DEATHS("deaths"),
    DROP_COUNT("drop"),
    CAKE_SLICES_EATEN("eat_cake_slice"),
    ITEM_ENCHANTED("enchant_item"),
    FALL_ONE_CM("fall_one_cm"),
    CAULDRON_FILLED("fill_cauldron"),
    FISH_CAUGHT("fish_caught"),
    FLY_ONE_CM("fly_one_cm"),
    HAPPY_GHAST_ONE_CM("happy_ghast_one_cm"),
    HORSE_ONE_CM("horse_one_cm"),
    DISPENSER_INSPECTED("inspect_dispenser"),
    DROPPER_INSPECTED("inspect_dropper"),
    HOPPER_INSPECTED("inspect_hopper"),
    INTERACT_WITH_ANVIL("interact_with_anvil"),
    BEACON_INTERACTION("interact_with_beacon"),
    INTERACT_WITH_BLAST_FURNACE("interact_with_blast_furnace"),
    BREWINGSTAND_INTERACTION("interact_with_brewingstand"),
    INTERACT_WITH_CAMPFIRE("interact_with_campfire"),
    INTERACT_WITH_CARTOGRAPHY_TABLE("interact_with_cartography_table"),
    CRAFTING_TABLE_INTERACTION("interact_with_crafting_table"),
    FURNACE_INTERACTION("interact_with_furnace"),
    INTERACT_WITH_GRINDSTONE("interact_with_grindstone"),
    INTERACT_WITH_LECTERN("interact_with_lectern"),
    INTERACT_WITH_LOOM("interact_with_loom"),
    INTERACT_WITH_SMITHING_TABLE("interact_with_smithing_table"),
    INTERACT_WITH_SMOKER("interact_with_smoker"),
    INTERACT_WITH_STONECUTTER("interact_with_stonecutter"),
    JUMP("jump"),
    LEAVE_GAME("leave_game"),
    MINECART_ONE_CM("minecart_one_cm"),
    MOB_KILLS("mob_kills"),
    NAUTILUS_ONE_CM("nautilus_one_cm"),
    OPEN_BARREL("open_barrel"),
    CHEST_OPENED("open_chest"),
    ENDERCHEST_OPENED("open_enderchest"),
    SHULKER_BOX_OPENED("open_shulker_box"),
    PIG_ONE_CM("pig_one_cm"),
    NOTEBLOCK_PLAYED("play_noteblock"),
    RECORD_PLAYED("play_record"),
    PLAY_ONE_MINUTE("play_time"),
    PLAYER_KILLS("player_kills"),
    FLOWER_POTTED("pot_flower"),
    RAID_TRIGGER("raid_trigger"),
    RAID_WIN("raid_win"),
    SLEEP_IN_BED("sleep_in_bed"),
    SNEAK_TIME("sneak_time"),
    SPRINT_ONE_CM("sprint_one_cm"),
    STRIDER_ONE_CM("strider_one_cm"),
    SWIM_ONE_CM("swim_one_cm"),
    TALKED_TO_VILLAGER("talked_to_villager"),
    TARGET_HIT("target_hit"),
    TIME_SINCE_DEATH("time_since_death"),
    TIME_SINCE_REST("time_since_rest"),
    TOTAL_WORLD_TIME("total_world_time"),
    TRADED_WITH_VILLAGER("traded_with_villager"),
    TRAPPED_CHEST_TRIGGERED("trigger_trapped_chest"),
    NOTEBLOCK_TUNED("tune_noteblock"),
    CAULDRON_USED("use_cauldron"),
    WALK_ON_WATER_ONE_CM("walk_on_water_one_cm"),
    WALK_ONE_CM("walk_one_cm"),
    WALK_UNDER_WATER_ONE_CM("walk_under_water_one_cm"),
    // End generate - StatisticCustom
    // Start generate - StatisticType
    BREAK_ITEM("broken", Type.ITEM),
    CRAFT_ITEM("crafted", Type.ITEM),
    DROP("dropped", Type.ITEM),
    KILL_ENTITY("killed", Type.ENTITY),
    ENTITY_KILLED_BY("killed_by", Type.ENTITY),
    MINE_BLOCK("mined", Type.BLOCK),
    PICKUP("picked_up", Type.ITEM),
    USE_ITEM("used", Type.ITEM);
    // End generate - StatisticType

    private final Type type;
    private final NamespacedKey key;

    Statistic(final String key) {
        this(key, Type.UNTYPED);
    }

    Statistic(final/* @NotNull */String key, final/* @NotNull */Type type) {
        this.type = type;
        this.key = NamespacedKey.minecraft(key);
    }

    /**
     * Gets the type of this statistic.
     *
     * @return the type of this statistic
     */
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

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * The type of statistic.
     *
     * @deprecated use {@link StatisticType}
     */
    @Deprecated(since = "1.21.11")
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

    @Deprecated
    @ApiStatus.Internal
    public static Statistic toLegacy(final io.papermc.paper.statistic.Statistic<?> stat) {
        Key key = stat.type().key();
        if (stat.type() == StatisticTypes.CUSTOM && stat.owner() instanceof final CustomStatistic customStatistic) {
            key = customStatistic.getKey();
        }
        return Objects.requireNonNull(Registry.STATISTIC.get(key), "Couldn't convert " + stat + " to a legacy stat");
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    @ApiStatus.Internal
    public io.papermc.paper.statistic.Statistic<?> toModern(@Nullable EntityType entityType, @Nullable Material material) {
        Preconditions.checkArgument(entityType == null || material == null, "No stat has an entity type and material value at the same time");
        Preconditions.checkArgument(this.type != Type.UNTYPED || (entityType == null && material == null), "no value needed for untyped stats");
        Preconditions.checkArgument(this.type != Type.ENTITY || (entityType != null && entityType != EntityType.UNKNOWN), "Must provide a valid entity type");
        Preconditions.checkArgument(this.type != Type.BLOCK || material != null && material.isBlock(), "Must provide a valid block material");
        Preconditions.checkArgument(this.type != Type.ITEM || material != null && material.isItem(), "Must provide a valid item material");
        if (this.type == Type.UNTYPED) {
            return StatisticTypes.CUSTOM.forValue(Objects.requireNonNull(Registry.CUSTOM_STAT.get(this.key), "Couldn't convert " + this + " to a modern stat"));
        } else {
            StatisticType<?> statType = Objects.requireNonNull(Registry.STAT_TYPE.get(this.key), "Couldn't convert " + this + " to a modern stat");
            return switch (this.type) {
                case Type.BLOCK -> ((StatisticType<BlockType>) statType).forValue(Objects.requireNonNull(material.asBlockType()));
                case Type.ITEM -> ((StatisticType<ItemType>) statType).forValue(Objects.requireNonNull(material.asItemType()));
                case Type.ENTITY -> ((StatisticType<EntityType>) statType).forValue(Objects.requireNonNull(entityType));
                default -> throw new IllegalStateException("Unexpected value: " + this.type);
            };
        }
    }
}
