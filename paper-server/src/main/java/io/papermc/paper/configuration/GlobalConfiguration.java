package io.papermc.paper.configuration;

import com.mojang.logging.LogUtils;
import io.papermc.paper.configuration.constraint.Constraint;
import io.papermc.paper.configuration.constraint.Constraints;
import io.papermc.paper.configuration.mapping.MergeMap;
import io.papermc.paper.configuration.serializer.collection.map.WriteKeyBack;
import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import io.papermc.paper.util.sanitizer.ItemObfuscationBinding;
import io.papermc.paper.util.sanitizer.OversizedItemComponentSanitizer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.Holder;
import net.minecraft.util.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.bukkit.Warning;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;

@SuppressWarnings({"CanBeFinal", "FieldCanBeLocal", "FieldMayBeFinal", "NotNullFieldNotInitialized", "InnerClassMayBeStatic"})
public class GlobalConfiguration extends ConfigurationPart {
    private static final Logger LOGGER = LogUtils.getLogger();
    static final int CURRENT_VERSION = 32; // (when you change the version, change the comment, so it conflicts on rebases): merge spigot.yml and bukkit.yml
    private static GlobalConfiguration instance;
    public static boolean isFirstStart = false;
    public static GlobalConfiguration get() {
        return instance;
    }

    @Comment("Settings that are read during server initialization before the rest of this file can be loaded.")
    public InitializationConfiguration initialization;

    public ChunkLoadingBasic chunkLoadingBasic;

    public class ChunkLoadingBasic extends ConfigurationPart {
        @Comment("The maximum rate in chunks per second that the server will send to any individual player. Set to -1 to disable this limit.")
        public double playerMaxChunkSendRate = 75.0;

        @Comment(
            "The maximum rate at which chunks will load for any individual player. " +
            "Note that this setting also affects chunk generations, since a chunk load is always first issued to test if a" +
            "chunk is already generated. Set to -1 to disable this limit."
        )
        public double playerMaxChunkLoadRate = 100.0;

        @Comment("The maximum rate at which chunks will generate for any individual player. Set to -1 to disable this limit.")
        public double playerMaxChunkGenerateRate = -1.0;
    }

    public ChunkLoadingAdvanced chunkLoadingAdvanced;

    public class ChunkLoadingAdvanced extends ConfigurationPart {
        @Comment(
            "Set to true if the server will match the chunk send radius that clients have configured" +
            "in their view distance settings if the client is less-than the server's send distance."
        )
        public boolean autoConfigSendDistance = true;

        @Comment(
            "Specifies the maximum amount of concurrent chunk loads that an individual player can have." +
            "Set to 0 to let the server configure it automatically per player, or set it to -1 to disable the limit."
        )
        public int playerMaxConcurrentChunkLoads = 0;

        @Comment(
            "Specifies the maximum amount of concurrent chunk generations that an individual player can have." +
            "Set to 0 to let the server configure it automatically per player, or set it to -1 to disable the limit."
        )
        public int playerMaxConcurrentChunkGenerates = 0;
    }
    static void set(final GlobalConfiguration instance) {
        GlobalConfiguration.instance = instance;
    }

    @Setting(Configuration.VERSION_FIELD)
    public int version = CURRENT_VERSION;

    public Messages messages;

    public class Messages extends ConfigurationPart {
        public Kick kick;

        public class Kick extends ConfigurationPart {
            public Component authenticationServersDown = Component.translatable("multiplayer.disconnect.authservers_down");
            public Component connectionThrottle = Component.text("Connection throttled! Please wait before reconnecting.");
            public Component flyingPlayer = Component.translatable("multiplayer.disconnect.flying");
            public Component flyingVehicle = Component.translatable("multiplayer.disconnect.flying");
            public Component whitelist = Component.translatable("multiplayer.disconnect.not_whitelisted");
            public Component serverFull = Component.translatable("multiplayer.disconnect.server_full");
            public Component outdatedClient = Component.translatable("multiplayer.disconnect.outdated_client");
            public Component outdatedServer = Component.translatable("multiplayer.disconnect.outdated_server");
            public Component restart = Component.text("Server is restarting");
        }

        public Component noPermission = Component.text("I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.", NamedTextColor.RED);
        public boolean useDisplayNameInQuitMessage = false;
        public boolean sendCommandParseFailureMessage = true;
        @Comment("Broadcast to players when the server shuts down.")
        public Component shutdown = Component.text("Server closed");
    }

    public Spark spark;

