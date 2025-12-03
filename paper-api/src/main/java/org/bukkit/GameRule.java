package org.bukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.world.flag.FeatureDependant;
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
 * @see GameRules
 */
@ApiStatus.NonExtendable
public abstract class GameRule<T> implements net.kyori.adventure.translation.Translatable, FeatureDependant, Keyed {

    // Boolean rules
    /**
     * Toggles the announcing of advancements.
     *
     * @deprecated renamed to {@link GameRules#SHOW_ADVANCEMENT_MESSAGES}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> ANNOUNCE_ADVANCEMENTS = GameRules.SHOW_ADVANCEMENT_MESSAGES;
    /**
     * Whether command blocks should notify admins when they perform commands.
     *
     * @deprecated renamed to {@link GameRules#COMMAND_BLOCK_OUTPUT}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = GameRules.COMMAND_BLOCK_OUTPUT;
    /**
     * Whether the server should skip checking player speed.
     *
     * @deprecated renamed to {@link GameRules#PLAYER_MOVEMENT_CHECK} (inverted)
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DISABLE_PLAYER_MOVEMENT_CHECK = InternalAPIBridge.get().legacyGameRuleBridge(GameRules.PLAYER_MOVEMENT_CHECK, inverseBool(), inverseBool(), Boolean.class);
    /**
     * Whether the server should skip checking player speed when the player is
     * wearing elytra.
     *
     * @deprecated renamed to {@link GameRules#ELYTRA_MOVEMENT_CHECK} (inverted)
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DISABLE_ELYTRA_MOVEMENT_CHECK = InternalAPIBridge.get().legacyGameRuleBridge(GameRules.ELYTRA_MOVEMENT_CHECK, inverseBool(), inverseBool(), Boolean.class);
    /**
     * Whether time progresses from the current moment.
     *
     * @deprecated renamed to {@link GameRules#ADVANCE_TIME}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_DAYLIGHT_CYCLE = GameRules.ADVANCE_TIME;
    /**
     * Whether entities that are not mobs should have drops.
     *
     * @deprecated renamed to {@link GameRules#ENTITY_DROPS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_ENTITY_DROPS = GameRules.ENTITY_DROPS;
    /**
     * Whether fire should spread and naturally extinguish.
     *
     * @deprecated use {@link GameRules#FIRE_SPREAD_RADIUS_AROUND_PLAYER}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_FIRE_TICK = InternalAPIBridge.get().legacyGameRuleBridge(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER, (value) -> value ? 128 : 0, (value) -> value != 0, Boolean.class);
    /**
     * Whether players should only be able to craft recipes they've unlocked
     * first.
     *
     * @deprecated renamed to {@link GameRules#LIMITED_CRAFTING}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_LIMITED_CRAFTING = GameRules.LIMITED_CRAFTING;
    /**
     * Whether projectiles can break blocks.
     *
     * @deprecated renamed to {@link GameRules#PROJECTILES_CAN_BREAK_BLOCKS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> PROJECTILES_CAN_BREAK_BLOCKS = GameRules.PROJECTILES_CAN_BREAK_BLOCKS;
    /**
     * Whether mobs should drop items.
     *
     * @deprecated renamed to {@link GameRules#MOB_DROPS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_MOB_LOOT = GameRules.MOB_DROPS;
    /**
     * Whether mobs should naturally spawn.
     *
     * @deprecated renamed to {@link GameRules#SPAWN_MOBS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_MOB_SPAWNING = GameRules.SPAWN_MOBS;
    /**
     * Whether blocks should have drops.
     *
     * @deprecated renamed to {@link GameRules#BLOCK_DROPS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_TILE_DROPS = GameRules.BLOCK_DROPS;
    /**
     * Whether the weather will change from the current moment.
     *
     * @deprecated renamed to {@link GameRules#ADVANCE_WEATHER}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_WEATHER_CYCLE = GameRules.ADVANCE_WEATHER;
    /**
     * Whether the player should keep items in their inventory after death.
     *
     * @deprecated renamed to {@link GameRules#KEEP_INVENTORY}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> KEEP_INVENTORY = GameRules.KEEP_INVENTORY;
    /**
     * Whether to log admin commands to server log.
     *
     * @deprecated renamed to {@link GameRules#LOG_ADMIN_COMMANDS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> LOG_ADMIN_COMMANDS = GameRules.LOG_ADMIN_COMMANDS;
    /**
     * Whether mobs can pick up items or change blocks.
     *
     * @deprecated renamed to {@link GameRules#MOB_GRIEFING}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> MOB_GRIEFING = GameRules.MOB_GRIEFING;
    /**
     * Whether players can regenerate health naturally through their hunger bar.
     *
     * @deprecated renamed to {@link GameRules#NATURAL_HEALTH_REGENERATION}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> NATURAL_REGENERATION = GameRules.NATURAL_HEALTH_REGENERATION;
    /**
     * Whether the debug screen shows all or reduced information.
     *
     * @deprecated renamed to {@link GameRules#REDUCED_DEBUG_INFO}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> REDUCED_DEBUG_INFO = GameRules.REDUCED_DEBUG_INFO;
    /**
     * Whether the feedback from commands executed by a player should show up in
     * chat. Also affects the default behavior of whether command blocks store
     * their output text.
     *
     * @deprecated renamed to {@link GameRules#SEND_COMMAND_FEEDBACK}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = GameRules.SEND_COMMAND_FEEDBACK;
    /**
     * Whether a message appears in chat when a player dies.
     *
     * @deprecated renamed to {@link GameRules#SHOW_DEATH_MESSAGES}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = GameRules.SHOW_DEATH_MESSAGES;
    /**
     * Whether players in spectator mode can generate chunks.
     *
     * @deprecated renamed to {@link GameRules#SPECTATORS_GENERATE_CHUNKS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> SPECTATORS_GENERATE_CHUNKS = GameRules.SPECTATORS_GENERATE_CHUNKS;
    /**
     * Whether pillager raids are enabled or not.
     *
     * @deprecated renamed to {@link GameRules#RAIDS} (inverted)
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DISABLE_RAIDS = InternalAPIBridge.get().legacyGameRuleBridge(GameRules.RAIDS, inverseBool(), inverseBool(), Boolean.class);
    /**
     * Whether phantoms will appear without sleeping or not.
     *
     * @deprecated renamed to {@link GameRules#SPAWN_PHANTOMS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_INSOMNIA = GameRules.SPAWN_PHANTOMS;
    /**
     * Whether clients will respawn immediately after death or not.
     *
     * @deprecated renamed to {@link GameRules#IMMEDIATE_RESPAWN}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_IMMEDIATE_RESPAWN = GameRules.IMMEDIATE_RESPAWN;
    /**
     * Whether drowning damage is enabled or not.
     *
     * @deprecated renamed to {@link GameRules#DROWNING_DAMAGE}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DROWNING_DAMAGE = GameRules.DROWNING_DAMAGE;
    /**
     * Whether fall damage is enabled or not.
     *
     * @deprecated renamed to {@link GameRules#FALL_DAMAGE}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> FALL_DAMAGE = GameRules.FALL_DAMAGE;
    /**
     * Whether fire damage is enabled or not.
     *
     * @deprecated renamed to {@link GameRules#FIRE_DAMAGE}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> FIRE_DAMAGE = GameRules.FIRE_DAMAGE;
    /**
     * Whether freeze damage is enabled or not.
     *
     * @deprecated renamed to {@link GameRules#FREEZE_DAMAGE}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> FREEZE_DAMAGE = GameRules.FREEZE_DAMAGE;
    /**
     * Whether patrols should naturally spawn.
     *
     * @deprecated renamed to {@link GameRules#SPAWN_PATROLS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_PATROL_SPAWNING = GameRules.SPAWN_PATROLS;
    /**
     * Whether traders should naturally spawn.
     *
     * @deprecated renamed to {@link GameRules#SPAWN_WANDERING_TRADERS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_TRADER_SPAWNING = GameRules.SPAWN_WANDERING_TRADERS;
    /**
     * Whether wardens should naturally spawn.
     *
     * @deprecated renamed to {@link GameRules#SPAWN_WARDENS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_WARDEN_SPAWNING = GameRules.SPAWN_WARDENS;
    /**
     * Whether mobs should cease being angry at a player once they die.
     *
     * @deprecated renamed to {@link GameRules#FORGIVE_DEAD_PLAYERS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> FORGIVE_DEAD_PLAYERS = GameRules.FORGIVE_DEAD_PLAYERS;
    /**
     * Whether mobs will target all player entities once angered.
     *
     * @deprecated renamed to {@link GameRules#UNIVERSAL_ANGER}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> UNIVERSAL_ANGER = GameRules.UNIVERSAL_ANGER;
    /**
     * Whether block explosions will destroy dropped items.
     *
     * @deprecated renamed to {@link GameRules#BLOCK_EXPLOSION_DROP_DECAY}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> BLOCK_EXPLOSION_DROP_DECAY = GameRules.BLOCK_EXPLOSION_DROP_DECAY;
    /**
     * * Whether mob explosions will destroy dropped items.
     *
     * @deprecated renamed to {@link GameRules#MOB_EXPLOSION_DROP_DECAY}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> MOB_EXPLOSION_DROP_DECAY = GameRules.MOB_EXPLOSION_DROP_DECAY;
    /**
     * Whether tnt explosions will destroy dropped items.
     *
     * @deprecated renamed to {@link GameRules#TNT_EXPLOSION_DROP_DECAY}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> TNT_EXPLOSION_DROP_DECAY = GameRules.TNT_EXPLOSION_DROP_DECAY;
    /**
     * Whether water blocks can convert into water source blocks.
     *
     * @deprecated renamed to {@link GameRules#WATER_SOURCE_CONVERSION}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> WATER_SOURCE_CONVERSION = GameRules.WATER_SOURCE_CONVERSION;
    /**
     * Whether lava blocks can convert into lava source blocks.
     *
     * @deprecated renamed to {@link GameRules#LAVA_SOURCE_CONVERSION}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> LAVA_SOURCE_CONVERSION = GameRules.LAVA_SOURCE_CONVERSION;
    /**
     * Whether global level events such as ender dragon, wither, and completed
     * end portal effects will propagate across the entire server.
     *
     * @deprecated renamed to {@link GameRules#GLOBAL_SOUND_EVENTS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> GLOBAL_SOUND_EVENTS = GameRules.GLOBAL_SOUND_EVENTS;
    /**
     * Whether vines will spread.
     *
     * @deprecated renamed to {@link GameRules#SPREAD_VINES}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> DO_VINES_SPREAD = GameRules.SPREAD_VINES;
    /**
     * Whether ender pearls will vanish on player death.
     *
     * @deprecated renamed to {@link GameRules#ENDER_PEARLS_VANISH_ON_DEATH}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> ENDER_PEARLS_VANISH_ON_DEATH = GameRules.ENDER_PEARLS_VANISH_ON_DEATH;
    /**
     * Whether fire will still propagate far away from players (8 chunks).
     *
     * @deprecated use {@link GameRules#FIRE_SPREAD_RADIUS_AROUND_PLAYER}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> ALLOW_FIRE_TICKS_AWAY_FROM_PLAYER = InternalAPIBridge.get().legacyGameRuleBridge(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER, (value) -> value ? -1 : 128, (value) -> value == -1, Boolean.class);
    /**
     * Whether primed tnt explodes.
     *
     * @deprecated renamed to {@link GameRules#TNT_EXPLODES}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> TNT_EXPLODES = GameRules.TNT_EXPLODES;
    /**
     * Configures if the world uses the locator bar.
     *
     * @deprecated renamed to {@link GameRules#LOCATOR_BAR}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> LOCATOR_BAR = GameRules.LOCATOR_BAR;
    /**
     * Whether player versus player combat is allowed.
     *
     * @deprecated renamed to {@link GameRules#PVP}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> PVP = GameRules.PVP;
    /**
     * Whether monsters should naturally spawn.
     *
     * @deprecated renamed to {@link GameRules#SPAWN_MONSTERS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> SPAWN_MONSTERS = GameRules.SPAWN_MONSTERS;
    /**
     * Whether players can enter the Nether using portals.
     *
     * @deprecated renamed to {@link GameRules#ALLOW_ENTERING_NETHER_USING_PORTALS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> ALLOW_ENTERING_NETHER_USING_PORTALS = GameRules.ALLOW_ENTERING_NETHER_USING_PORTALS;
    /**
     * Whether command blocks are enabled.
     *
     * @deprecated renamed to {@link GameRules#COMMAND_BLOCKS_WORK}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> COMMAND_BLOCKS_ENABLED = GameRules.COMMAND_BLOCKS_WORK;
    /**
     * Whether spawner blocks are enabled.
     *
     * @deprecated renamed to {@link GameRules#SPAWNER_BLOCKS_WORK}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Boolean> SPAWNER_BLOCKS_ENABLED = GameRules.SPAWNER_BLOCKS_WORK;

    // Numerical rules
    /**
     * How often a random block tick occurs (such as plant growth, leaf decay,
     * etc.) per chunk section per game tick. 0 will disable random ticks,
     * higher numbers will increase random ticks.
     *
     * @deprecated renamed to {@link GameRules#RANDOM_TICK_SPEED}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> RANDOM_TICK_SPEED = GameRules.RANDOM_TICK_SPEED;
    /**
     * The number of blocks outward from the world spawn coordinates that a
     * player will spawn in when first joining a server or when dying without a
     * spawnpoint.
     *
     * @deprecated renamed to {@link GameRules#RESPAWN_RADIUS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> SPAWN_RADIUS = GameRules.RESPAWN_RADIUS;
    /**
     * The maximum number of other pushable entities a mob or player can push,
     * before taking suffocation damage.
     * <br>
     * Setting to 0 disables this rule.
     *
     * @deprecated renamed to {@link GameRules#MAX_ENTITY_CRAMMING}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> MAX_ENTITY_CRAMMING = GameRules.MAX_ENTITY_CRAMMING;
    /**
     * Determines the number at which the chain of command blocks act as a
     * "chain."
     * <br>
     * This is the maximum amount of command blocks that can be activated in a
     * single tick from a single chain.
     *
     * @deprecated renamed to {@link GameRules#MAX_COMMAND_SEQUENCE_LENGTH}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> MAX_COMMAND_CHAIN_LENGTH = GameRules.MAX_COMMAND_SEQUENCE_LENGTH;
    /**
     * Determines the number of different commands/functions which execute
     * commands can fork into.
     *
     * @deprecated renamed to {@link GameRules#MAX_COMMAND_FORKS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> MAX_COMMAND_FORK_COUNT = GameRules.MAX_COMMAND_FORKS;
    /**
     * Determines the maximum number of blocks which a command can modify.
     *
     * @deprecated renamed to {@link GameRules#MAX_BLOCK_MODIFICATIONS}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> COMMAND_MODIFICATION_BLOCK_LIMIT = GameRules.MAX_BLOCK_MODIFICATIONS;
    /**
     * The percentage of online players which must be sleeping for the night to
     * advance.
     *
     * @deprecated renamed to {@link GameRules#PLAYERS_SLEEPING_PERCENTAGE}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> PLAYERS_SLEEPING_PERCENTAGE = GameRules.PLAYERS_SLEEPING_PERCENTAGE;
    /**
     * @deprecated renamed to {@link GameRules#MAX_SNOW_ACCUMULATION_HEIGHT}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> SNOW_ACCUMULATION_HEIGHT = GameRules.MAX_SNOW_ACCUMULATION_HEIGHT;
    /**
     * The amount of time a player must stand in a nether portal before the
     * portal activates.
     *
     * @deprecated renamed to {@link GameRules#PLAYERS_NETHER_PORTAL_DEFAULT_DELAY}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY;
    /**
     * The amount of time a player in creative mode must stand in a nether
     * portal before the portal activates.
     *
     * @deprecated renamed to {@link GameRules#PLAYERS_NETHER_PORTAL_CREATIVE_DELAY}
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY;
    /**
     * The maximum speed of minecarts (when the new movement algorithm is
     * enabled).
     *
     * @deprecated renamed to {@link GameRules#MAX_MINECART_SPEED}
     */
    @MinecraftExperimental(MinecraftExperimental.Requires.MINECART_IMPROVEMENTS)
    @ApiStatus.Experimental
    @Deprecated(forRemoval = true, since = "1.21.11")
    public static final GameRule<Integer> MINECART_MAX_SPEED = GameRules.MAX_MINECART_SPEED;

