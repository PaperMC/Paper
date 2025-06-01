package org.bukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.world.flag.FeatureDependant;
import java.util.HashMap;
import java.util.Map;
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
public final class GameRule<T> implements net.kyori.adventure.translation.Translatable, FeatureDependant {

    private static Map<String, GameRule<?>> gameRules = new HashMap<>();
    // Boolean rules
    /**
     * Toggles the announcing of advancements.
     */
    public static final GameRule<Boolean> ANNOUNCE_ADVANCEMENTS = new GameRule<>("announceAdvancements", Boolean.class);

    /**
     * Whether command blocks should notify admins when they perform commands.
     */
    public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = new GameRule<>("commandBlockOutput", Boolean.class);

    /**
     * Whether the server should skip checking player speed.
     */
    public static final GameRule<Boolean> DISABLE_PLAYER_MOVEMENT_CHECK = new GameRule<>("disablePlayerMovementCheck", Boolean.class);

    /**
     * Whether the server should skip checking player speed when the player is
     * wearing elytra.
     */
    public static final GameRule<Boolean> DISABLE_ELYTRA_MOVEMENT_CHECK = new GameRule<>("disableElytraMovementCheck", Boolean.class);

    /**
     * Whether time progresses from the current moment.
     */
    public static final GameRule<Boolean> DO_DAYLIGHT_CYCLE = new GameRule<>("doDaylightCycle", Boolean.class);

    /**
     * Whether entities that are not mobs should have drops.
     */
    public static final GameRule<Boolean> DO_ENTITY_DROPS = new GameRule<>("doEntityDrops", Boolean.class);

    /**
     * Whether fire should spread and naturally extinguish.
     */
    public static final GameRule<Boolean> DO_FIRE_TICK = new GameRule<>("doFireTick", Boolean.class);

    /**
     * Whether players should only be able to craft recipes they've unlocked
     * first.
     */
    public static final GameRule<Boolean> DO_LIMITED_CRAFTING = new GameRule<>("doLimitedCrafting", Boolean.class);

    /**
     * Whether mobs should drop items.
     */
    public static final GameRule<Boolean> DO_MOB_LOOT = new GameRule<>("doMobLoot", Boolean.class);

    /**
     * Whether projectiles can break blocks.
     */
    public static final GameRule<Boolean> PROJECTILES_CAN_BREAK_BLOCKS = new GameRule<>("projectilesCanBreakBlocks", Boolean.class);

    /**
     * Whether mobs should naturally spawn.
     */
    public static final GameRule<Boolean> DO_MOB_SPAWNING = new GameRule<>("doMobSpawning", Boolean.class);

    /**
     * Whether blocks should have drops.
     */
    public static final GameRule<Boolean> DO_TILE_DROPS = new GameRule<>("doTileDrops", Boolean.class);

    /**
     * Whether the weather will change from the current moment.
     */
    public static final GameRule<Boolean> DO_WEATHER_CYCLE = new GameRule<>("doWeatherCycle", Boolean.class);

    /**
     * Whether the player should keep items in their inventory after death.
     */
    public static final GameRule<Boolean> KEEP_INVENTORY = new GameRule<>("keepInventory", Boolean.class);

    /**
     * Whether to log admin commands to server log.
     */
    public static final GameRule<Boolean> LOG_ADMIN_COMMANDS = new GameRule<>("logAdminCommands", Boolean.class);

    /**
     * Whether mobs can pick up items or change blocks.
     */
    public static final GameRule<Boolean> MOB_GRIEFING = new GameRule<>("mobGriefing", Boolean.class);

    /**
     * Whether players can regenerate health naturally through their hunger bar.
     */
    public static final GameRule<Boolean> NATURAL_REGENERATION = new GameRule<>("naturalRegeneration", Boolean.class);

    /**
     * Whether the debug screen shows all or reduced information.
     */
    public static final GameRule<Boolean> REDUCED_DEBUG_INFO = new GameRule<>("reducedDebugInfo", Boolean.class);

    /**
     * Whether the feedback from commands executed by a player should show up in
     * chat. Also affects the default behavior of whether command blocks store
     * their output text.
     */
    public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = new GameRule<>("sendCommandFeedback", Boolean.class);

    /**
     * Whether a message appears in chat when a player dies.
     */
    public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = new GameRule<>("showDeathMessages", Boolean.class);

    /**
     * Whether players in spectator mode can generate chunks.
     */
    public static final GameRule<Boolean> SPECTATORS_GENERATE_CHUNKS = new GameRule<>("spectatorsGenerateChunks", Boolean.class);

    /**
     * Whether pillager raids are enabled or not.
     */
    public static final GameRule<Boolean> DISABLE_RAIDS = new GameRule<>("disableRaids", Boolean.class);