    public class Spark extends ConfigurationPart {
        public boolean enabled = true;
        public boolean enableImmediately = false;
    }

    public Proxies proxies;

    public class Proxies extends ConfigurationPart {
        public BungeeCord bungeeCord;

        public class BungeeCord extends ConfigurationPart {
            public boolean enabled = false;
            public boolean onlineMode = true;
        }

        public Velocity velocity;

        public class Velocity extends ConfigurationPart {
            public boolean enabled = false;
            public boolean onlineMode = true;
            public String secret = "";

            @PostProcess
            private void postProcess() {
                if (!this.enabled) return;

                final String environmentSourcedVelocitySecret = System.getenv("PAPER_VELOCITY_SECRET");
                if (environmentSourcedVelocitySecret != null && !environmentSourcedVelocitySecret.isEmpty()) {
                    this.secret = environmentSourcedVelocitySecret;
                }

                if (this.secret.isEmpty()) {
                    LOGGER.error("Velocity is enabled, but no secret key was specified. A secret key is required. Disabling velocity...");
                    this.enabled = false;
                }
            }
        }
        public boolean proxyProtocol = false;
        public boolean isProxyOnlineMode() {
            return org.bukkit.Bukkit.getOnlineMode() || (this.bungeeCord.enabled && this.bungeeCord.onlineMode) || (this.velocity.enabled && this.velocity.onlineMode);
        }
    }

    public Console console;

    public class Console extends ConfigurationPart {
        public boolean enableBrigadierHighlighting = true;
        public boolean enableBrigadierCompletions = true;
        public boolean hasAllPermissions = false;
    }

    public Watchdog watchdog;

    public class Watchdog extends ConfigurationPart {
        public int timeoutSeconds = 60;
        public boolean restartOnCrash = true;
        public String restartScript = "./start.sh";
        public int earlyWarningEvery = 5000;
        public int earlyWarningDelay = 10000;
    }

    public SpamLimiter spamLimiter;

    public class SpamLimiter extends ConfigurationPart {
        public int tabSpamIncrement = 1;
        public int tabSpamLimit = 500;
        public int recipeSpamIncrement = 1;
        public int recipeSpamLimit = 20;
        public IntOr.Disabled incomingPacketThreshold = new IntOr.Disabled(OptionalInt.of(300));

        public Commands commands;
        public class Commands extends ConfigurationPart {
            public boolean enabled = false;
            public Set<String> exclusions = Set.of("/skill");
        }
    }

    public UnsupportedSettings unsupportedSettings;

    public class UnsupportedSettings extends ConfigurationPart {
        @Comment("This setting allows for exploits related to end portals, for example sand duping")
        public boolean allowUnsafeEndPortalTeleportation = false;
        @Comment("This setting controls the ability to enable dupes related to tripwires.")
        public boolean skipTripwireHookPlacementValidation = false;
        @Comment("This setting controls if players should be able to break bedrock, end portals and other intended to be permanent blocks.")
        public boolean allowPermanentBlockBreakExploits = false;
        @Comment("This setting controls if player should be able to use TNT duplication, but this also allows duplicating carpet, rails and potentially other items")
        public boolean allowPistonDuplication = false;
        public boolean performUsernameValidation = true;
        @Comment("This setting controls if players should be able to create headless pistons.")
        public boolean allowHeadlessPistons = false;
        @Comment("This setting controls if the vanilla damage tick should be skipped if damage was blocked via a shield.")
        public boolean skipVanillaDamageTickWhenShieldBlocked = false;
        @Comment("This setting controls if equipment should be updated when handling certain player actions.")
        public boolean updateEquipmentOnPlayerActions = true;
        @Comment("This setting controls what item data components don't need to be sanitized in oversized item obfuscation. Adding them re-enables exploits, but may be needed for certain resource packs. (Expected: minecraft:container, minecraft:charged_projectiles and minecraft:bundle_contents)")
        public OversizedItemComponentSanitizer.AssetOversizedItemComponentSanitizerConfiguration oversizedItemComponentSanitizer = new OversizedItemComponentSanitizer.AssetOversizedItemComponentSanitizerConfiguration(Set.of());
    }

    public Commands commands;