    private static UnaryOperator<Boolean> inverseBool() {
        return operand -> !operand;
    }

    /**
     * Get the name of this GameRule.
     *
     * @return the name of this GameRule
     * @deprecated Game rule is now a registry, use {@link #getKey()} instead
     */
    @NotNull
    @Deprecated(since = "1.21.11", forRemoval = true)
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
     * @deprecated Game rule is now a registry, fetch element from it
     */
    @Nullable
    @Deprecated(since = "1.21.11", forRemoval = true)
    public static <T> GameRule<T> getByName(@NotNull String rule) {
        Preconditions.checkArgument(rule != null, "Rule cannot be null");
        NamespacedKey key = NamespacedKey.fromString(rule);
        if (key == null) {
            return null;
        }

        return (GameRule<T>) Registry.GAME_RULE.get(key);
    }

    /**
     * Get an immutable collection of {@link GameRule}s.
     *
     * @return an immutable collection containing all registered GameRules.
     * @deprecated Game rule is now a registry, fetch elements from it
     */
    @NotNull
    @Deprecated(since = "1.21.11", forRemoval = true)
    public static GameRule<?>[] values() {
        return Registry.GAME_RULE.stream().toArray(GameRule[]::new);
    }

    @Override
    public abstract @NotNull String translationKey();

}
