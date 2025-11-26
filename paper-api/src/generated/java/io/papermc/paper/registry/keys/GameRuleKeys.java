package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.GameRule;
import org.bukkit.MinecraftExperimental;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#GAME_RULE}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedClass
public final class GameRuleKeys {
    /**
     * {@code minecraft:advance_time}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> ADVANCE_TIME = create(key("advance_time"));

    /**
     * {@code minecraft:advance_weather}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> ADVANCE_WEATHER = create(key("advance_weather"));

    /**
     * {@code minecraft:allow_entering_nether_using_portals}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> ALLOW_ENTERING_NETHER_USING_PORTALS = create(key("allow_entering_nether_using_portals"));

    /**
     * {@code minecraft:block_drops}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> BLOCK_DROPS = create(key("block_drops"));

    /**
     * {@code minecraft:block_explosion_drop_decay}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> BLOCK_EXPLOSION_DROP_DECAY = create(key("block_explosion_drop_decay"));

    /**
     * {@code minecraft:command_block_output}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> COMMAND_BLOCK_OUTPUT = create(key("command_block_output"));

    /**
     * {@code minecraft:command_blocks_work}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> COMMAND_BLOCKS_WORK = create(key("command_blocks_work"));

    /**
     * {@code minecraft:drowning_damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> DROWNING_DAMAGE = create(key("drowning_damage"));

    /**
     * {@code minecraft:elytra_movement_check}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> ELYTRA_MOVEMENT_CHECK = create(key("elytra_movement_check"));

    /**
     * {@code minecraft:ender_pearls_vanish_on_death}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> ENDER_PEARLS_VANISH_ON_DEATH = create(key("ender_pearls_vanish_on_death"));

    /**
     * {@code minecraft:entity_drops}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> ENTITY_DROPS = create(key("entity_drops"));

    /**
     * {@code minecraft:fall_damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> FALL_DAMAGE = create(key("fall_damage"));

    /**
     * {@code minecraft:fire_damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> FIRE_DAMAGE = create(key("fire_damage"));

    /**
     * {@code minecraft:fire_spread_radius_around_player}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> FIRE_SPREAD_RADIUS_AROUND_PLAYER = create(key("fire_spread_radius_around_player"));

    /**
     * {@code minecraft:forgive_dead_players}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> FORGIVE_DEAD_PLAYERS = create(key("forgive_dead_players"));

    /**
     * {@code minecraft:freeze_damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> FREEZE_DAMAGE = create(key("freeze_damage"));

    /**
     * {@code minecraft:global_sound_events}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> GLOBAL_SOUND_EVENTS = create(key("global_sound_events"));

    /**
     * {@code minecraft:immediate_respawn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> IMMEDIATE_RESPAWN = create(key("immediate_respawn"));

    /**
     * {@code minecraft:keep_inventory}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> KEEP_INVENTORY = create(key("keep_inventory"));

    /**
     * {@code minecraft:lava_source_conversion}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> LAVA_SOURCE_CONVERSION = create(key("lava_source_conversion"));

    /**
     * {@code minecraft:limited_crafting}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> LIMITED_CRAFTING = create(key("limited_crafting"));

    /**
     * {@code minecraft:locator_bar}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> LOCATOR_BAR = create(key("locator_bar"));

    /**
     * {@code minecraft:log_admin_commands}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> LOG_ADMIN_COMMANDS = create(key("log_admin_commands"));

    /**
     * {@code minecraft:max_block_modifications}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MAX_BLOCK_MODIFICATIONS = create(key("max_block_modifications"));

    /**
     * {@code minecraft:max_command_forks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MAX_COMMAND_FORKS = create(key("max_command_forks"));

    /**
     * {@code minecraft:max_command_sequence_length}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MAX_COMMAND_SEQUENCE_LENGTH = create(key("max_command_sequence_length"));

    /**
     * {@code minecraft:max_entity_cramming}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MAX_ENTITY_CRAMMING = create(key("max_entity_cramming"));

    /**
     * {@code minecraft:max_minecart_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    @ApiStatus.Experimental
    @MinecraftExperimental(MinecraftExperimental.Requires.MINECART_IMPROVEMENTS)
    public static final TypedKey<GameRule<?>> MAX_MINECART_SPEED = create(key("max_minecart_speed"));

    /**
     * {@code minecraft:max_snow_accumulation_height}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MAX_SNOW_ACCUMULATION_HEIGHT = create(key("max_snow_accumulation_height"));

    /**
     * {@code minecraft:mob_drops}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MOB_DROPS = create(key("mob_drops"));

    /**
     * {@code minecraft:mob_explosion_drop_decay}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MOB_EXPLOSION_DROP_DECAY = create(key("mob_explosion_drop_decay"));

    /**
     * {@code minecraft:mob_griefing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> MOB_GRIEFING = create(key("mob_griefing"));

    /**
     * {@code minecraft:natural_health_regeneration}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> NATURAL_HEALTH_REGENERATION = create(key("natural_health_regeneration"));

    /**
     * {@code minecraft:player_movement_check}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> PLAYER_MOVEMENT_CHECK = create(key("player_movement_check"));

    /**
     * {@code minecraft:players_nether_portal_creative_delay}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = create(key("players_nether_portal_creative_delay"));

    /**
     * {@code minecraft:players_nether_portal_default_delay}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = create(key("players_nether_portal_default_delay"));

    /**
     * {@code minecraft:players_sleeping_percentage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> PLAYERS_SLEEPING_PERCENTAGE = create(key("players_sleeping_percentage"));

    /**
     * {@code minecraft:projectiles_can_break_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> PROJECTILES_CAN_BREAK_BLOCKS = create(key("projectiles_can_break_blocks"));

    /**
     * {@code minecraft:pvp}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> PVP = create(key("pvp"));

    /**
     * {@code minecraft:raids}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> RAIDS = create(key("raids"));

    /**
     * {@code minecraft:random_tick_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> RANDOM_TICK_SPEED = create(key("random_tick_speed"));

    /**
     * {@code minecraft:reduced_debug_info}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> REDUCED_DEBUG_INFO = create(key("reduced_debug_info"));

    /**
     * {@code minecraft:respawn_radius}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> RESPAWN_RADIUS = create(key("respawn_radius"));

    /**
     * {@code minecraft:send_command_feedback}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SEND_COMMAND_FEEDBACK = create(key("send_command_feedback"));

    /**
     * {@code minecraft:show_advancement_messages}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SHOW_ADVANCEMENT_MESSAGES = create(key("show_advancement_messages"));

    /**
     * {@code minecraft:show_death_messages}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SHOW_DEATH_MESSAGES = create(key("show_death_messages"));

    /**
     * {@code minecraft:spawn_mobs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPAWN_MOBS = create(key("spawn_mobs"));

    /**
     * {@code minecraft:spawn_monsters}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPAWN_MONSTERS = create(key("spawn_monsters"));

    /**
     * {@code minecraft:spawn_patrols}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPAWN_PATROLS = create(key("spawn_patrols"));

    /**
     * {@code minecraft:spawn_phantoms}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPAWN_PHANTOMS = create(key("spawn_phantoms"));

    /**
     * {@code minecraft:spawn_wandering_traders}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPAWN_WANDERING_TRADERS = create(key("spawn_wandering_traders"));

    /**
     * {@code minecraft:spawn_wardens}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPAWN_WARDENS = create(key("spawn_wardens"));

    /**
     * {@code minecraft:spawner_blocks_work}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPAWNER_BLOCKS_WORK = create(key("spawner_blocks_work"));

    /**
     * {@code minecraft:spectators_generate_chunks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPECTATORS_GENERATE_CHUNKS = create(key("spectators_generate_chunks"));

    /**
     * {@code minecraft:spread_vines}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> SPREAD_VINES = create(key("spread_vines"));

    /**
     * {@code minecraft:tnt_explodes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> TNT_EXPLODES = create(key("tnt_explodes"));

    /**
     * {@code minecraft:tnt_explosion_drop_decay}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> TNT_EXPLOSION_DROP_DECAY = create(key("tnt_explosion_drop_decay"));

    /**
     * {@code minecraft:universal_anger}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> UNIVERSAL_ANGER = create(key("universal_anger"));

    /**
     * {@code minecraft:water_source_conversion}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<GameRule<?>> WATER_SOURCE_CONVERSION = create(key("water_source_conversion"));

    private GameRuleKeys() {
    }

    private static TypedKey<GameRule<?>> create(final Key key) {
        return TypedKey.create(RegistryKey.GAME_RULE, key);
    }
}