    public class Commands extends ConfigurationPart {
        public boolean suggestPlayerNamesWhenNullTabCompletions = true;
        @Comment("Allow mounting entities to a player in the Vanilla '/ride' command.")
        public boolean rideCommandAllowPlayerAsVehicle = false;
        @Comment("Send commands with namespace prefixes to clients")
        public boolean sendNamespacedCommands = true;
        @Comment("Send tab completions to clients")
        public boolean tabCompletion = true;
        @Comment("Hide command block output from the console, regardless of the commandBlockOutput game rule.")
        public boolean silentCommandBlockConsole = false;
        @Comment("""
            Commands that command blocks may run even when a plugin has registered the same name.
            Use a single '*' entry to allow every command.""")
        public List<String> commandBlockOverrides = List.of();
        @Comment("Let plugin-registered permissions take precedence over the vanilla permission level for a command.")
        public boolean ignoreVanillaPermissions = false;
        @Comment("""
            Command aliases, each mapping a name to the commands it runs.
            '$1-' passes every argument through, '$1' the first, and so on.""")
        public Map<String, List<String>> aliases = Map.of("icanhasbukkit", List.of("version $1-"));
    }

    public Time time;

    public class Time extends ConfigurationPart {
        public boolean affectsAllWorlds = false;
    }

    public Logging logging;

    public class Logging extends ConfigurationPart {
        @Comment("Raise the root logger to trace level. Very noisy; only useful when debugging the server itself.")
        public boolean debug = false;
        @Comment("Log the execution of commands")
        public boolean commandExecution = true;
        @Comment("Log the deaths of villagers")
        public boolean villagerDeaths = true;
        @Comment("Log the deaths of named living entities")
        public boolean namedLivingEntityDeaths = true;

        @PostProcess
        private void postProcess() {
            final LoggerContext context = (LoggerContext) LogManager.getContext(false);
            if (this.debug && !LogManager.getRootLogger().isTraceEnabled()) {
                final org.apache.logging.log4j.core.config.Configuration configuration = context.getConfiguration();
                configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(Level.ALL);
                context.updateLoggers(configuration);
            }
            if (LogManager.getRootLogger().isTraceEnabled()) {
                LOGGER.info("Debug logging is enabled");
            }
        }
    }

    public Scoreboards scoreboards;

    public class Scoreboards extends ConfigurationPart {
        public boolean trackPluginScoreboards = false;
        public boolean saveEmptyScoreboardTeams = true;
    }

    @SuppressWarnings("unused") // used in postProcess
    public ChunkSystem chunkSystem;

    public class ChunkSystem extends ConfigurationPart {

        public int ioThreads = -1;
        public int workerThreads = -1;
        @Comment("Ticks a chunk stays loaded after a plugin ticket for it is added.")
        public int pluginTicketTimeout = 600;

        @PostProcess
        private void postProcess() {
            ca.spottedleaf.moonrise.common.util.MoonriseCommon.adjustWorkerThreads(this.workerThreads, this.ioThreads);
        }
    }

    public ItemValidation itemValidation;

    public class ItemValidation extends ConfigurationPart {
        public int displayName = 8192;
        public int loreLine = 8192;
        public Book book;

        public class Book extends ConfigurationPart {
            public int title = 8192;
            public int author = 8192;
            public int page = 16384;
        }

        public BookSize bookSize;

        public class BookSize extends ConfigurationPart {
            public IntOr.Disabled pageMax = new IntOr.Disabled(OptionalInt.of(2560)); // TODO this appears to be a duplicate setting with one above
            public double totalMultiplier = 0.98D; // TODO this should probably be merged into the above inner class
        }
        public boolean resolveSelectorsInBooks = false;
    }

    public PacketLimiter packetLimiter;

    public class PacketLimiter extends ConfigurationPart {
        public Component kickMessage = Component.translatable("disconnect.exceeded_packet_rate", NamedTextColor.RED);
        public PacketLimit allPackets = new PacketLimit(7.0, 500.0, PacketLimit.ViolateAction.KICK);
        public Map<@WriteKeyBack Class<? extends Packet<?>>, PacketLimit> overrides = Map.of(ServerboundPlaceRecipePacket.class, new PacketLimit(4.0, 5.0, PacketLimit.ViolateAction.DROP));

        @ConfigSerializable
        public record PacketLimit(@Required double interval, @Required double maxPacketRate, ViolateAction action) {
            public PacketLimit(final double interval, final double maxPacketRate, final @Nullable ViolateAction action) {
                this.interval = interval;
                this.maxPacketRate = maxPacketRate;
                this.action = Objects.requireNonNullElse(action, ViolateAction.KICK);
            }

            public boolean isEnabled() {
                return this.interval > 0.0 && this.maxPacketRate > 0.0;
            }

            public enum ViolateAction {
                KICK,
                DROP;
            }
        }
    }


