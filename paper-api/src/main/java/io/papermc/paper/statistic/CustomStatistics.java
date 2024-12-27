package io.papermc.paper.statistic;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Registry;

/**
 * All custom statistics.
 */
public final class CustomStatistics {

    // Start generate - CustomStatistics
    public static final CustomStatistic ANIMALS_BRED = get("animals_bred");

    public static final CustomStatistic AVIATE_ONE_CM = get("aviate_one_cm");

    public static final CustomStatistic BELL_RING = get("bell_ring");

    public static final CustomStatistic BOAT_ONE_CM = get("boat_one_cm");

    public static final CustomStatistic CLEAN_ARMOR = get("clean_armor");

    public static final CustomStatistic CLEAN_BANNER = get("clean_banner");

    public static final CustomStatistic CLEAN_SHULKER_BOX = get("clean_shulker_box");

    public static final CustomStatistic CLIMB_ONE_CM = get("climb_one_cm");

    public static final CustomStatistic CROUCH_ONE_CM = get("crouch_one_cm");

    public static final CustomStatistic DAMAGE_ABSORBED = get("damage_absorbed");

    public static final CustomStatistic DAMAGE_BLOCKED_BY_SHIELD = get("damage_blocked_by_shield");

    public static final CustomStatistic DAMAGE_DEALT = get("damage_dealt");

    public static final CustomStatistic DAMAGE_DEALT_ABSORBED = get("damage_dealt_absorbed");

    public static final CustomStatistic DAMAGE_DEALT_RESISTED = get("damage_dealt_resisted");

    public static final CustomStatistic DAMAGE_RESISTED = get("damage_resisted");

    public static final CustomStatistic DAMAGE_TAKEN = get("damage_taken");

    public static final CustomStatistic DEATHS = get("deaths");

    public static final CustomStatistic DROP = get("drop");

    public static final CustomStatistic EAT_CAKE_SLICE = get("eat_cake_slice");

    public static final CustomStatistic ENCHANT_ITEM = get("enchant_item");

    public static final CustomStatistic FALL_ONE_CM = get("fall_one_cm");

    public static final CustomStatistic FILL_CAULDRON = get("fill_cauldron");

    public static final CustomStatistic FISH_CAUGHT = get("fish_caught");

    public static final CustomStatistic FLY_ONE_CM = get("fly_one_cm");

    public static final CustomStatistic HAPPY_GHAST_ONE_CM = get("happy_ghast_one_cm");

    public static final CustomStatistic HORSE_ONE_CM = get("horse_one_cm");

    public static final CustomStatistic INSPECT_DISPENSER = get("inspect_dispenser");

    public static final CustomStatistic INSPECT_DROPPER = get("inspect_dropper");

    public static final CustomStatistic INSPECT_HOPPER = get("inspect_hopper");

    public static final CustomStatistic INTERACT_WITH_ANVIL = get("interact_with_anvil");

    public static final CustomStatistic INTERACT_WITH_BEACON = get("interact_with_beacon");

    public static final CustomStatistic INTERACT_WITH_BLAST_FURNACE = get("interact_with_blast_furnace");

    public static final CustomStatistic INTERACT_WITH_BREWINGSTAND = get("interact_with_brewingstand");

    public static final CustomStatistic INTERACT_WITH_CAMPFIRE = get("interact_with_campfire");

    public static final CustomStatistic INTERACT_WITH_CARTOGRAPHY_TABLE = get("interact_with_cartography_table");

    public static final CustomStatistic INTERACT_WITH_CRAFTING_TABLE = get("interact_with_crafting_table");

    public static final CustomStatistic INTERACT_WITH_FURNACE = get("interact_with_furnace");

    public static final CustomStatistic INTERACT_WITH_GRINDSTONE = get("interact_with_grindstone");

    public static final CustomStatistic INTERACT_WITH_LECTERN = get("interact_with_lectern");

    public static final CustomStatistic INTERACT_WITH_LOOM = get("interact_with_loom");

    public static final CustomStatistic INTERACT_WITH_SMITHING_TABLE = get("interact_with_smithing_table");

    public static final CustomStatistic INTERACT_WITH_SMOKER = get("interact_with_smoker");

    public static final CustomStatistic INTERACT_WITH_STONECUTTER = get("interact_with_stonecutter");

    public static final CustomStatistic JUMP = get("jump");

    public static final CustomStatistic LEAVE_GAME = get("leave_game");

    public static final CustomStatistic MINECART_ONE_CM = get("minecart_one_cm");

    public static final CustomStatistic MOB_KILLS = get("mob_kills");

    public static final CustomStatistic NAUTILUS_ONE_CM = get("nautilus_one_cm");

    public static final CustomStatistic OPEN_BARREL = get("open_barrel");

    public static final CustomStatistic OPEN_CHEST = get("open_chest");

    public static final CustomStatistic OPEN_ENDERCHEST = get("open_enderchest");

    public static final CustomStatistic OPEN_SHULKER_BOX = get("open_shulker_box");

    public static final CustomStatistic PIG_ONE_CM = get("pig_one_cm");

    public static final CustomStatistic PLAY_NOTEBLOCK = get("play_noteblock");

    public static final CustomStatistic PLAY_RECORD = get("play_record");

    public static final CustomStatistic PLAY_TIME = get("play_time");

    public static final CustomStatistic PLAYER_KILLS = get("player_kills");

    public static final CustomStatistic POT_FLOWER = get("pot_flower");

    public static final CustomStatistic RAID_TRIGGER = get("raid_trigger");

    public static final CustomStatistic RAID_WIN = get("raid_win");

    public static final CustomStatistic SLEEP_IN_BED = get("sleep_in_bed");

    public static final CustomStatistic SNEAK_TIME = get("sneak_time");

    public static final CustomStatistic SPRINT_ONE_CM = get("sprint_one_cm");

    public static final CustomStatistic STRIDER_ONE_CM = get("strider_one_cm");

    public static final CustomStatistic SWIM_ONE_CM = get("swim_one_cm");

    public static final CustomStatistic TALKED_TO_VILLAGER = get("talked_to_villager");

    public static final CustomStatistic TARGET_HIT = get("target_hit");

    public static final CustomStatistic TIME_SINCE_DEATH = get("time_since_death");

    public static final CustomStatistic TIME_SINCE_REST = get("time_since_rest");

    public static final CustomStatistic TOTAL_WORLD_TIME = get("total_world_time");

    public static final CustomStatistic TRADED_WITH_VILLAGER = get("traded_with_villager");

    public static final CustomStatistic TRIGGER_TRAPPED_CHEST = get("trigger_trapped_chest");

    public static final CustomStatistic TUNE_NOTEBLOCK = get("tune_noteblock");

    public static final CustomStatistic USE_CAULDRON = get("use_cauldron");

    public static final CustomStatistic WALK_ON_WATER_ONE_CM = get("walk_on_water_one_cm");

    public static final CustomStatistic WALK_ONE_CM = get("walk_one_cm");

    public static final CustomStatistic WALK_UNDER_WATER_ONE_CM = get("walk_under_water_one_cm");
    // End generate - CustomStatistics

    private static CustomStatistic get(@KeyPattern.Value final String key) {
        return Registry.CUSTOM_STAT.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    private CustomStatistics() {
    }
}
