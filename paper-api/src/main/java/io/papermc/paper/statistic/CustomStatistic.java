package io.papermc.paper.statistic;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jetbrains.annotations.ApiStatus;

/**
 * Custom statistic types.
 */
@ApiStatus.NonExtendable
public interface CustomStatistic extends Keyed, Translatable {

    // Start generate - CustomStatistic
    // @GeneratedFrom 1.21.6-rc1
    CustomStatistic ANIMALS_BRED = get("animals_bred");

    CustomStatistic AVIATE_ONE_CM = get("aviate_one_cm");

    CustomStatistic BELL_RING = get("bell_ring");

    CustomStatistic BOAT_ONE_CM = get("boat_one_cm");

    CustomStatistic CLEAN_ARMOR = get("clean_armor");

    CustomStatistic CLEAN_BANNER = get("clean_banner");

    CustomStatistic CLEAN_SHULKER_BOX = get("clean_shulker_box");

    CustomStatistic CLIMB_ONE_CM = get("climb_one_cm");

    CustomStatistic CROUCH_ONE_CM = get("crouch_one_cm");

    CustomStatistic DAMAGE_ABSORBED = get("damage_absorbed");

    CustomStatistic DAMAGE_BLOCKED_BY_SHIELD = get("damage_blocked_by_shield");

    CustomStatistic DAMAGE_DEALT = get("damage_dealt");

    CustomStatistic DAMAGE_DEALT_ABSORBED = get("damage_dealt_absorbed");

    CustomStatistic DAMAGE_DEALT_RESISTED = get("damage_dealt_resisted");

    CustomStatistic DAMAGE_RESISTED = get("damage_resisted");

    CustomStatistic DAMAGE_TAKEN = get("damage_taken");

    CustomStatistic DEATHS = get("deaths");

    CustomStatistic DROP = get("drop");

    CustomStatistic EAT_CAKE_SLICE = get("eat_cake_slice");

    CustomStatistic ENCHANT_ITEM = get("enchant_item");

    CustomStatistic FALL_ONE_CM = get("fall_one_cm");

    CustomStatistic FILL_CAULDRON = get("fill_cauldron");

    CustomStatistic FISH_CAUGHT = get("fish_caught");

    CustomStatistic FLY_ONE_CM = get("fly_one_cm");

    CustomStatistic HAPPY_GHAST_ONE_CM = get("happy_ghast_one_cm");

    CustomStatistic HORSE_ONE_CM = get("horse_one_cm");

    CustomStatistic INSPECT_DISPENSER = get("inspect_dispenser");

    CustomStatistic INSPECT_DROPPER = get("inspect_dropper");

    CustomStatistic INSPECT_HOPPER = get("inspect_hopper");

    CustomStatistic INTERACT_WITH_ANVIL = get("interact_with_anvil");

    CustomStatistic INTERACT_WITH_BEACON = get("interact_with_beacon");

    CustomStatistic INTERACT_WITH_BLAST_FURNACE = get("interact_with_blast_furnace");

    CustomStatistic INTERACT_WITH_BREWINGSTAND = get("interact_with_brewingstand");

    CustomStatistic INTERACT_WITH_CAMPFIRE = get("interact_with_campfire");

    CustomStatistic INTERACT_WITH_CARTOGRAPHY_TABLE = get("interact_with_cartography_table");

    CustomStatistic INTERACT_WITH_CRAFTING_TABLE = get("interact_with_crafting_table");

    CustomStatistic INTERACT_WITH_FURNACE = get("interact_with_furnace");

    CustomStatistic INTERACT_WITH_GRINDSTONE = get("interact_with_grindstone");

    CustomStatistic INTERACT_WITH_LECTERN = get("interact_with_lectern");

    CustomStatistic INTERACT_WITH_LOOM = get("interact_with_loom");

    CustomStatistic INTERACT_WITH_SMITHING_TABLE = get("interact_with_smithing_table");

    CustomStatistic INTERACT_WITH_SMOKER = get("interact_with_smoker");

    CustomStatistic INTERACT_WITH_STONECUTTER = get("interact_with_stonecutter");

    CustomStatistic JUMP = get("jump");

    CustomStatistic LEAVE_GAME = get("leave_game");

    CustomStatistic MINECART_ONE_CM = get("minecart_one_cm");

    CustomStatistic MOB_KILLS = get("mob_kills");

    CustomStatistic OPEN_BARREL = get("open_barrel");

    CustomStatistic OPEN_CHEST = get("open_chest");

    CustomStatistic OPEN_ENDERCHEST = get("open_enderchest");

    CustomStatistic OPEN_SHULKER_BOX = get("open_shulker_box");

    CustomStatistic PIG_ONE_CM = get("pig_one_cm");

    CustomStatistic PLAY_NOTEBLOCK = get("play_noteblock");

    CustomStatistic PLAY_RECORD = get("play_record");

    CustomStatistic PLAY_TIME = get("play_time");

    CustomStatistic PLAYER_KILLS = get("player_kills");

    CustomStatistic POT_FLOWER = get("pot_flower");

    CustomStatistic RAID_TRIGGER = get("raid_trigger");

    CustomStatistic RAID_WIN = get("raid_win");

    CustomStatistic SLEEP_IN_BED = get("sleep_in_bed");

    CustomStatistic SNEAK_TIME = get("sneak_time");

    CustomStatistic SPRINT_ONE_CM = get("sprint_one_cm");

    CustomStatistic STRIDER_ONE_CM = get("strider_one_cm");

    CustomStatistic SWIM_ONE_CM = get("swim_one_cm");

    CustomStatistic TALKED_TO_VILLAGER = get("talked_to_villager");

    CustomStatistic TARGET_HIT = get("target_hit");

    CustomStatistic TIME_SINCE_DEATH = get("time_since_death");

    CustomStatistic TIME_SINCE_REST = get("time_since_rest");

    CustomStatistic TOTAL_WORLD_TIME = get("total_world_time");

    CustomStatistic TRADED_WITH_VILLAGER = get("traded_with_villager");

    CustomStatistic TRIGGER_TRAPPED_CHEST = get("trigger_trapped_chest");

    CustomStatistic TUNE_NOTEBLOCK = get("tune_noteblock");

    CustomStatistic USE_CAULDRON = get("use_cauldron");

    CustomStatistic WALK_ON_WATER_ONE_CM = get("walk_on_water_one_cm");

    CustomStatistic WALK_ONE_CM = get("walk_one_cm");

    CustomStatistic WALK_UNDER_WATER_ONE_CM = get("walk_under_water_one_cm");
    // End generate - CustomStatistic

    /**
     * Gets the statistic with the given custom stat.
     *
     * @return the statistic for the custom stat.
     */
    default Statistic<CustomStatistic> stat() {
        return StatisticType.CUSTOM.of(this);
    }

    private static CustomStatistic get(@KeyPattern.Value final String key) {
        return Registry.CUSTOM_STAT.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