    @Comment("""
        Plugin-provided generators, keyed by world name. Read while a world is being created, before its
        own config exists, which is why these live here rather than in the world config.""")
    public Map<String, WorldGenerators> worldGenerators = Map.of();

    @ConfigSerializable
    public record WorldGenerators(@Nullable String generator, @Nullable String biomeProvider) {
    }

    public Collisions collisions;

    public class Collisions extends ConfigurationPart {
        public boolean enablePlayerCollisions = true;
        public boolean sendFullPosForHardCollidingEntities = true;
    }

    public Players players;

    public class Players extends ConfigurationPart {

        public boolean disableSaving = false;
        public AutoSave autoSave;
        public int userCacheSize = 1000;
        public boolean saveUserCacheOnStopOnly = false;
        @Constraint(Constraints.Positive.class)
        public int sampleCount = 12;
        public int connectionShuffle = 0;
        public class AutoSave extends ConfigurationPart {
            public int rate = -1;
            private int maxPerTick = -1;
            public int maxPerTick() {
                if (this.maxPerTick < 0) {
                    return (this.rate == 1 || this.rate > 100) ? 10 : 20;
                }
                return this.maxPerTick;
            }
        }
    }

    public Misc misc;

    public class Misc extends ConfigurationPart {

        @SuppressWarnings("unused") // used in postProcess
        public ChatThreads chatThreads;
        public class ChatThreads extends ConfigurationPart {
            private int chatExecutorCoreSize = -1;
            private int chatExecutorMaxSize = -1;

            @PostProcess
            private void postProcess() {
                //noinspection ConstantConditions
                if (net.minecraft.server.MinecraftServer.getServer() == null) return; // In testing env, this will be null here
                int _chatExecutorMaxSize = (this.chatExecutorMaxSize <= 0) ? Integer.MAX_VALUE : this.chatExecutorMaxSize; // This is somewhat dumb, but, this is the default, do we cap this?;
                int _chatExecutorCoreSize = Math.max(this.chatExecutorCoreSize, 0);

                if (_chatExecutorMaxSize < _chatExecutorCoreSize) {
                    _chatExecutorMaxSize = _chatExecutorCoreSize;
                }

                java.util.concurrent.ThreadPoolExecutor executor = (java.util.concurrent.ThreadPoolExecutor) net.minecraft.server.MinecraftServer.getServer().chatExecutor;
                executor.setCorePoolSize(_chatExecutorCoreSize);
                executor.setMaximumPoolSize(_chatExecutorMaxSize);
            }
        }
        public int maxJoinsPerTick = 5;
        @Constraints.Min(0)
        public IntOr.Default catchupTicks = IntOr.Default.USE_DEFAULT;
        public boolean sendFullPosForItemEntities = false;
        public boolean loadPermissionsYmlBeforePlugins = true;
        @Constraints.Min(4)
        public int regionFileCacheSize = 256;
        @Comment("""
            Whether chunks generated before 1.18 get the extended world height applied when they are next loaded.
            Global rather than per-world: it is also consulted by the world upgrader and the legacy structure
            file fix, which run over region files without a loaded world.""")
        public boolean belowZeroGenerationInExistingChunks = true;
        @Comment("See https://luckformula.emc.gs")
        public boolean useAlternativeLuckFormula = false;
        public boolean useDimensionTypeForCustomSpawners = false;
        public IntOr.Default compressionLevel = IntOr.Default.USE_DEFAULT;
        @Comment("Defines the leniency distance added on the server to the interaction range of a player when validating interact packets.")
        public DoubleOr.Default clientInteractionLeniencyDistance = DoubleOr.Default.USE_DEFAULT;
        @Comment("Defines how many orbs groups can exist in an area.")
        @Constraints.Min(1)
        public IntOr.Default xpOrbGroupsPerArea = IntOr.Default.USE_DEFAULT;
        @Comment("See Fix MC-163962; prevent villager demand from going negative.")
        public boolean preventNegativeVillagerDemand = false;
        @Comment("""
            Ticks between server-wide saves of global data and player data.
            Per-world chunk saving is configured separately, with chunks.auto-save-interval in the world config.""")
        public int autoSaveInterval = 6000;
        @Comment("Whether the nether dimension is enabled and will be loaded.")
        public boolean enableNether = true;
        @Comment("Whether the end dimension is enabled and will be loaded.")
        public boolean enableEnd = true;
        @Comment("File that plugin permissions are loaded from, relative to the server root.")
        public String permissionsFile = "permissions.yml";
        @Comment("Milliseconds a connecting player must wait before reconnecting. Set to -1 to disable.")
        public int connectionThrottle = 4000;
        @Comment("Whether the plugin list is included in GS4 query responses.")
        public boolean queryPlugins = true;
        @Comment("""
            Whether deprecation warnings are printed when a plugin uses a deprecated API.
            DEFAULT defers to the plugin's own author-declared preference.""")
        public Warning.WarningState deprecatedVerbose = Warning.WarningState.DEFAULT;
        @Comment("Lowest plugin API version the server will load. 'none' allows any.")
        public String minimumApi = "none";
        @Comment("Cache map colours between renders. Uses more memory but is faster.")
        public boolean useMapColorCache = true;
        @Comment("Keeps Paper's fix for MC-159283 enabled. Disable to use vanilla End ring terrain.")
        public boolean fixFarEndTerrainGeneration = true;
        @Comment("Fix for MC-301114. This removes the oldest combat entry when it hits the cap, to fix a memory leak on constant entity damage.")
        public IntOr.Disabled maxTrackingCombatEntries = new IntOr.Disabled(OptionalInt.of(10240));
        public int nettyThreads = 4;

