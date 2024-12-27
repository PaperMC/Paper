package io.papermc.paper.statistic;

import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.ApiStatus;

/**
 * Custom statistic types.
 */
@ApiStatus.NonExtendable
public interface CustomStatistic extends Keyed, Translatable {

    Statistic<CustomStatistic> LEAVE_GAME = get("leave_game");
    Statistic<CustomStatistic> PLAY_TIME = get("play_time");
    Statistic<CustomStatistic> TOTAL_WORLD_TIME = get("total_world_time");
    Statistic<CustomStatistic> TIME_SINCE_DEATH = get("time_since_death");
    Statistic<CustomStatistic> TIME_SINCE_REST = get("time_since_rest");
    Statistic<CustomStatistic> SNEAK_TIME = get("sneak_time");
    Statistic<CustomStatistic> WALK_ONE_CM = get("walk_one_cm");
    Statistic<CustomStatistic> CROUCH_ONE_CM = get("crouch_one_cm");
    Statistic<CustomStatistic> SPRINT_ONE_CM = get("sprint_one_cm");
    Statistic<CustomStatistic> WALK_ON_WATER_ONE_CM = get("walk_on_water_one_cm");
    Statistic<CustomStatistic> FALL_ONE_CM = get("fall_one_cm");
    Statistic<CustomStatistic> CLIMB_ONE_CM = get("climb_one_cm");
    Statistic<CustomStatistic> FLY_ONE_CM = get("fly_one_cm");
    Statistic<CustomStatistic> WALK_UNDER_WATER_ONE_CM = get("walk_under_water_one_cm");
    Statistic<CustomStatistic> MINECART_ONE_CM = get("minecart_one_cm");
    Statistic<CustomStatistic> BOAT_ONE_CM = get("boat_one_cm");
    Statistic<CustomStatistic> PIG_ONE_CM = get("pig_one_cm");
    Statistic<CustomStatistic> HORSE_ONE_CM = get("horse_one_cm");
    Statistic<CustomStatistic> AVIATE_ONE_CM = get("aviate_one_cm");
    Statistic<CustomStatistic> SWIM_ONE_CM = get("swim_one_cm");
    Statistic<CustomStatistic> STRIDER_ONE_CM = get("strider_one_cm");
    Statistic<CustomStatistic> JUMP = get("jump");
    Statistic<CustomStatistic> DROP = get("drop");
    Statistic<CustomStatistic> DAMAGE_DEALT = get("damage_dealt");
    Statistic<CustomStatistic> DAMAGE_DEALT_ABSORBED = get("damage_dealt_absorbed");
    Statistic<CustomStatistic> DAMAGE_DEALT_RESISTED = get("damage_dealt_resisted");
    Statistic<CustomStatistic> DAMAGE_TAKEN = get("damage_taken");
    Statistic<CustomStatistic> DAMAGE_BLOCKED_BY_SHIELD = get("damage_blocked_by_shield");
    Statistic<CustomStatistic> DAMAGE_ABSORBED = get("damage_absorbed");
    Statistic<CustomStatistic> DAMAGE_RESISTED = get("damage_resisted");
    Statistic<CustomStatistic> DEATHS = get("deaths");
    Statistic<CustomStatistic> MOB_KILLS = get("mob_kills");
    Statistic<CustomStatistic> ANIMALS_BRED = get("animals_bred");
    Statistic<CustomStatistic> PLAYER_KILLS = get("player_kills");
    Statistic<CustomStatistic> FISH_CAUGHT = get("fish_caught");
    Statistic<CustomStatistic> TALKED_TO_VILLAGER = get("talked_to_villager");
    Statistic<CustomStatistic> TRADED_WITH_VILLAGER = get("traded_with_villager");
    Statistic<CustomStatistic> EAT_CAKE_SLICE = get("eat_cake_slice");
    Statistic<CustomStatistic> FILL_CAULDRON = get("fill_cauldron");
    Statistic<CustomStatistic> USE_CAULDRON = get("use_cauldron");
    Statistic<CustomStatistic> CLEAN_ARMOR = get("clean_armor");
    Statistic<CustomStatistic> CLEAN_BANNER = get("clean_banner");
    Statistic<CustomStatistic> CLEAN_SHULKER_BOX = get("clean_shulker_box");
    Statistic<CustomStatistic> INTERACT_WITH_BREWINGSTAND = get("interact_with_brewingstand");
    Statistic<CustomStatistic> INTERACT_WITH_BEACON = get("interact_with_beacon");
    Statistic<CustomStatistic> INSPECT_DROPPER = get("inspect_dropper");
    Statistic<CustomStatistic> INSPECT_HOPPER = get("inspect_hopper");
    Statistic<CustomStatistic> INSPECT_DISPENSER = get("inspect_dispenser");
    Statistic<CustomStatistic> PLAY_NOTEBLOCK = get("play_noteblock");
    Statistic<CustomStatistic> TUNE_NOTEBLOCK = get("tune_noteblock");
    Statistic<CustomStatistic> POT_FLOWER = get("pot_flower");
    Statistic<CustomStatistic> TRIGGER_TRAPPED_CHEST = get("trigger_trapped_chest");
    Statistic<CustomStatistic> OPEN_ENDERCHEST = get("open_enderchest");
    Statistic<CustomStatistic> ENCHANT_ITEM = get("enchant_item");
    Statistic<CustomStatistic> PLAY_RECORD = get("play_record");
    Statistic<CustomStatistic> INTERACT_WITH_FURNACE = get("interact_with_furnace");
    Statistic<CustomStatistic> INTERACT_WITH_CRAFTING_TABLE = get("interact_with_crafting_table");
    Statistic<CustomStatistic> OPEN_CHEST = get("open_chest");
    Statistic<CustomStatistic> SLEEP_IN_BED = get("sleep_in_bed");
    Statistic<CustomStatistic> OPEN_SHULKER_BOX = get("open_shulker_box");
    Statistic<CustomStatistic> OPEN_BARREL = get("open_barrel");
    Statistic<CustomStatistic> INTERACT_WITH_BLAST_FURNACE = get("interact_with_blast_furnace");
    Statistic<CustomStatistic> INTERACT_WITH_SMOKER = get("interact_with_smoker");
    Statistic<CustomStatistic> INTERACT_WITH_LECTERN = get("interact_with_lectern");
    Statistic<CustomStatistic> INTERACT_WITH_CAMPFIRE = get("interact_with_campfire");
    Statistic<CustomStatistic> INTERACT_WITH_CARTOGRAPHY_TABLE = get("interact_with_cartography_table");
    Statistic<CustomStatistic> INTERACT_WITH_LOOM = get("interact_with_loom");
    Statistic<CustomStatistic> INTERACT_WITH_STONECUTTER = get("interact_with_stonecutter");
    Statistic<CustomStatistic> BELL_RING = get("bell_ring");
    Statistic<CustomStatistic> RAID_TRIGGER = get("raid_trigger");
    Statistic<CustomStatistic> RAID_WIN = get("raid_win");
    Statistic<CustomStatistic> INTERACT_WITH_ANVIL = get("interact_with_anvil");
    Statistic<CustomStatistic> INTERACT_WITH_GRINDSTONE = get("interact_with_grindstone");
    Statistic<CustomStatistic> TARGET_HIT = get("target_hit");
    Statistic<CustomStatistic> INTERACT_WITH_SMITHING_TABLE = get("interact_with_smithing_table");

    private static Statistic<CustomStatistic> get(final String key) {
        return StatisticType.CUSTOM.of(Registry.CUSTOM_STAT.getOrThrow(NamespacedKey.minecraft(key)));
    }
}