    /**
     * Whether phantoms will appear without sleeping or not.
     */
    public static final GameRule<Boolean> DO_INSOMNIA = new GameRule<>("doInsomnia", Boolean.class);

    /**
     * Whether clients will respawn immediately after death or not.
     */
    public static final GameRule<Boolean> DO_IMMEDIATE_RESPAWN = new GameRule<>("doImmediateRespawn", Boolean.class);

    /**
     * Whether drowning damage is enabled or not.
     */
    public static final GameRule<Boolean> DROWNING_DAMAGE = new GameRule<>("drowningDamage", Boolean.class);

    /**
     * Whether fall damage is enabled or not.
     */
    public static final GameRule<Boolean> FALL_DAMAGE = new GameRule<>("fallDamage", Boolean.class);

    /**
     * Whether fire damage is enabled or not.
     */
    public static final GameRule<Boolean> FIRE_DAMAGE = new GameRule<>("fireDamage", Boolean.class);

    /**
     * Whether freeze damage is enabled or not.
     */
    public static final GameRule<Boolean> FREEZE_DAMAGE = new GameRule<>("freezeDamage", Boolean.class);

    /**
     * Whether patrols should naturally spawn.
     */
    public static final GameRule<Boolean> DO_PATROL_SPAWNING = new GameRule<>("doPatrolSpawning", Boolean.class);

    /**
     * Whether traders should naturally spawn.
     */
    public static final GameRule<Boolean> DO_TRADER_SPAWNING = new GameRule<>("doTraderSpawning", Boolean.class);

    /**
     * Whether wardens should naturally spawn.
     */
    public static final GameRule<Boolean> DO_WARDEN_SPAWNING = new GameRule<>("doWardenSpawning", Boolean.class);

    /**
     * Whether mobs should cease being angry at a player once they die.
     */
    public static final GameRule<Boolean> FORGIVE_DEAD_PLAYERS = new GameRule<>("forgiveDeadPlayers", Boolean.class);

    /**
     * Whether mobs will target all player entities once angered.
     */
    public static final GameRule<Boolean> UNIVERSAL_ANGER = new GameRule<>("universalAnger", Boolean.class);
    /**
     * Whether block explosions will destroy dropped items.
     */
    public static final GameRule<Boolean> BLOCK_EXPLOSION_DROP_DECAY = new GameRule<>("blockExplosionDropDecay", Boolean.class);
    /**
     * * Whether mob explosions will destroy dropped items.
     */
    public static final GameRule<Boolean> MOB_EXPLOSION_DROP_DECAY = new GameRule<>("mobExplosionDropDecay", Boolean.class);
    /**
     * Whether tnt explosions will destroy dropped items.
     */
    public static final GameRule<Boolean> TNT_EXPLOSION_DROP_DECAY = new GameRule<>("tntExplosionDropDecay", Boolean.class);
    /**
     * Whether water blocks can convert into water source blocks.
     */
    public static final GameRule<Boolean> WATER_SOURCE_CONVERSION = new GameRule<>("waterSourceConversion", Boolean.class);
    /**
     * Whether lava blocks can convert into lava source blocks.
     */
    public static final GameRule<Boolean> LAVA_SOURCE_CONVERSION = new GameRule<>("lavaSourceConversion", Boolean.class);
    /**
     * Whether global level events such as ender dragon, wither, and completed
     * end portal effects will propagate across the entire server.
     */
    public static final GameRule<Boolean> GLOBAL_SOUND_EVENTS = new GameRule<>("globalSoundEvents", Boolean.class);
    /**
     * Whether vines will spread.
     */
    public static final GameRule<Boolean> DO_VINES_SPREAD = new GameRule<>("doVinesSpread", Boolean.class);
    /**
     * Whether ender pearls will vanish on player death.
     */
    public static final GameRule<Boolean> ENDER_PEARLS_VANISH_ON_DEATH = new GameRule<>("enderPearlsVanishOnDeath", Boolean.class);
    /**
     * Whether fire will still propagate far away from players (8 chunks).
     */
    public static final GameRule<Boolean> ALLOW_FIRE_TICKS_AWAY_FROM_PLAYER = new GameRule<>("allowFireTicksAwayFromPlayer", Boolean.class);
    /**
     * Whether primed tnt explodes.
     */
    public static final GameRule<Boolean> TNT_EXPLODES = new GameRule<>("tntExplodes", Boolean.class);

    // Numerical rules
    /**
     * How often a random block tick occurs (such as plant growth, leaf decay,
     * etc.) per chunk section per game tick. 0 will disable random ticks,
     * higher numbers will increase random ticks.
     */
    public static final GameRule<Integer> RANDOM_TICK_SPEED = new GameRule<>("randomTickSpeed", Integer.class);

