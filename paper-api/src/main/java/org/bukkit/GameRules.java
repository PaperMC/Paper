package org.bukkit;

import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * All the vanilla gamerules
 */
@NullMarked
public final class GameRules {

    // Start generate - GameRules
    public static final GameRule<Boolean> ADVANCE_TIME = getRule("advance_time");

    public static final GameRule<Boolean> ADVANCE_WEATHER = getRule("advance_weather");

    public static final GameRule<Boolean> ALLOW_ENTERING_NETHER_USING_PORTALS = getRule("allow_entering_nether_using_portals");

    public static final GameRule<Boolean> BLOCK_DROPS = getRule("block_drops");

    public static final GameRule<Boolean> BLOCK_EXPLOSION_DROP_DECAY = getRule("block_explosion_drop_decay");

    public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = getRule("command_block_output");

    public static final GameRule<Boolean> COMMAND_BLOCKS_WORK = getRule("command_blocks_work");

    public static final GameRule<Boolean> DROWNING_DAMAGE = getRule("drowning_damage");

    public static final GameRule<Boolean> ELYTRA_MOVEMENT_CHECK = getRule("elytra_movement_check");

    public static final GameRule<Boolean> ENDER_PEARLS_VANISH_ON_DEATH = getRule("ender_pearls_vanish_on_death");

    public static final GameRule<Boolean> ENTITY_DROPS = getRule("entity_drops");

    public static final GameRule<Boolean> FALL_DAMAGE = getRule("fall_damage");

    public static final GameRule<Boolean> FIRE_DAMAGE = getRule("fire_damage");

    public static final GameRule<Integer> FIRE_SPREAD_RADIUS_AROUND_PLAYER = getRule("fire_spread_radius_around_player");

    public static final GameRule<Boolean> FORGIVE_DEAD_PLAYERS = getRule("forgive_dead_players");

    public static final GameRule<Boolean> FREEZE_DAMAGE = getRule("freeze_damage");

    public static final GameRule<Boolean> GLOBAL_SOUND_EVENTS = getRule("global_sound_events");

    public static final GameRule<Boolean> IMMEDIATE_RESPAWN = getRule("immediate_respawn");

    public static final GameRule<Boolean> KEEP_INVENTORY = getRule("keep_inventory");

    public static final GameRule<Boolean> LAVA_SOURCE_CONVERSION = getRule("lava_source_conversion");

    public static final GameRule<Boolean> LIMITED_CRAFTING = getRule("limited_crafting");

    public static final GameRule<Boolean> LOCATOR_BAR = getRule("locator_bar");

    public static final GameRule<Boolean> LOG_ADMIN_COMMANDS = getRule("log_admin_commands");

    public static final GameRule<Integer> MAX_BLOCK_MODIFICATIONS = getRule("max_block_modifications");

    public static final GameRule<Integer> MAX_COMMAND_FORKS = getRule("max_command_forks");

    public static final GameRule<Integer> MAX_COMMAND_SEQUENCE_LENGTH = getRule("max_command_sequence_length");

    public static final GameRule<Integer> MAX_ENTITY_CRAMMING = getRule("max_entity_cramming");

    @MinecraftExperimental(MinecraftExperimental.Requires.MINECART_IMPROVEMENTS)
    @ApiStatus.Experimental
    public static final GameRule<Integer> MAX_MINECART_SPEED = getRule("max_minecart_speed");

    public static final GameRule<Integer> MAX_SNOW_ACCUMULATION_HEIGHT = getRule("max_snow_accumulation_height");

    public static final GameRule<Boolean> MOB_DROPS = getRule("mob_drops");

    public static final GameRule<Boolean> MOB_EXPLOSION_DROP_DECAY = getRule("mob_explosion_drop_decay");

    public static final GameRule<Boolean> MOB_GRIEFING = getRule("mob_griefing");

    public static final GameRule<Boolean> NATURAL_HEALTH_REGENERATION = getRule("natural_health_regeneration");

    public static final GameRule<Boolean> PLAYER_MOVEMENT_CHECK = getRule("player_movement_check");

    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = getRule("players_nether_portal_creative_delay");

    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = getRule("players_nether_portal_default_delay");

    public static final GameRule<Integer> PLAYERS_SLEEPING_PERCENTAGE = getRule("players_sleeping_percentage");

    public static final GameRule<Boolean> PROJECTILES_CAN_BREAK_BLOCKS = getRule("projectiles_can_break_blocks");

    public static final GameRule<Boolean> PVP = getRule("pvp");

    public static final GameRule<Boolean> RAIDS = getRule("raids");

    public static final GameRule<Integer> RANDOM_TICK_SPEED = getRule("random_tick_speed");

    public static final GameRule<Boolean> REDUCED_DEBUG_INFO = getRule("reduced_debug_info");

    public static final GameRule<Integer> RESPAWN_RADIUS = getRule("respawn_radius");

    public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = getRule("send_command_feedback");

    public static final GameRule<Boolean> SHOW_ADVANCEMENT_MESSAGES = getRule("show_advancement_messages");

    public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = getRule("show_death_messages");

    public static final GameRule<Boolean> SPAWN_MOBS = getRule("spawn_mobs");

    public static final GameRule<Boolean> SPAWN_MONSTERS = getRule("spawn_monsters");

    public static final GameRule<Boolean> SPAWN_PATROLS = getRule("spawn_patrols");

    public static final GameRule<Boolean> SPAWN_PHANTOMS = getRule("spawn_phantoms");

    public static final GameRule<Boolean> SPAWN_WANDERING_TRADERS = getRule("spawn_wandering_traders");

    public static final GameRule<Boolean> SPAWN_WARDENS = getRule("spawn_wardens");

    public static final GameRule<Boolean> SPAWNER_BLOCKS_WORK = getRule("spawner_blocks_work");

    public static final GameRule<Boolean> SPECTATORS_GENERATE_CHUNKS = getRule("spectators_generate_chunks");

    public static final GameRule<Boolean> SPREAD_VINES = getRule("spread_vines");

    public static final GameRule<Boolean> TNT_EXPLODES = getRule("tnt_explodes");

    public static final GameRule<Boolean> TNT_EXPLOSION_DROP_DECAY = getRule("tnt_explosion_drop_decay");

    public static final GameRule<Boolean> UNIVERSAL_ANGER = getRule("universal_anger");

    public static final GameRule<Boolean> WATER_SOURCE_CONVERSION = getRule("water_source_conversion");
    // End generate - GameRules

    private static <T> GameRule<T> getRule(@KeyPattern.Value String key) {
        return (GameRule<T>) Registry.GAME_RULE.getOrThrow(NamespacedKey.minecraft(key));
    }

    private GameRules() {
    }
}