        @PostProcess
        private void postProcess() {
            System.setProperty("io.netty.eventLoopThreads", Integer.toString(this.nettyThreads));
        }
    }

    public BlockUpdates blockUpdates;

    public class BlockUpdates extends ConfigurationPart {
        public boolean disableNoteblockUpdates = false;
        public boolean disableTripwireUpdates = false;
        public boolean disableChorusPlantUpdates = false;
        public boolean disableMushroomBlockUpdates = false;
    }

    public Anticheat anticheat;

    public class Anticheat extends ConfigurationPart {

        public double movedWronglyThreshold = 0.0625D;
        public double movedTooQuicklyMultiplier = 10.0D;

        public Obfuscation obfuscation;

        public class Obfuscation extends ConfigurationPart {
            public Items items;

            public class Items extends ConfigurationPart {

                public boolean enableItemObfuscation = false;
                public ItemObfuscationBinding.AssetObfuscationConfiguration allModels = new ItemObfuscationBinding.AssetObfuscationConfiguration(
                    true,
                    Set.of(DataComponents.LODESTONE_TRACKER),
                    Set.of()
                );

                public Map<Identifier, ItemObfuscationBinding.AssetObfuscationConfiguration> modelOverrides = Map.of(
                    Objects.requireNonNull(net.minecraft.world.item.Items.ELYTRA.components().get(DataComponents.ITEM_MODEL)),
                    new ItemObfuscationBinding.AssetObfuscationConfiguration(
                        true,
                        Set.of(DataComponents.DAMAGE),
                        Set.of()
                    )
                );

                public transient ItemObfuscationBinding binding;

                @PostProcess
                public void bindDataSanitizer() {
                    this.binding = new ItemObfuscationBinding(this);
                }
            }
        }
    }

    public UpdateChecker updateChecker;

    public class UpdateChecker extends ConfigurationPart {
        public boolean enabled = true;
    }

    public Advancements advancements;
    public class Advancements extends ConfigurationPart {
        public boolean strictDimensionCheck = false;
        public boolean disableSaving = false;
        // NOTE: the disabled-advancements list lives in `initialization`; it is read before datapacks load.
    }

    public Stats stats;
    public class Stats extends ConfigurationPart {
        public boolean disableSaving = false;
        public Object2IntMap<Holder<Identifier>> forcedCustomStatValues;
    }

    public AttributesSection attributes;
    public class AttributesSection extends ConfigurationPart {
        @ConfigSerializable
        public record AttributeOverride(double max) {
        }
        @MergeMap
        public Map<Holder<Attribute>, AttributeOverride> overrides = Stream.of(Attributes.MAX_ABSORPTION, Attributes.MAX_HEALTH, Attributes.MOVEMENT_SPEED, Attributes.ATTACK_DAMAGE)
            .collect(Collectors.toMap(Function.identity(), a -> new AttributeOverride(((RangedAttribute) a.value()).maxValue)));

        @PostProcess
        private void postProcess() {
            this.overrides.forEach((attribute, override) -> {
                if (attribute.value() instanceof final RangedAttribute ranged) {
                    ranged.maxValue = override.max();
                } else {
                    LOGGER.warn("Ignoring attribute max override for {}, it is not a ranged attribute", attribute.getRegisteredName());
                }
            });
        }
    }
}
