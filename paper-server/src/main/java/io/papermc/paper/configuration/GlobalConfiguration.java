package io.papermc.paper.configuration;

import com.mojang.logging.LogUtils;
import io.papermc.paper.FeatureHooks;
import io.papermc.paper.configuration.constraint.Constraints;
import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import io.papermc.paper.util.DataSanitizationUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;

@SuppressWarnings({"CanBeFinal", "FieldCanBeLocal", "FieldMayBeFinal", "NotNullFieldNotInitialized", "InnerClassMayBeStatic"})
public class GlobalConfiguration extends ConfigurationPart {
    private static final Logger LOGGER = LogUtils.getLogger();
    static final int CURRENT_VERSION = 29; // (when you change the version, change the comment, so it conflicts on rebases): <insert changes here>
    private static GlobalConfiguration instance;
    public static boolean isFirstStart = false;
    public static GlobalConfiguration get() {
        return instance;
    }

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
    static void set(GlobalConfiguration instance) {
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
        }

        public Component noPermission = Component.text("I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.", NamedTextColor.RED);
        public boolean useDisplayNameInQuitMessage = false;
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
            return org.bukkit.Bukkit.getOnlineMode() || (org.spigotmc.SpigotConfig.bungee && this.bungeeCord.onlineMode) || (this.velocity.enabled && this.velocity.onlineMode);
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
        public int earlyWarningEvery = 5000;
        public int earlyWarningDelay = 10000;
    }

    public SpamLimiter spamLimiter;

    public class SpamLimiter extends ConfigurationPart {
        public int tabSpamIncrement = 1;
        public int tabSpamLimit = 500;
        public int recipeSpamIncrement = 1;
        public int recipeSpamLimit = 20;
        public int incomingPacketThreshold = 300;
    }

    public UnsupportedSettings unsupportedSettings;

    public class UnsupportedSettings extends ConfigurationPart {
        @Comment("This setting allows for exploits related to end portals, for example sand duping")
        public boolean allowUnsafeEndPortalTeleportation = false;
        @Comment("This setting controls if players should be able to break bedrock, end portals and other intended to be permanent blocks.")
        public boolean allowPermanentBlockBreakExploits = false;
        @Comment("This setting controls if player should be able to use TNT duplication, but this also allows duplicating carpet, rails and potentially other items")
        public boolean allowPistonDuplication = false;
        public boolean performUsernameValidation = true;
        @Comment("This setting controls if players should be able to create headless pistons.")
        public boolean allowHeadlessPistons = false;
        @Comment("This setting controls if the vanilla damage tick should be skipped if damage was blocked via a shield.")
        public boolean skipVanillaDamageTickWhenShieldBlocked = false;
        @Comment("This setting controls what compression format is used for region files.")
        public CompressionFormat compressionFormat = CompressionFormat.ZLIB;

        public enum CompressionFormat {
            GZIP,
            ZLIB,
            NONE
        }
    }

    public Commands commands;

    public class Commands extends ConfigurationPart {
        public boolean suggestPlayerNamesWhenNullTabCompletions = true;
        public boolean fixTargetSelectorTagCompletion = true;
        public boolean timeCommandAffectsAllWorlds = false;
    }

    public Logging logging;

    public class Logging extends ConfigurationPart {
        public boolean deobfuscateStacktraces = true;
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
        public String genParallelism = "default";

        @PostProcess
        private void postProcess() {
            ca.spottedleaf.moonrise.common.util.MoonriseCommon.adjustWorkerThreads(this.workerThreads, this.ioThreads);
            String newChunkSystemGenParallelism = this.genParallelism;
            if (newChunkSystemGenParallelism.equalsIgnoreCase("default")) {
                newChunkSystemGenParallelism = "true";
            }

            final boolean useParallelGen;
            if (newChunkSystemGenParallelism.equalsIgnoreCase("on") || newChunkSystemGenParallelism.equalsIgnoreCase("enabled")
                || newChunkSystemGenParallelism.equalsIgnoreCase("true")) {
                useParallelGen = true;
            } else if (newChunkSystemGenParallelism.equalsIgnoreCase("off") || newChunkSystemGenParallelism.equalsIgnoreCase("disabled")
                || newChunkSystemGenParallelism.equalsIgnoreCase("false")) {
                useParallelGen = false;
            } else {
                throw new IllegalStateException("Invalid option for gen-parallelism: must be one of [on, off, enabled, disabled, true, false, default]");
            }
            FeatureHooks.initChunkTaskScheduler(useParallelGen);
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
        public Map<Class<? extends Packet<?>>, PacketLimit> overrides = Map.of(ServerboundPlaceRecipePacket.class, new PacketLimit(4.0, 5.0, PacketLimit.ViolateAction.DROP));

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

    public Collisions collisions;

    public class Collisions extends ConfigurationPart {
        public boolean enablePlayerCollisions = true;
        public boolean sendFullPosForHardCollidingEntities = true;
    }

    public PlayerAutoSave playerAutoSave;


    public class PlayerAutoSave extends ConfigurationPart {
        public int rate = -1;
        private int maxPerTick = -1;
        public int maxPerTick() {
            if (this.maxPerTick < 0) {
                return (this.rate == 1 || this.rate > 100) ? 10 : 20;
            }
            return this.maxPerTick;
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
        public boolean fixEntityPositionDesync = true;
        public boolean loadPermissionsYmlBeforePlugins = true;
        @Constraints.Min(4)
        public int regionFileCacheSize = 256;
        @Comment("See https://luckformula.emc.gs")
        public boolean useAlternativeLuckFormula = false;
        public boolean useDimensionTypeForCustomSpawners = false;
        public boolean strictAdvancementDimensionCheck = false;
        public IntOr.Default compressionLevel = IntOr.Default.USE_DEFAULT;
        @Comment("Defines the leniency distance added on the server to the interaction range of a player when validating interact packets.")
        public DoubleOr.Default clientInteractionLeniencyDistance = DoubleOr.Default.USE_DEFAULT;
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

        public Obfuscation obfuscation;

        public class Obfuscation extends ConfigurationPart {
            public Items items = new Items();

            public class Items extends ConfigurationPart {
                public static Set<DataComponentType<?>> BASE_OVERRIDERS = Set.of(
                    DataComponents.MAX_STACK_SIZE,
                    DataComponents.MAX_DAMAGE,
                    DataComponents.DAMAGE,
                    DataComponents.UNBREAKABLE,
                    DataComponents.CUSTOM_NAME,
                    DataComponents.ITEM_NAME,
                    DataComponents.LORE,
                    DataComponents.RARITY,
                    DataComponents.ENCHANTMENTS,
                    DataComponents.CAN_PLACE_ON,
                    DataComponents.CAN_BREAK,
                    DataComponents.ATTRIBUTE_MODIFIERS,
                    DataComponents.HIDE_ADDITIONAL_TOOLTIP,
                    DataComponents.HIDE_TOOLTIP,
                    DataComponents.REPAIR_COST,
                    DataComponents.USE_REMAINDER,
                    DataComponents.FOOD,
                    DataComponents.DAMAGE_RESISTANT, // Not important on the player
                    DataComponents.TOOL,
                    DataComponents.ENCHANTABLE,
                    DataComponents.REPAIRABLE,
                    DataComponents.GLIDER,
                    DataComponents.TOOLTIP_STYLE,
                    DataComponents.DEATH_PROTECTION,
                    DataComponents.STORED_ENCHANTMENTS,
                    DataComponents.MAP_ID,
                    DataComponents.POTION_CONTENTS,
                    DataComponents.SUSPICIOUS_STEW_EFFECTS,
                    DataComponents.WRITABLE_BOOK_CONTENT,
                    DataComponents.WRITTEN_BOOK_CONTENT,
                    DataComponents.CUSTOM_DATA,
                    DataComponents.ENTITY_DATA,
                    DataComponents.BUCKET_ENTITY_DATA,
                    DataComponents.BLOCK_ENTITY_DATA,
                    DataComponents.INSTRUMENT,
                    DataComponents.OMINOUS_BOTTLE_AMPLIFIER,
                    DataComponents.JUKEBOX_PLAYABLE,
                    DataComponents.LODESTONE_TRACKER,
                    DataComponents.FIREWORKS,
                    DataComponents.NOTE_BLOCK_SOUND,
                    DataComponents.BEES,
                    DataComponents.LOCK,
                    DataComponents.CONTAINER_LOOT
                );

                public boolean enableItemObfuscation = false;
                public AssetObfuscationConfiguration allModels = new AssetObfuscationConfiguration(true,
                    Set.of(DataComponents.LODESTONE_TRACKER),
                    Set.of()
                );

                public Map<String, AssetObfuscationConfiguration> modelOverrides = Map.of(
                    net.minecraft.world.item.Items.ELYTRA.components().get(DataComponents.ITEM_MODEL).toString(), new AssetObfuscationConfiguration(true,
                        Set.of(DataComponents.DAMAGE),
                        Set.of()
                    )
                );

                @ConfigSerializable
                public record AssetObfuscationConfiguration(@Required boolean sanitizeCount, Set<DataComponentType<?>> dontObfuscate, Set<DataComponentType<?>> alsoObfuscate) {
                    public AssetObfuscationConfiguration(final boolean sanitizeCount, final @Nullable Set<DataComponentType<?>> dontObfuscate, final @Nullable Set<DataComponentType<?>> alsoObfuscate) {
                        this.sanitizeCount = sanitizeCount;
                        this.dontObfuscate = Objects.requireNonNullElse(dontObfuscate, Set.of());
                        this.alsoObfuscate = Objects.requireNonNullElse(alsoObfuscate, Set.of());
                    }

                }

                @PostProcess
                public void computeOverridenTypes() {
                    DataSanitizationUtil.compute(this);
                }

            }

        }

    }
}
