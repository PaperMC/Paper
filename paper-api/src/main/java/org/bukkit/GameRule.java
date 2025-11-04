package org.bukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.world.flag.FeatureDependant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GameRules dictate certain behavior within Minecraft itself
 * <br>
 * For more information please visit the
 * <a href="https://minecraft.wiki/w/Commands/gamerule">Minecraft
 * Wiki</a>
 *
 * @param <T> type of rule (Boolean or Integer)
 */
@ApiStatus.NonExtendable
public abstract class GameRule<T> implements net.kyori.adventure.translation.Translatable, FeatureDependant, Keyed {

    // Start generate - GameRule
    public static final GameRule<Boolean> ADVANCE_TIME = getByName("advance_time");

    public static final GameRule<Boolean> ADVANCE_WEATHER = getByName("advance_weather");

    public static final GameRule<Boolean> ALLOW_ENTERING_NETHER_USING_PORTALS = getByName("allow_entering_nether_using_portals");

    public static final GameRule<Boolean> BLOCK_DROPS = getByName("block_drops");

    public static final GameRule<Boolean> BLOCK_EXPLOSION_DROP_DECAY = getByName("block_explosion_drop_decay");

    public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = getByName("command_block_output");

    public static final GameRule<Boolean> COMMAND_BLOCKS_WORK = getByName("command_blocks_work");

    public static final GameRule<Boolean> DROWNING_DAMAGE = getByName("drowning_damage");

    public static final GameRule<Boolean> ELYTRA_MOVEMENT_CHECK = getByName("elytra_movement_check");

    public static final GameRule<Boolean> ENDER_PEARLS_VANISH_ON_DEATH = getByName("ender_pearls_vanish_on_death");

    public static final GameRule<Boolean> ENTITY_DROPS = getByName("entity_drops");

    public static final GameRule<Boolean> FALL_DAMAGE = getByName("fall_damage");

    public static final GameRule<Boolean> FIRE_DAMAGE = getByName("fire_damage");

    public static final GameRule<Integer> FIRE_SPREAD_RADIUS_AROUND_PLAYER = getByName("fire_spread_radius_around_player");

    public static final GameRule<Boolean> FORGIVE_DEAD_PLAYERS = getByName("forgive_dead_players");

    public static final GameRule<Boolean> FREEZE_DAMAGE = getByName("freeze_damage");

    public static final GameRule<Boolean> GLOBAL_SOUND_EVENTS = getByName("global_sound_events");

    public static final GameRule<Boolean> IMMEDIATE_RESPAWN = getByName("immediate_respawn");

    public static final GameRule<Boolean> KEEP_INVENTORY = getByName("keep_inventory");

    public static final GameRule<Boolean> LAVA_SOURCE_CONVERSION = getByName("lava_source_conversion");

    public static final GameRule<Boolean> LIMITED_CRAFTING = getByName("limited_crafting");

    public static final GameRule<Boolean> LOCATOR_BAR = getByName("locator_bar");

    public static final GameRule<Boolean> LOG_ADMIN_COMMANDS = getByName("log_admin_commands");

    public static final GameRule<Integer> MAX_BLOCK_MODIFICATIONS = getByName("max_block_modifications");

    public static final GameRule<Integer> MAX_COMMAND_FORKS = getByName("max_command_forks");

    public static final GameRule<Integer> MAX_COMMAND_SEQUENCE_LENGTH = getByName("max_command_sequence_length");

    public static final GameRule<Integer> MAX_ENTITY_CRAMMING = getByName("max_entity_cramming");

    public static final GameRule<Integer> MAX_MINECART_SPEED = getByName("max_minecart_speed");

    public static final GameRule<Integer> MAX_SNOW_ACCUMULATION_HEIGHT = getByName("max_snow_accumulation_height");

    public static final GameRule<Boolean> MOB_DROPS = getByName("mob_drops");

    public static final GameRule<Boolean> MOB_EXPLOSION_DROP_DECAY = getByName("mob_explosion_drop_decay");

    public static final GameRule<Boolean> MOB_GRIEFING = getByName("mob_griefing");

    public static final GameRule<Boolean> NATURAL_HEALTH_REGENERATION = getByName("natural_health_regeneration");

    public static final GameRule<Boolean> PLAYER_MOVEMENT_CHECK = getByName("player_movement_check");

    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = getByName("players_nether_portal_creative_delay");

    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = getByName("players_nether_portal_default_delay");

    public static final GameRule<Integer> PLAYERS_SLEEPING_PERCENTAGE = getByName("players_sleeping_percentage");

    public static final GameRule<Boolean> PROJECTILES_CAN_BREAK_BLOCKS = getByName("projectiles_can_break_blocks");

    public static final GameRule<Boolean> PVP = getByName("pvp");

    public static final GameRule<Boolean> RAIDS = getByName("raids");

    public static final GameRule<Integer> RANDOM_TICK_SPEED = getByName("random_tick_speed");

    public static final GameRule<Boolean> REDUCED_DEBUG_INFO = getByName("reduced_debug_info");