    /**
     * The number of blocks outward from the world spawn coordinates that a
     * player will spawn in when first joining a server or when dying without a
     * spawnpoint.
     */
    public static final GameRule<Integer> SPAWN_RADIUS = new GameRule<>("spawnRadius", Integer.class);

    /**
     * The maximum number of other pushable entities a mob or player can push,
     * before taking suffocation damage.
     * <br>
     * Setting to 0 disables this rule.
     */
    public static final GameRule<Integer> MAX_ENTITY_CRAMMING = new GameRule<>("maxEntityCramming", Integer.class);

    /**
     * Determines the number at which the chain of command blocks act as a
     * "chain."
     * <br>
     * This is the maximum amount of command blocks that can be activated in a
     * single tick from a single chain.
     */
    public static final GameRule<Integer> MAX_COMMAND_CHAIN_LENGTH = new GameRule<>("maxCommandChainLength", Integer.class);

    /**
     * Determines the number of different commands/functions which execute
     * commands can fork into.
     */
    public static final GameRule<Integer> MAX_COMMAND_FORK_COUNT = new GameRule<>("maxCommandForkCount", Integer.class);

    /**
     * Determines the maximum number of blocks which a command can modify.
     */
    public static final GameRule<Integer> COMMAND_MODIFICATION_BLOCK_LIMIT = new GameRule<>("commandModificationBlockLimit", Integer.class);

    /**
     * The percentage of online players which must be sleeping for the night to
     * advance.
     */
    public static final GameRule<Integer> PLAYERS_SLEEPING_PERCENTAGE = new GameRule<>("playersSleepingPercentage", Integer.class);
    public static final GameRule<Integer> SNOW_ACCUMULATION_HEIGHT = new GameRule<>("snowAccumulationHeight", Integer.class);

    /**
     * The amount of time a player must stand in a nether portal before the
     * portal activates.
     */
    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = new GameRule<>("playersNetherPortalDefaultDelay", Integer.class);

    /**
     * The amount of time a player in creative mode must stand in a nether
     * portal before the portal activates.
     */
    public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = new GameRule<>("playersNetherPortalCreativeDelay", Integer.class);

    /**
     * The maximum speed of minecarts (when the new movement algorithm is
     * enabled).
     */
    @MinecraftExperimental(MinecraftExperimental.Requires.MINECART_IMPROVEMENTS) // Paper - add missing annotation
    @org.jetbrains.annotations.ApiStatus.Experimental // Paper - add missing annotation
    public static final GameRule<Integer> MINECART_MAX_SPEED = new GameRule<>("minecartMaxSpeed", Integer.class);

    /**
     * The number of chunks around spawn which will be kept loaded at all times.
     */
    public static final GameRule<Integer> SPAWN_CHUNK_RADIUS = new GameRule<>("spawnChunkRadius", Integer.class);

    /**
     * Configures if the world uses the locator bar.
     */
    public static final GameRule<Boolean> USE_LOCATOR_BAR = new GameRule<>("useLocatorBar", Boolean.class);

    // All GameRules instantiated above this for organizational purposes
    private final String name;
    private final Class<T> type;

    private GameRule(@NotNull String name, @NotNull Class<T> clazz) {
        Preconditions.checkNotNull(name, "GameRule name cannot be null");
        Preconditions.checkNotNull(clazz, "GameRule type cannot be null");
        Preconditions.checkArgument(clazz == Boolean.class || clazz == Integer.class, "Must be of type Boolean or Integer. Found %s ", clazz.getName());
        this.name = name;
        this.type = clazz;
        gameRules.put(name, this);
    }

    /**
     * Get the name of this GameRule.
     *
     * @return the name of this GameRule
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the type of this rule.
     *
     * @return the rule type; Integer or Boolean
     */
    @NotNull
    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GameRule)) {
            return false;
        }
        GameRule<?> other = (GameRule<?>) obj;
        return this.getName().equals(other.getName()) && this.getType() == other.getType();
    }

    @Override
    public String toString() {
        return "GameRule{" + "key=" + name + ", type=" + type + '}';
    }

    /**
     * Get a {@link GameRule} by its name.
     *
     * @param rule the name of the GameRule
     * @return the {@link GameRule} or null if no GameRule matches the given
     * name
     */
    @Nullable
    public static GameRule<?> getByName(@NotNull String rule) {
        Preconditions.checkNotNull(rule, "Rule cannot be null");
        return gameRules.get(rule);
    }

    /**
     * Get an immutable collection of {@link GameRule}s.
     *
     * @return an immutable collection containing all registered GameRules.
     */
    @NotNull
    public static GameRule<?>[] values() {
        return gameRules.values().toArray(new GameRule<?>[gameRules.size()]);
    }

    @Override
    public @NotNull String translationKey() {
        return "gamerule." + this.name;
    }

}