    public static final GameRule<Integer> RESPAWN_RADIUS = getByName("respawn_radius");

    public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = getByName("send_command_feedback");

    public static final GameRule<Boolean> SHOW_ADVANCEMENT_MESSAGES = getByName("show_advancement_messages");

    public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = getByName("show_death_messages");

    public static final GameRule<Boolean> SPAWN_MOBS = getByName("spawn_mobs");

    public static final GameRule<Boolean> SPAWN_MONSTERS = getByName("spawn_monsters");

    public static final GameRule<Boolean> SPAWN_PATROLS = getByName("spawn_patrols");

    public static final GameRule<Boolean> SPAWN_PHANTOMS = getByName("spawn_phantoms");

    public static final GameRule<Boolean> SPAWN_WANDERING_TRADERS = getByName("spawn_wandering_traders");

    public static final GameRule<Boolean> SPAWN_WARDENS = getByName("spawn_wardens");

    public static final GameRule<Boolean> SPAWNER_BLOCKS_WORK = getByName("spawner_blocks_work");

    public static final GameRule<Boolean> SPECTATORS_GENERATE_CHUNKS = getByName("spectators_generate_chunks");

    public static final GameRule<Boolean> SPREAD_VINES = getByName("spread_vines");

    public static final GameRule<Boolean> TNT_EXPLODES = getByName("tnt_explodes");

    public static final GameRule<Boolean> TNT_EXPLOSION_DROP_DECAY = getByName("tnt_explosion_drop_decay");

    public static final GameRule<Boolean> UNIVERSAL_ANGER = getByName("universal_anger");

    public static final GameRule<Boolean> WATER_SOURCE_CONVERSION = getByName("water_source_conversion");
    // End generate - GameRule

    // Boolean rules
    /**
     * Toggles the announcing of advancements.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> ANNOUNCE_ADVANCEMENTS = InternalAPIBridge.get().legacyGameRuleBridge(SHOW_ADVANCEMENT_MESSAGES, null, null, Boolean.class);

    /**
     * Whether the server should skip checking player speed.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DISABLE_PLAYER_MOVEMENT_CHECK = InternalAPIBridge.get().legacyGameRuleBridge(PLAYER_MOVEMENT_CHECK, inverseBool(),  inverseBool(), Boolean.class);

    /**
     * Whether the server should skip checking player speed when the player is
     * wearing elytra.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DISABLE_ELYTRA_MOVEMENT_CHECK = InternalAPIBridge.get().legacyGameRuleBridge(ELYTRA_MOVEMENT_CHECK, inverseBool(),  inverseBool(), Boolean.class);

    /**
     * Whether time progresses from the current moment.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_DAYLIGHT_CYCLE = InternalAPIBridge.get().legacyGameRuleBridge(ADVANCE_TIME, null, null, Boolean.class);

    /**
     * Whether entities that are not mobs should have drops.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_ENTITY_DROPS = InternalAPIBridge.get().legacyGameRuleBridge(ENTITY_DROPS, null, null, Boolean.class);

    /**
     * Whether fire should spread and naturally extinguish.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_FIRE_TICK = InternalAPIBridge.get().legacyGameRuleBridge(FIRE_SPREAD_RADIUS_AROUND_PLAYER, (value) -> value ? 128 : 0, (value) -> value != 0, Boolean.class);

    /**
     * Whether players should only be able to craft recipes they've unlocked
     * first.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_LIMITED_CRAFTING = InternalAPIBridge.get().legacyGameRuleBridge(LIMITED_CRAFTING, null, null, Boolean.class);

    /**
     * Whether mobs should drop items.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_MOB_LOOT = InternalAPIBridge.get().legacyGameRuleBridge(MOB_DROPS, null, null, Boolean.class);

    /**
     * Whether mobs should naturally spawn.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_MOB_SPAWNING = InternalAPIBridge.get().legacyGameRuleBridge(SPAWN_MOBS, null, null, Boolean.class);

    /**
     * Whether blocks should have drops.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_TILE_DROPS = InternalAPIBridge.get().legacyGameRuleBridge(BLOCK_DROPS, null, null, Boolean.class);

    /**
     * Whether the weather will change from the current moment.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_WEATHER_CYCLE = InternalAPIBridge.get().legacyGameRuleBridge(ADVANCE_WEATHER, null, null, Boolean.class);

    /**
     * Whether players can regenerate health naturally through their hunger bar.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> NATURAL_REGENERATION = InternalAPIBridge.get().legacyGameRuleBridge(NATURAL_HEALTH_REGENERATION, null, null, Boolean.class);

    /**
     * Whether pillager raids are enabled or not.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DISABLE_RAIDS = InternalAPIBridge.get().legacyGameRuleBridge(RAIDS, inverseBool(), inverseBool(), Boolean.class);

    /**
     * Whether phantoms will appear without sleeping or not.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_INSOMNIA = InternalAPIBridge.get().legacyGameRuleBridge(SPAWN_PHANTOMS, null, null, Boolean.class);

    /**
     * Whether clients will respawn immediately after death or not.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_IMMEDIATE_RESPAWN = InternalAPIBridge.get().legacyGameRuleBridge(IMMEDIATE_RESPAWN, null, null, Boolean.class);

    /**
     * Whether patrols should naturally spawn.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_PATROL_SPAWNING = InternalAPIBridge.get().legacyGameRuleBridge(SPAWN_PATROLS, null, null, Boolean.class);

    /**
     * Whether traders should naturally spawn.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_TRADER_SPAWNING = InternalAPIBridge.get().legacyGameRuleBridge(SPAWN_WANDERING_TRADERS, null, null, Boolean.class);

    /**
     * Whether wardens should naturally spawn.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_WARDEN_SPAWNING = InternalAPIBridge.get().legacyGameRuleBridge(SPAWN_WARDENS, null, null, Boolean.class);
    /**
     * Whether vines will spread.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_VINES_SPREAD = InternalAPIBridge.get().legacyGameRuleBridge(SPREAD_VINES, null, null, Boolean.class);
    /**
     * Whether fire will still propagate far away from players (8 chunks).
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> ALLOW_FIRE_TICKS_AWAY_FROM_PLAYER = InternalAPIBridge.get().legacyGameRuleBridge(FIRE_SPREAD_RADIUS_AROUND_PLAYER, (value) -> value ? 128 : 0, (value) -> value != 0, Boolean.class);

    /**
     * The number of blocks outward from the world spawn coordinates that a
     * player will spawn in when first joining a server or when dying without a
     * spawnpoint.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> SPAWN_RADIUS = InternalAPIBridge.get().legacyGameRuleBridge(RESPAWN_RADIUS, null, null, Integer.class);

    /**
     * Determines the number at which the chain of command blocks act as a
     * "chain."
     * <br>
     * This is the maximum amount of command blocks that can be activated in a
     * single tick from a single chain.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> MAX_COMMAND_CHAIN_LENGTH = InternalAPIBridge.get().legacyGameRuleBridge(MAX_COMMAND_SEQUENCE_LENGTH, null, null, Integer.class);

    /**
     * Determines the number of different commands/functions which execute
     * commands can fork into.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> MAX_COMMAND_FORK_COUNT = InternalAPIBridge.get().legacyGameRuleBridge(MAX_COMMAND_FORKS, null, null, Integer.class);

    /**
     * Determines the maximum number of blocks which a command can modify.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> COMMAND_MODIFICATION_BLOCK_LIMIT = InternalAPIBridge.get().legacyGameRuleBridge(MAX_BLOCK_MODIFICATIONS, null, null, Integer.class);

    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> SNOW_ACCUMULATION_HEIGHT = InternalAPIBridge.get().legacyGameRuleBridge(MAX_SNOW_ACCUMULATION_HEIGHT, null, null, Integer.class);

    /**
     * The maximum speed of minecarts (when the new movement algorithm is
     * enabled).
     */
    @MinecraftExperimental(MinecraftExperimental.Requires.MINECART_IMPROVEMENTS) // Paper - add missing annotation
    @org.jetbrains.annotations.ApiStatus.Experimental // Paper - add missing annotation
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> MINECART_MAX_SPEED = InternalAPIBridge.get().legacyGameRuleBridge(MAX_MINECART_SPEED, null, null, Integer.class);

    /**
     * Whether command blocks are enabled.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> COMMAND_BLOCKS_ENABLED = InternalAPIBridge.get().legacyGameRuleBridge(COMMAND_BLOCKS_WORK, null, null, Boolean.class);

    /**
     * Whether spawner blocks are enabled.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> SPAWNER_BLOCKS_ENABLED = InternalAPIBridge.get().legacyGameRuleBridge(SPAWNER_BLOCKS_WORK, null, null, Boolean.class);

    private static UnaryOperator<Boolean> inverseBool() {
        return operand -> !operand;
    }

    /**
     * Get the name of this GameRule.
     *
     * @return the name of this GameRule
     */
    @NotNull
    public abstract String getName();

    /**
     * Get the type of this rule.
     *
     * @return the rule type; Integer or Boolean
     */
    @NotNull
    public abstract Class<T> getType();

    /**
     * Get a {@link GameRule} by its name.
     *
     * @param rule the name of the GameRule
     * @return the {@link GameRule} or null if no GameRule matches the given
     * name
     */
    @Nullable
    public static <T> GameRule<T> getByName(@NotNull String rule) {
        Preconditions.checkNotNull(rule, "Rule cannot be null");
        try {
            return (GameRule<T>) RegistryAccess.registryAccess().getRegistry(RegistryKey.GAME_RULE).getOrThrow(NamespacedKey.fromString(rule));
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Get an immutable collection of {@link GameRule}s.
     *
     * @return an immutable collection containing all registered GameRules.
     */
    @NotNull
    public static GameRule<?>[] values() {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.GAME_RULE)
                .stream()
                .toArray(GameRule[]::new);
    }

    @Override
    public abstract @NotNull String translationKey();

}
