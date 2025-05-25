package org.bukkit.craftbukkit;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import io.papermc.paper.configuration.PaperServerConfiguration;
import io.papermc.paper.configuration.ServerConfiguration;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.Optionull;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.ReportedNbtException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ConsoleInput;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.commands.ReloadCommand;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.players.IpBanListEntry;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.phys.Vec3;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.ServerLinks;
import org.bukkit.ServerTickManager;
import org.bukkit.StructureType;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.ban.CraftIpBanList;
import org.bukkit.craftbukkit.ban.CraftProfileBanList;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.craftbukkit.boss.CraftKeyedBossbar;
import org.bukkit.craftbukkit.command.CraftCommandMap;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.entity.CraftEntityFactory;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.generator.CraftWorldInfo;
import org.bukkit.craftbukkit.generator.OldCraftChunkData;
import org.bukkit.craftbukkit.help.SimpleHelpMap;
import org.bukkit.craftbukkit.inventory.CraftBlastingRecipe;
import org.bukkit.craftbukkit.inventory.CraftCampfireRecipe;
import org.bukkit.craftbukkit.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.inventory.CraftItemCraftResult;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftMerchantCustom;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.craftbukkit.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.inventory.CraftSmithingTransformRecipe;
import org.bukkit.craftbukkit.inventory.CraftSmithingTrimRecipe;
import org.bukkit.craftbukkit.inventory.CraftSmokingRecipe;
import org.bukkit.craftbukkit.inventory.CraftStonecuttingRecipe;
import org.bukkit.craftbukkit.inventory.CraftTransmuteRecipe;
import org.bukkit.craftbukkit.inventory.RecipeIterator;
import org.bukkit.craftbukkit.inventory.util.CraftInventoryCreator;
import org.bukkit.craftbukkit.map.CraftMapColorCache;
import org.bukkit.craftbukkit.map.CraftMapCursor;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.metadata.EntityMetadataStore;
import org.bukkit.craftbukkit.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.packs.CraftDataPackManager;
import org.bukkit.craftbukkit.packs.CraftResourcePack;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.scoreboard.CraftCriteria;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.structure.CraftStructureManager;
import org.bukkit.craftbukkit.tag.CraftBlockTag;
import org.bukkit.craftbukkit.tag.CraftDamageTag;
import org.bukkit.craftbukkit.tag.CraftEntityTag;
import org.bukkit.craftbukkit.tag.CraftFluidTag;
import org.bukkit.craftbukkit.tag.CraftGameEventTag;
import org.bukkit.craftbukkit.tag.CraftItemTag;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftIconCache;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.craftbukkit.util.Versioning;
import org.bukkit.craftbukkit.util.permissions.CraftDefaultPermissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemCraftResult;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.SmithingTrimRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.TransmuteRecipe;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;
import org.bukkit.packs.DataPackManager;
import org.bukkit.packs.ResourcePack;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitWorker;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import net.md_5.bungee.api.chat.BaseComponent; // Spigot

public final class CraftServer implements Server {
    private final String serverName = io.papermc.paper.ServerBuildInfo.buildInfo().brandName();
    private final String serverVersion;
    private final String bukkitVersion = Versioning.getBukkitVersion();
    private final Logger logger = Logger.getLogger("Minecraft");
    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final CraftScheduler scheduler = new CraftScheduler();
    private final CraftCommandMap commandMap; // Paper - Move down
    private final SimpleHelpMap helpMap = new SimpleHelpMap(this);
    private final StandardMessenger messenger = new StandardMessenger();
    private final SimplePluginManager pluginManager; // Paper - Move down
    public final io.papermc.paper.plugin.manager.PaperPluginManagerImpl paperPluginManager;
    private final StructureManager structureManager;
    final DedicatedServer console;
    private final DedicatedPlayerList playerList;
    private final Map<String, World> worlds = new LinkedHashMap<>();
    private YamlConfiguration configuration;
    private YamlConfiguration commandsConfiguration;
    private final Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
    private final Map<UUID, OfflinePlayer> offlinePlayers = new MapMaker().weakValues().makeMap();
    private final EntityMetadataStore entityMetadata = new EntityMetadataStore();
    private final PlayerMetadataStore playerMetadata = new PlayerMetadataStore();
    private final WorldMetadataStore worldMetadata = new WorldMetadataStore();
    private final Object2IntOpenHashMap<SpawnCategory> spawnCategoryLimit = new Object2IntOpenHashMap<>();
    private File container;
    private WarningState warningState = WarningState.DEFAULT;
    public ApiVersion minimumAPI;
    public CraftScoreboardManager scoreboardManager;
    public CraftDataPackManager dataPackManager;
    private final CraftServerTickManager serverTickManager;
    private final CraftServerLinks serverLinks;
    public boolean playerCommandState;
    private boolean printSaveWarning;
    private CraftIconCache icon;
    private boolean overrideAllCommandBlockCommands = false;
    public boolean ignoreVanillaPermissions = false;
    private final List<CraftPlayer> playerView;
    public int reloadCount;
    public Set<String> activeCompatibilities = Collections.emptySet();
    private final io.papermc.paper.datapack.PaperDatapackManager datapackManager;
    public static Exception excessiveVelEx;
    private final io.papermc.paper.logging.SysoutCatcher sysoutCatcher = new io.papermc.paper.logging.SysoutCatcher();
    private final io.papermc.paper.potion.PaperPotionBrewer potionBrewer;
    public final io.papermc.paper.SparksFly spark;
    private final ServerConfiguration serverConfig = new PaperServerConfiguration();

    // Paper start - Folia region threading API
    private final io.papermc.paper.threadedregions.scheduler.FallbackRegionScheduler regionizedScheduler = new io.papermc.paper.threadedregions.scheduler.FallbackRegionScheduler();
    private final io.papermc.paper.threadedregions.scheduler.FoliaAsyncScheduler asyncScheduler = new io.papermc.paper.threadedregions.scheduler.FoliaAsyncScheduler();
    private final io.papermc.paper.threadedregions.scheduler.FoliaGlobalRegionScheduler globalRegionScheduler = new io.papermc.paper.threadedregions.scheduler.FoliaGlobalRegionScheduler();

    @Override
    public final io.papermc.paper.threadedregions.scheduler.RegionScheduler getRegionScheduler() {
        return this.regionizedScheduler;
    }

    @Override
    public final io.papermc.paper.threadedregions.scheduler.AsyncScheduler getAsyncScheduler() {
        return this.asyncScheduler;
    }

    @Override
    public final io.papermc.paper.threadedregions.scheduler.FoliaGlobalRegionScheduler getGlobalRegionScheduler() {
        return this.globalRegionScheduler;
    }

    @Override
    public final boolean isOwnedByCurrentRegion(World world, io.papermc.paper.math.Position position) {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(
            ((CraftWorld) world).getHandle(), position.blockX() >> 4, position.blockZ() >> 4
        );
    }

    @Override
    public final boolean isOwnedByCurrentRegion(World world, io.papermc.paper.math.Position position, int squareRadiusChunks) {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(
            ((CraftWorld) world).getHandle(), position.blockX() >> 4, position.blockZ() >> 4, squareRadiusChunks
        );
    }

    @Override
    public final boolean isOwnedByCurrentRegion(Location location) {
        World world = location.getWorld();
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(
            ((CraftWorld) world).getHandle(), location.getBlockX() >> 4, location.getBlockZ() >> 4
        );
    }

    @Override
    public final boolean isOwnedByCurrentRegion(Location location, int squareRadiusChunks) {
        World world = location.getWorld();
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(
            ((CraftWorld) world).getHandle(), location.getBlockX() >> 4, location.getBlockZ() >> 4, squareRadiusChunks
        );
    }

    @Override
    public final boolean isOwnedByCurrentRegion(World world, int chunkX, int chunkZ) {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(
            ((CraftWorld) world).getHandle(), chunkX, chunkZ
        );
    }

    @Override
    public final boolean isOwnedByCurrentRegion(World world, int chunkX, int chunkZ, int squareRadiusChunks) {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(
            ((CraftWorld) world).getHandle(), chunkX, chunkZ, squareRadiusChunks
        );
    }

    @Override
    public final boolean isOwnedByCurrentRegion(World world, int minChunkX, int minChunkZ, int maxChunkX, int maxChunkZ) {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(
            ((CraftWorld) world).getHandle(), minChunkX, minChunkZ, maxChunkX, maxChunkZ
        );
    }

    @Override
    public final boolean isOwnedByCurrentRegion(Entity entity) {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThreadFor(((org.bukkit.craftbukkit.entity.CraftEntity) entity).getHandleRaw());
    }

    @Override
    public final boolean isGlobalTickThread() {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThread();
    }
    // Paper end - Folia reagion threading API

    static {
        ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
        ConfigurationSerialization.registerClass(CraftPlayerProfile.class);
        ConfigurationSerialization.registerClass(com.destroystokyo.paper.profile.CraftPlayerProfile.class); // Paper
        CraftItemFactory.instance();
        CraftEntityFactory.instance();
    }

    public CraftServer(DedicatedServer console, PlayerList playerList) {
        this.console = console;
        this.playerList = (DedicatedPlayerList) playerList;
        this.playerView = Collections.unmodifiableList(Lists.transform(playerList.players, new Function<ServerPlayer, CraftPlayer>() {
            @Override
            public CraftPlayer apply(ServerPlayer player) {
                return player.getBukkitEntity();
            }
        }));
        this.serverVersion = io.papermc.paper.ServerBuildInfo.buildInfo().asString(io.papermc.paper.ServerBuildInfo.StringRepresentation.VERSION_SIMPLE); // Paper - improve version
        this.structureManager = new CraftStructureManager(console.getStructureManager(), console.registryAccess());
        this.dataPackManager = new CraftDataPackManager(this.getServer().getPackRepository());
        this.serverTickManager = new CraftServerTickManager(console.tickRateManager());
        this.serverLinks = new CraftServerLinks(console);

        Bukkit.setServer(this);
        // Paper start
        this.commandMap = new CraftCommandMap(this);
        this.pluginManager = new SimplePluginManager(this, commandMap);
        this.paperPluginManager = new io.papermc.paper.plugin.manager.PaperPluginManagerImpl(this, this.commandMap, pluginManager);
        this.pluginManager.paperPluginManager = this.paperPluginManager;
         // Paper end

        CraftRegistry.setMinecraftRegistry(console.registryAccess());

        if (!Main.useConsole) {
            this.getLogger().info("Console input is disabled due to --noconsole command argument");
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.configuration.options().copyDefaults(true);
        YamlConfiguration configurationDefaults = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), StandardCharsets.UTF_8));
        this.configuration.setDefaults(configurationDefaults);
        this.configuration.options().setHeader(configurationDefaults.options().getHeader());
        ConfigurationSection legacyAlias = null;
        if (!this.configuration.isString("aliases")) {
            legacyAlias = this.configuration.getConfigurationSection("aliases");
            this.configuration.set("aliases", "now-in-commands.yml");
        }
        this.saveConfig();
        if (this.getCommandsConfigFile().isFile()) {
            legacyAlias = null;
        }
        this.commandsConfiguration = YamlConfiguration.loadConfiguration(this.getCommandsConfigFile());
        this.commandsConfiguration.options().copyDefaults(true);
        // Paper start - don't enforce icanhasbukkit default if alias block exists
        final YamlConfiguration commandsDefaults = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), StandardCharsets.UTF_8));
        if (this.commandsConfiguration.contains("aliases")) commandsDefaults.set("aliases", null);
        this.commandsConfiguration.setDefaults(commandsDefaults);
        // Paper end - don't enforce icanhasbukkit default if alias block exists
        this.commandsConfiguration.options().setHeader(commandsDefaults.options().getHeader());
        this.saveCommandsConfig();

        // Migrate aliases from old file and add previously implicit $1- to pass all arguments
        if (legacyAlias != null) {
            ConfigurationSection aliases = this.commandsConfiguration.createSection("aliases");
            for (String key : legacyAlias.getKeys(false)) {
                List<String> commands = new ArrayList<>();

                if (legacyAlias.isList(key)) {
                    for (String command : legacyAlias.getStringList(key)) {
                        commands.add(command + " $1-");
                    }
                } else {
                    commands.add(legacyAlias.getString(key) + " $1-");
                }

                aliases.set(key, commands);
            }
        }

        this.saveCommandsConfig();
        this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
        this.ignoreVanillaPermissions = this.commandsConfiguration.getBoolean("ignore-vanilla-permissions");
        this.overrideSpawnLimits();
        console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
        this.warningState = WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
        TicketType.PLUGIN_TYPE_TIMEOUT = this.configuration.getInt("chunk-gc.period-in-ticks");
        this.minimumAPI = ApiVersion.getOrCreateVersion(this.configuration.getString("settings.minimum-api"));
        this.loadIcon();
        this.loadCompatibilities();
        CraftMagicNumbers.INSTANCE.getCommodore().updateReroute(activeCompatibilities::contains);

        // Set map color cache
        if (this.configuration.getBoolean("settings.use-map-color-cache")) {
            MapPalette.setMapColorCache(new CraftMapColorCache(this.logger));
        }
        this.potionBrewer = new io.papermc.paper.potion.PaperPotionBrewer(console); // Paper - custom potion mixes
        datapackManager = new io.papermc.paper.datapack.PaperDatapackManager(console.getPackRepository()); // Paper
        this.spark = new io.papermc.paper.SparksFly(this); // Paper - spark
    }

    public boolean getCommandBlockOverride(String command) {
        return this.overrideAllCommandBlockCommands || this.commandsConfiguration.getStringList("command-block-overrides").contains(command);
    }

    private File getConfigFile() {
        return (File) this.console.options.valueOf("bukkit-settings");
    }

    private File getCommandsConfigFile() {
        return (File) this.console.options.valueOf("commands-settings");
    }

    private void overrideSpawnLimits() {
        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            if (CraftSpawnCategory.isValidForLimits(spawnCategory)) {
                this.spawnCategoryLimit.put(spawnCategory, this.configuration.getInt(CraftSpawnCategory.getConfigNameSpawnLimit(spawnCategory)));
            }
        }
    }

    private void saveConfig() {
        try {
            this.configuration.save(this.getConfigFile());
        } catch (IOException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getConfigFile(), ex);
        }
    }

    private void saveCommandsConfig() {
        try {
            this.commandsConfiguration.save(this.getCommandsConfigFile());
        } catch (IOException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + this.getCommandsConfigFile(), ex);
        }
    }

    private void loadCompatibilities() {
        if (true) return; // Paper - Big nope
        ConfigurationSection compatibilities = this.configuration.getConfigurationSection("settings.compatibility");
        if (compatibilities == null) {
            this.activeCompatibilities = Collections.emptySet();
            return;
        }

        this.activeCompatibilities = compatibilities
                .getKeys(false)
                .stream()
                .filter(compatibilities::getBoolean)
                .collect(Collectors.toSet());

        if (!this.activeCompatibilities.isEmpty()) {
            this.logger.info("Using following compatibilities: `" + Joiner.on("`, `").join(this.activeCompatibilities) + "`, this will affect performance and other plugins behavior.");
            this.logger.info("Only use when necessary and prefer updating plugins if possible.");
        }

        if (this.activeCompatibilities.contains("enum-compatibility-mode")) {
            this.getLogger().warning("Loading plugins in enum compatibility mode. This will affect plugin performance. Use only as a transition period or when absolutely necessary.");
        } else if (System.getProperty("RemoveEnumBanner") == null) {
            // TODO 2024-06-16: Remove in newer version
            this.getLogger().info("*** This version of Spigot contains changes to some enums. If you notice that plugins no longer work after updating, please report this to the developers of those plugins first. ***");
            this.getLogger().info("*** If you cannot update those plugins, you can try setting `settings.compatibility.enum-compatibility-mode` to `true` in `bukkit.yml`. ***");
        }
    }

    public void loadPlugins() {
        io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler.INSTANCE.enter(io.papermc.paper.plugin.entrypoint.Entrypoint.PLUGIN); // Paper - replace implementation
    }

    @Override
    public File getPluginsFolder() {
        return this.console.getPluginsFolder();
    }

    private List<File> extraPluginJars() {
        @SuppressWarnings("unchecked")
        final List<File> jars = (List<File>) this.console.options.valuesOf("add-plugin");
        final List<File> list = new ArrayList<>();
        for (final File file : jars) {
            if (!file.exists()) {
                net.minecraft.server.MinecraftServer.LOGGER.warn("File '{}' specified through 'add-plugin' argument does not exist, cannot load a plugin from it!", file.getAbsolutePath());
                continue;
            }
            if (!file.isFile()) {
                net.minecraft.server.MinecraftServer.LOGGER.warn("File '{}' specified through 'add-plugin' argument is not a file, cannot load a plugin from it!", file.getAbsolutePath());
                continue;
            }
            if (!file.getName().endsWith(".jar")) {
                net.minecraft.server.MinecraftServer.LOGGER.warn("File '{}' specified through 'add-plugin' argument is not a jar file, cannot load a plugin from it!", file.getAbsolutePath());
                continue;
            }
            list.add(file);
        }
        return list;
    }

    public void enablePlugins(PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            this.helpMap.clear();
            this.helpMap.initializeGeneralTopics();
            if (io.papermc.paper.configuration.GlobalConfiguration.get().misc.loadPermissionsYmlBeforePlugins) loadCustomPermissions(); // Paper
        }

        Plugin[] plugins = this.pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if ((!plugin.isEnabled()) && (plugin.getDescription().getLoad() == type)) {
                this.enablePlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            // Spigot start - Allow vanilla commands to be forced to be the main command
            this.commandMap.setFallbackCommands();
            // Spigot end
            this.commandMap.registerServerAliases();
            DefaultPermissions.registerCorePermissions();
            CraftDefaultPermissions.registerCorePermissions();
            if (!io.papermc.paper.configuration.GlobalConfiguration.get().misc.loadPermissionsYmlBeforePlugins) this.loadCustomPermissions(); // Paper
            this.helpMap.initializeCommands();
            this.syncCommands();
        }
    }

    public void disablePlugins() {
        this.pluginManager.disablePlugins();
    }

    public void syncCommands() {
        Commands dispatcher = this.getHandle().getServer().getCommands(); // Paper - We now register directly to the dispatcher.

        // Refresh commands
        for (ServerPlayer player : this.getHandle().players) {
            dispatcher.sendCommands(player);
        }
    }

    private void enablePlugin(Plugin plugin) {
        try {
            List<Permission> perms = plugin.getDescription().getPermissions();
            List<Permission> permsToLoad = new ArrayList<>(); // Paper
            for (Permission perm : perms) {
                // Paper start
                if (this.paperPluginManager.getPermission(perm.getName()) == null) {
                    permsToLoad.add(perm);
                } else {
                    this.getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered");
                // Paper end
                }
            }
            this.paperPluginManager.addPermissions(permsToLoad); // Paper

            this.pluginManager.enablePlugin(plugin);
        } catch (Throwable ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }

    @Override
    public String getName() {
        return this.serverName;
    }

    @Override
    public String getVersion() {
        return this.serverVersion + " (MC: " + this.console.getServerVersion() + ")";
    }

    @Override
    public String getBukkitVersion() {
        return this.bukkitVersion;
    }

    @Override
    public String getMinecraftVersion() {
        return this.console.getServerVersion();
    }

    @Override
    public List<CraftPlayer> getOnlinePlayers() {
        return this.playerView;
    }

    @Override
    @Deprecated
    public Player getPlayer(final String name) {
        Preconditions.checkArgument(name != null, "name cannot be null");

        Player found = this.getPlayerExact(name);
        // Try for an exact match first.
        if (found != null) {
            return found;
        }

        String lowerName = name.toLowerCase(Locale.ROOT);
        int delta = Integer.MAX_VALUE;
        for (Player player : this.getOnlinePlayers()) {
            if (player.getName().toLowerCase(Locale.ROOT).startsWith(lowerName)) {
                int curDelta = Math.abs(player.getName().length() - lowerName.length());
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
                if (curDelta == 0) break;
            }
        }
        return found;
    }

    @Override
    @Deprecated
    public Player getPlayerExact(String name) {
        Preconditions.checkArgument(name != null, "name cannot be null");

        ServerPlayer player = this.playerList.getPlayerByName(name);
        return (player != null) ? player.getBukkitEntity() : null;
    }

    @Override
    public Player getPlayer(UUID id) {
        Preconditions.checkArgument(id != null, "UUID id cannot be null");

        ServerPlayer player = this.playerList.getPlayer(id);
        if (player != null) {
            return player.getBukkitEntity();
        }

        return null;
    }

    @Override
    @Deprecated
    public List<Player> matchPlayer(String partialName) {
        Preconditions.checkArgument(partialName != null, "partialName cannot be null");

        List<Player> matchedPlayers = new ArrayList<>();

        for (Player iterPlayer : this.getOnlinePlayers()) {
            String iterPlayerName = iterPlayer.getName();

            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (iterPlayerName.toLowerCase(Locale.ROOT).contains(partialName.toLowerCase(Locale.ROOT))) {
                // Partial match
                matchedPlayers.add(iterPlayer);
            }
        }

        return matchedPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return this.playerList.getMaxPlayers();
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        Preconditions.checkArgument(maxPlayers >= 0, "maxPlayers must be >= 0");

        this.playerList.maxPlayers = maxPlayers;
    }

    // NOTE: These are dependent on the corresponding call in MinecraftServer
    // so if that changes this will need to as well
    @Override
    public int getPort() {
        return this.getServer().getPort();
    }

    @Override
    public int getViewDistance() {
        return this.getProperties().viewDistance;
    }

    @Override
    public int getSimulationDistance() {
        return this.getProperties().simulationDistance;
    }

    @Override
    public String getIp() {
        return this.getServer().getLocalIp();
    }

    @Override
    public String getWorldType() {
        return this.getProperties().properties.getProperty("level-type");
    }

    @Override
    public boolean getGenerateStructures() {
        return this.getServer().getWorldData().worldGenOptions().generateStructures();
    }

    @Override
    public int getMaxWorldSize() {
        return this.getProperties().maxWorldSize;
    }

    @Override
    public boolean getAllowEnd() {
        return this.configuration.getBoolean("settings.allow-end");
    }

    @Override
    public boolean getAllowNether() {
        return this.getProperties().allowNether;
    }

    @Override
    public boolean isLoggingIPs() {
        return this.getServer().logIPs();
    }

    public boolean getWarnOnOverload() {
        return this.configuration.getBoolean("settings.warn-on-overload");
    }

    public boolean getQueryPlugins() {
        return this.configuration.getBoolean("settings.query-plugins");
    }

    @Override
    public List<String> getInitialEnabledPacks() {
        return Collections.unmodifiableList(this.getProperties().initialDataPackConfiguration.getEnabled());
    }

    @Override
    public List<String> getInitialDisabledPacks() {
        return Collections.unmodifiableList(this.getProperties().initialDataPackConfiguration.getDisabled());
    }

    @Override
    public DataPackManager getDataPackManager() {
        return this.dataPackManager;
    }

    @Override
    public ServerTickManager getServerTickManager() {
        return this.serverTickManager;
    }

    @Override
    public ResourcePack getServerResourcePack() {
        return this.getServer().getServerResourcePack().map(CraftResourcePack::new).orElse(null);
    }

    @Override
    public String getResourcePack() {
        return this.getServer().getServerResourcePack().map(MinecraftServer.ServerResourcePackInfo::url).orElse("");
    }

    @Override
    public String getResourcePackHash() {
        return this.getServer().getServerResourcePack().map(MinecraftServer.ServerResourcePackInfo::hash).orElse("").toUpperCase(Locale.ROOT);
    }

    @Override
    public String getResourcePackPrompt() {
        return this.getServer().getServerResourcePack().map(MinecraftServer.ServerResourcePackInfo::prompt).map(CraftChatMessage::fromComponent).orElse("");
    }

    @Override
    public boolean isResourcePackRequired() {
        return this.getServer().isResourcePackRequired();
    }

    @Override
    public boolean hasWhitelist() {
        return this.playerList.isUsingWhitelist();
    }

    // NOTE: Temporary calls through to server.properies until its replaced
    private DedicatedServerProperties getProperties() {
        return this.console.getProperties();
    }
    // End Temporary calls

    @Override
    public String getUpdateFolder() {
        return this.configuration.getString("settings.update-folder", "update");
    }

    @Override
    public File getUpdateFolderFile() {
        return new File((File) this.console.options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
    }

    @Override
    public long getConnectionThrottle() {
        // Spigot start - Automatically set connection throttle for bungee configurations
        if (org.spigotmc.SpigotConfig.bungee || io.papermc.paper.configuration.GlobalConfiguration.get().proxies.velocity.enabled) { // Paper - Add Velocity IP Forwarding Support
            return -1;
        } else {
            return this.configuration.getInt("settings.connection-throttle");
        }
        // Spigot end
    }

    @Override
    @Deprecated
    public int getTicksPerAnimalSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.ANIMAL);
    }

    @Override
    @Deprecated
    public int getTicksPerMonsterSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public int getTicksPerWaterSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public int getTicksPerWaterAmbientSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public int getTicksPerWaterUndergroundCreatureSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public int getTicksPerAmbientSpawns() {
        return this.getTicksPerSpawns(SpawnCategory.AMBIENT);
    }

    @Override
    public int getTicksPerSpawns(SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory.%s are not supported", spawnCategory);
        return this.configuration.getInt(CraftSpawnCategory.getConfigNameTicksPerSpawn(spawnCategory));
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public CraftScheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        return this.servicesManager;
    }

    @Override
    public List<World> getWorlds() {
        return new ArrayList<World>(this.worlds.values());
    }

    @Override
    public boolean isTickingWorlds() {
        return console.isIteratingOverLevels;
    }

    public DedicatedPlayerList getHandle() {
        return this.playerList;
    }

    // NOTE: Should only be called from DedicatedServer.ah()
    public boolean dispatchServerCommand(CommandSender sender, ConsoleInput serverCommand) {
        if (sender instanceof Conversable) {
            Conversable conversable = (Conversable) sender;

            if (conversable.isConversing()) {
                conversable.acceptConversationInput(serverCommand.msg);
                return true;
            }
        }
        try {
            this.playerCommandState = true;
            return this.dispatchCommand(sender, serverCommand.msg);
        } catch (Exception ex) {
            this.getLogger().log(Level.WARNING, "Unexpected exception while parsing console command \"" + serverCommand.msg + '"', ex);
            return false;
        } finally {
            this.playerCommandState = false;
        }
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        Preconditions.checkArgument(sender != null, "sender cannot be null");
        Preconditions.checkArgument(commandLine != null, "commandLine cannot be null");
        org.spigotmc.AsyncCatcher.catchOp("Command Dispatched Async: " + commandLine); // Spigot // Paper - Include command in error message

        if (this.commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        return this.dispatchCommand(VanillaCommandWrapper.getListener(sender), commandLine);
    }

    public boolean dispatchCommand(CommandSourceStack sourceStack, String commandLine) {
        net.minecraft.commands.Commands commands = this.getHandle().getServer().getCommands();
        com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher = commands.getDispatcher();
        com.mojang.brigadier.ParseResults<CommandSourceStack> results = dispatcher.parse(commandLine, sourceStack);

        CommandSender sender = sourceStack.getBukkitSender();
        String[] args = org.apache.commons.lang3.StringUtils.split(commandLine, ' '); // Paper - fix adjacent spaces (from console/plugins) causing empty array elements
        Command target = this.commandMap.getCommand(args[0].toLowerCase(java.util.Locale.ENGLISH));

        try {
            commands.performCommand(results, commandLine, commandLine, true);
        } catch (CommandException ex) {
            new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerCommandException(ex, target, sender, args)).callEvent(); // Paper
            throw ex;
        } catch (Throwable ex) {
            String msg = "Unhandled exception executing '" + commandLine + "' in " + target;
            new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerCommandException(ex, target, sender, args)).callEvent(); // Paper
            throw new CommandException(msg, ex);
        }
        // Paper end

        return false;
    }

    @Override
    public void reload() {
        // Paper start - lifecycle events
        if (io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner.INSTANCE.blocksPluginReloading()) {
            throw new IllegalStateException(org.bukkit.command.defaults.ReloadCommand.RELOADING_DISABLED_MESSAGE);
        }
        // Paper end - lifecycle events
        org.spigotmc.WatchdogThread.hasStarted = false; // Paper - Disable watchdog early timeout on reload
        this.reloadCount++;
        this.configuration = YamlConfiguration.loadConfiguration(this.getConfigFile());
        this.commandsConfiguration = YamlConfiguration.loadConfiguration(this.getCommandsConfigFile());

        this.console.settings = new DedicatedServerSettings(this.console.options);
        DedicatedServerProperties config = this.console.settings.getProperties();

        this.console.setPvpAllowed(config.pvp);
        this.console.setFlightAllowed(config.allowFlight);
        this.console.setMotd(config.motd);
        this.overrideSpawnLimits();
        this.warningState = WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
        TicketType.PLUGIN_TYPE_TIMEOUT = this.configuration.getInt("chunk-gc.period-in-ticks");
        this.minimumAPI = ApiVersion.getOrCreateVersion(this.configuration.getString("settings.minimum-api"));
        this.printSaveWarning = false;
        this.console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
        this.loadIcon();
        this.loadCompatibilities();
        CraftMagicNumbers.INSTANCE.getCommodore().updateReroute(activeCompatibilities::contains);

        try {
            this.playerList.getIpBans().load();
        } catch (IOException ex) {
            this.logger.log(Level.WARNING, "Failed to load banned-ips.json, " + ex.getMessage());
        }
        try {
            this.playerList.getBans().load();
        } catch (IOException ex) {
            this.logger.log(Level.WARNING, "Failed to load banned-players.json, " + ex.getMessage());
        }

        org.spigotmc.SpigotConfig.init((File) this.console.options.valueOf("spigot-settings")); // Spigot
        this.console.paperConfigurations.reloadConfigs(this.console);
        for (ServerLevel world : this.console.getAllLevels()) {
            // world.serverLevelData.setDifficulty(config.difficulty); // Paper - per level difficulty
            world.setSpawnSettings(world.serverLevelData.getDifficulty() != Difficulty.PEACEFUL && config.spawnMonsters); // Paper - per level difficulty (from MinecraftServer#setDifficulty(ServerLevel, Difficulty, boolean))

            for (SpawnCategory spawnCategory : SpawnCategory.values()) {
                if (CraftSpawnCategory.isValidForLimits(spawnCategory)) {
                    long ticksPerCategorySpawn = this.getTicksPerSpawns(spawnCategory);
                    if (ticksPerCategorySpawn < 0) {
                        world.ticksPerSpawnCategory.put(spawnCategory, CraftSpawnCategory.getDefaultTicksPerSpawn(spawnCategory));
                    } else {
                        world.ticksPerSpawnCategory.put(spawnCategory, ticksPerCategorySpawn);
                    }
                }
            }
            world.spigotConfig.init(); // Spigot
        }

        Plugin[] pluginClone = pluginManager.getPlugins().clone(); // Paper
        this.commandMap.clearCommands(); // Paper - Move command reloading up
        this.pluginManager.clearPlugins();
        // Paper - move up
        // Paper start
        for (Plugin plugin : pluginClone) {
            entityMetadata.removeAll(plugin);
            worldMetadata.removeAll(plugin);
            playerMetadata.removeAll(plugin);
        }
        // Paper end
        this.reloadData();
        org.spigotmc.SpigotConfig.registerCommands(); // Spigot
        io.papermc.paper.command.PaperCommands.registerCommands(this.console); // Paper
        this.spark.registerCommandBeforePlugins(this); // Paper - spark
        this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
        this.ignoreVanillaPermissions = this.commandsConfiguration.getBoolean("ignore-vanilla-permissions");

        int pollCount = 0;

        // Wait for at most 2.5 seconds for plugins to close their threads
        while (pollCount < 50 && this.getScheduler().getActiveWorkers().size() > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}
            pollCount++;
        }

        List<BukkitWorker> overdueWorkers = this.getScheduler().getActiveWorkers();
        for (BukkitWorker worker : overdueWorkers) {
            Plugin plugin = worker.getOwner();
            this.getLogger().log(Level.SEVERE, String.format(
                "Nag author(s): '%s' of '%s' about the following: %s",
                plugin.getDescription().getAuthors(),
                plugin.getDescription().getFullName(),
                "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"
            ));
            if (console.isDebugging()) io.papermc.paper.util.TraceUtil.dumpTraceForThread(worker.getThread(), "still running"); // Paper - Debugging
        }
        io.papermc.paper.plugin.PluginInitializerManager.reload(this.console); // Paper
        this.loadPlugins();
        this.enablePlugins(PluginLoadOrder.STARTUP);
        this.enablePlugins(PluginLoadOrder.POSTWORLD);
        this.spark.registerCommandAfterPlugins(this); // Paper - spark
        if (io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper != null) io.papermc.paper.plugin.PluginInitializerManager.instance().pluginRemapper.pluginsEnabled(); // Paper - Remap plugins
        // Paper start - brigadier command API
        io.papermc.paper.command.brigadier.PaperCommands.INSTANCE.setValid(); // to clear invalid state for event fire below
        io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner.INSTANCE.callReloadableRegistrarEvent(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, io.papermc.paper.command.brigadier.PaperCommands.INSTANCE, org.bukkit.plugin.Plugin.class, io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent.Cause.RELOAD); // call commands event for regular plugins
        this.helpMap.initializeCommands();
        this.syncCommands(); // Refresh commands after event
        // Paper end - brigadier command API
        new ServerLoadEvent(ServerLoadEvent.LoadType.RELOAD).callEvent();
        org.spigotmc.WatchdogThread.hasStarted = true; // Paper - Disable watchdog early timeout on reload
    }

    // Paper start - Wait for Async Tasks during shutdown
    public void waitForAsyncTasksShutdown() {
        int pollCount = 0;

        // Wait for at most 5 seconds for plugins to close their threads
        while (pollCount < 10*5 && getScheduler().getActiveWorkers().size() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            pollCount++;
        }

        List<BukkitWorker> overdueWorkers = getScheduler().getActiveWorkers();
        for (BukkitWorker worker : overdueWorkers) {
            Plugin plugin = worker.getOwner();
            getLogger().log(Level.SEVERE, String.format(
                "Nag author(s): '%s' of '%s' about the following: %s",
                plugin.getPluginMeta().getAuthors(),
                plugin.getPluginMeta().getDisplayName(),
                "This plugin is not properly shutting down its async tasks when it is being shut down. This task may throw errors during the final shutdown logs and might not complete before process dies."
            ));
            if (console.isDebugging()) io.papermc.paper.util.TraceUtil.dumpTraceForThread(worker.getThread(), "still running"); // Paper - Debugging
        }
    }
    // Paper end - Wait for Async Tasks during shutdown

    @Override
    public void reloadData() {
        ReloadCommand.reload(this.console);
    }

    // Paper start - API for updating recipes on clients
    @Override
    public void updateResources() {
        this.playerList.reloadResources();
    }

    @Override
    public void updateRecipes() {
        this.playerList.reloadRecipes();
    }
    // Paper end - API for updating recipes on clients

    private void loadIcon() {
        this.icon = new CraftIconCache(null);
        try {
            final File file = new File(new File("."), "server-icon.png");
            if (file.isFile()) {
                this.icon = CraftServer.loadServerIcon0(file);
            }
        } catch (Exception ex) {
            this.getLogger().log(Level.WARNING, "Couldn't load server icon", ex);
        }
    }

    @SuppressWarnings({ "unchecked", "finally" })
    private void loadCustomPermissions() {
        File file = new File(this.configuration.getString("settings.permissions-file"));
        FileInputStream stream;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            try {
                file.createNewFile();
            } finally {
                return;
            }
        }

        Map<String, Map<String, Object>> perms;

        try {
            perms = this.yaml.load(stream);
        } catch (MarkedYAMLException ex) {
            this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML: " + ex.toString());
            return;
        } catch (Throwable ex) {
            this.getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML.", ex);
            return;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {}
        }

        if (perms == null) {
            this.getLogger().log(Level.INFO, "Server permissions file " + file + " is empty, ignoring it");
            return;
        }

        List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);

        for (Permission perm : permsList) {
            try {
                this.pluginManager.addPermission(perm);
            } catch (IllegalArgumentException ex) {
                this.getLogger().log(Level.SEVERE, "Permission in " + file + " was already defined", ex);
            }
        }
    }

    @Override
    public String toString() {
        return "CraftServer{" + "serverName=" + this.serverName + ",serverVersion=" + this.serverVersion + ",minecraftVersion=" + this.console.getServerVersion() + '}';
    }

    @Override
    public World createWorld(WorldCreator creator) {
        Preconditions.checkState(this.console.getAllLevels().iterator().hasNext(), "Cannot create additional worlds on STARTUP");
        //Preconditions.checkState(!this.console.isIteratingOverLevels, "Cannot create a world while worlds are being ticked"); // Paper - Cat - Temp disable. We'll see how this goes.
        Preconditions.checkArgument(creator != null, "WorldCreator cannot be null");

        String name = creator.name();
        ChunkGenerator chunkGenerator = creator.generator();
        BiomeProvider biomeProvider = creator.biomeProvider();
        File folder = new File(this.getWorldContainer(), name);
        World world = this.getWorld(name);

        // Paper start
        World worldByKey = this.getWorld(creator.key());
        if (world != null || worldByKey != null) {
            if (world == worldByKey) {
                return world;
            }
            throw new IllegalArgumentException("Cannot create a world with key " + creator.key() + " and name " + name + " one (or both) already match a world that exists");
        }
        // Paper end

        if (folder.exists()) {
            Preconditions.checkArgument(folder.isDirectory(), "File (%s) exists and isn't a folder", name);
        }

        if (chunkGenerator == null) {
            chunkGenerator = this.getGenerator(name);
        }

        if (biomeProvider == null) {
            biomeProvider = this.getBiomeProvider(name);
        }

        ResourceKey<LevelStem> actualDimension = switch (creator.environment()) {
            case NORMAL -> LevelStem.OVERWORLD;
            case NETHER -> LevelStem.NETHER;
            case THE_END -> LevelStem.END;
            default -> throw new IllegalArgumentException("Illegal dimension (" + creator.environment() + ")");
        };

        LevelStorageSource.LevelStorageAccess levelStorageAccess;
        try {
            levelStorageAccess = LevelStorageSource.createDefault(this.getWorldContainer().toPath()).validateAndCreateAccess(name, actualDimension);
        } catch (IOException | ContentValidationException ex) {
            throw new RuntimeException(ex);
        }

        Dynamic<?> dataTag;
        if (levelStorageAccess.hasWorldData()) {
            net.minecraft.world.level.storage.LevelSummary summary;
            try {
                dataTag = levelStorageAccess.getDataTag();
                summary = levelStorageAccess.getSummary(dataTag);
            } catch (NbtException | ReportedNbtException | IOException e) {
                LevelStorageSource.LevelDirectory levelDirectory = levelStorageAccess.getLevelDirectory();
                MinecraftServer.LOGGER.warn("Failed to load world data from {}", levelDirectory.dataFile(), e);
                MinecraftServer.LOGGER.info("Attempting to use fallback");

                try {
                    dataTag = levelStorageAccess.getDataTagFallback();
                    summary = levelStorageAccess.getSummary(dataTag);
                } catch (NbtException | ReportedNbtException | IOException e1) {
                    MinecraftServer.LOGGER.error("Failed to load world data from {}", levelDirectory.oldDataFile(), e1);
                    MinecraftServer.LOGGER.error(
                        "Failed to load world data from {} and {}. World files may be corrupted. Shutting down.",
                        levelDirectory.dataFile(),
                        levelDirectory.oldDataFile()
                    );
                    return null;
                }

                levelStorageAccess.restoreLevelDataFromOld();
            }

            if (summary.requiresManualConversion()) {
                MinecraftServer.LOGGER.info("This world must be opened in an older version (like 1.6.4) to be safely converted");
                return null;
            }

            if (!summary.isCompatible()) {
                MinecraftServer.LOGGER.info("This world was created by an incompatible version.");
                return null;
            }
        } else {
            dataTag = null;
        }

        boolean hardcore = creator.hardcore();

        PrimaryLevelData primaryLevelData;
        WorldLoader.DataLoadContext context = this.console.worldLoader;
        RegistryAccess.Frozen registryAccess = context.datapackDimensions();
        net.minecraft.core.Registry<LevelStem> contextLevelStemRegistry = registryAccess.lookupOrThrow(Registries.LEVEL_STEM);
        if (dataTag != null) {
            LevelDataAndDimensions levelDataAndDimensions = LevelStorageSource.getLevelDataAndDimensions(
                dataTag, context.dataConfiguration(), contextLevelStemRegistry, context.datapackWorldgen()
            );
            primaryLevelData = (PrimaryLevelData) levelDataAndDimensions.worldData();
            registryAccess = levelDataAndDimensions.dimensions().dimensionsRegistryAccess();
        } else {
            LevelSettings levelSettings;
            WorldOptions worldOptions = new WorldOptions(creator.seed(), creator.generateStructures(), creator.bonusChest());
            WorldDimensions worldDimensions;

            DedicatedServerProperties.WorldDimensionData properties = new DedicatedServerProperties.WorldDimensionData(GsonHelper.parse((creator.generatorSettings().isEmpty()) ? "{}" : creator.generatorSettings()), creator.type().name().toLowerCase(Locale.ROOT));
            levelSettings = new LevelSettings(
                name,
                GameType.byId(this.getDefaultGameMode().getValue()),
                hardcore, Difficulty.EASY,
                false,
                new GameRules(context.dataConfiguration().enabledFeatures()),
                context.dataConfiguration())
            ;
            worldDimensions = properties.create(context.datapackWorldgen());

            WorldDimensions.Complete complete = worldDimensions.bake(contextLevelStemRegistry);
            Lifecycle lifecycle = complete.lifecycle().add(context.datapackWorldgen().allRegistriesLifecycle());

            primaryLevelData = new PrimaryLevelData(levelSettings, worldOptions, complete.specialWorldProperty(), lifecycle);
            registryAccess = complete.dimensionsRegistryAccess();
        }

        contextLevelStemRegistry = registryAccess.lookupOrThrow(Registries.LEVEL_STEM);
        primaryLevelData.customDimensions = contextLevelStemRegistry;
        primaryLevelData.checkName(name);
        primaryLevelData.setModdedInfo(this.console.getServerModName(), this.console.getModdedStatus().shouldReportAsModified());

        if (this.console.options.has("forceUpgrade")) {
            net.minecraft.server.Main.forceUpgrade(levelStorageAccess, primaryLevelData, DataFixers.getDataFixer(), this.console.options.has("eraseCache"), () -> true, registryAccess, this.console.options.has("recreateRegionFiles"));
        }

        long i = BiomeManager.obfuscateSeed(primaryLevelData.worldGenOptions().seed());
        List<CustomSpawner> list = ImmutableList.of(
            new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(primaryLevelData)
        );
        LevelStem customStem = contextLevelStemRegistry.getValue(actualDimension);

        WorldInfo worldInfo = new CraftWorldInfo(primaryLevelData, levelStorageAccess, creator.environment(), customStem.type().value(), customStem.generator(), this.getHandle().getServer().registryAccess()); // Paper - Expose vanilla BiomeProvider from WorldInfo
        if (biomeProvider == null && chunkGenerator != null) {
            biomeProvider = chunkGenerator.getDefaultBiomeProvider(worldInfo);
        }

        ResourceKey<net.minecraft.world.level.Level> dimensionKey;
        String levelName = this.getServer().getProperties().levelName;
        if (name.equals(levelName + "_nether")) {
            dimensionKey = net.minecraft.world.level.Level.NETHER;
        } else if (name.equals(levelName + "_the_end")) {
            dimensionKey = net.minecraft.world.level.Level.END;
        } else {
            dimensionKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(creator.key().namespace(), creator.key().value()));
        }

        // If set to not keep spawn in memory (changed from default) then adjust rule accordingly
        if (creator.keepSpawnLoaded() == net.kyori.adventure.util.TriState.FALSE) { // Paper
            primaryLevelData.getGameRules().getRule(GameRules.RULE_SPAWN_CHUNK_RADIUS).set(0, null);
        }

        ServerLevel serverLevel = new ServerLevel(
            this.console,
            this.console.executor,
            levelStorageAccess,
            primaryLevelData,
            dimensionKey,
            customStem,
            this.getServer().progressListenerFactory.create(primaryLevelData.getGameRules().getInt(GameRules.RULE_SPAWN_CHUNK_RADIUS)),
            primaryLevelData.isDebugWorld(),
            i,
            creator.environment() == Environment.NORMAL ? list : ImmutableList.of(),
            true,
            this.console.overworld().getRandomSequences(),
            creator.environment(),
            chunkGenerator, biomeProvider
        );

        if (!(this.worlds.containsKey(name.toLowerCase(Locale.ROOT)))) {
            return null;
        }

        this.console.addLevel(serverLevel); // Paper - Put world into worldlist before initing the world; move up
        this.console.initWorld(serverLevel, primaryLevelData, primaryLevelData, primaryLevelData.worldGenOptions());

        serverLevel.setSpawnSettings(true);
        // Paper - Put world into worldlist before initing the world; move up

        this.getServer().prepareLevels(serverLevel.getChunkSource().chunkMap.progressListener, serverLevel);
        io.papermc.paper.FeatureHooks.tickEntityManager(serverLevel); // SPIGOT-6526: Load pending entities so they are available to the API // Paper - chunk system

        new WorldLoadEvent(serverLevel.getWorld()).callEvent();
        return serverLevel.getWorld();
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        return this.unloadWorld(this.getWorld(name), save);
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        //Preconditions.checkState(!this.console.isIteratingOverLevels, "Cannot unload a world while worlds are being ticked"); // Paper - Cat - Temp disable. We'll see how this goes.
        if (world == null) {
            return false;
        }

        ServerLevel handle = ((CraftWorld) world).getHandle();

        if (this.console.getLevel(handle.dimension()) == null) {
            return false;
        }

        if (handle.dimension() == net.minecraft.world.level.Level.OVERWORLD) {
            return false;
        }

        if (!handle.players().isEmpty()) {
            return false;
        }

        WorldUnloadEvent event = new WorldUnloadEvent(handle.getWorld());
        if (!event.callEvent()) {
            return false;
        }

        try {
            if (save) {
                handle.save(null, true, false); // Paper - Fix saving in unloadWorld
            }

            handle.getChunkSource().close(save);
            io.papermc.paper.FeatureHooks.closeEntityManager(handle, save); // SPIGOT-6722: close entityManager // Paper - chunk system
            handle.levelStorageAccess.close();
        } catch (Exception ex) {
            this.getLogger().log(Level.SEVERE, null, ex);
        }

        this.worlds.remove(world.getName().toLowerCase(Locale.ROOT));
        this.console.removeLevel(handle);
        return true;
    }

    public DedicatedServer getServer() {
        return this.console;
    }

    @Override
    public World getWorld(String name) {
        Preconditions.checkArgument(name != null, "name cannot be null");

        return this.worlds.get(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public World getWorld(UUID uid) {
        for (World world : this.worlds.values()) {
            if (world.getUID().equals(uid)) {
                return world;
            }
        }
        return null;
    }

    @Override
    public World getWorld(net.kyori.adventure.key.Key worldKey) {
        ServerLevel level = console.getLevel(ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION, io.papermc.paper.adventure.PaperAdventure.asVanilla(worldKey)));
        if (level == null) return null;
        return level.getWorld();
    }

    public void addWorld(World world) {
        // Check if a World already exists with the UID.
        if (this.getWorld(world.getUID()) != null) {
            System.out.println("World " + world.getName() + " is a duplicate of another world and has been prevented from loading. Please delete the uid.dat file from " + world.getName() + "'s world directory if you want to be able to load the duplicate world.");
            return;
        }
        this.worlds.put(world.getName().toLowerCase(Locale.ROOT), world);
    }

    @Override
    public WorldBorder createWorldBorder() {
        return new CraftWorldBorder(new net.minecraft.world.level.border.WorldBorder());
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        Command command = this.commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        } else {
            return null;
        }
    }

    @Override
    public void savePlayers() {
        this.checkSaveState();
        this.playerList.saveAll();
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        // Paper start - API for updating recipes on clients
        return this.addRecipe(recipe, false);
    }

    @Override
    public boolean addRecipe(Recipe recipe, boolean resendRecipes) {
        // Paper end - API for updating recipes on clients
        CraftRecipe toAdd;
        if (recipe instanceof CraftRecipe) {
            toAdd = (CraftRecipe) recipe;
        } else {
            if (recipe instanceof ShapedRecipe) {
                toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe) recipe);
            } else if (recipe instanceof FurnaceRecipe) {
                toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe) recipe);
            } else if (recipe instanceof BlastingRecipe) {
                toAdd = CraftBlastingRecipe.fromBukkitRecipe((BlastingRecipe) recipe);
            } else if (recipe instanceof CampfireRecipe) {
                toAdd = CraftCampfireRecipe.fromBukkitRecipe((CampfireRecipe) recipe);
            } else if (recipe instanceof SmokingRecipe) {
                toAdd = CraftSmokingRecipe.fromBukkitRecipe((SmokingRecipe) recipe);
            } else if (recipe instanceof StonecuttingRecipe) {
                toAdd = CraftStonecuttingRecipe.fromBukkitRecipe((StonecuttingRecipe) recipe);
            } else if (recipe instanceof SmithingTransformRecipe) {
                toAdd = CraftSmithingTransformRecipe.fromBukkitRecipe((SmithingTransformRecipe) recipe);
            } else if (recipe instanceof SmithingTrimRecipe) {
                toAdd = CraftSmithingTrimRecipe.fromBukkitRecipe((SmithingTrimRecipe) recipe);
            } else if (recipe instanceof TransmuteRecipe) {
                toAdd = CraftTransmuteRecipe.fromBukkitRecipe((TransmuteRecipe) recipe);
            } else if (recipe instanceof ComplexRecipe) {
                throw new UnsupportedOperationException("Cannot add custom complex recipe");
            } else {
                return false;
            }
        }
        toAdd.addToCraftingManager();
        // Paper start - API for updating recipes on clients
        if (true || resendRecipes) { // Always needs to be resent now... TODO
            this.playerList.reloadRecipes();
        }
        // Paper end - API for updating recipes on clients
        return true;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        Preconditions.checkArgument(result != null, "ItemStack cannot be null");

        List<Recipe> results = new ArrayList<>();
        Iterator<Recipe> iter = this.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            ItemStack stack = recipe.getResult();
            if (stack.getType() != result.getType()) {
                continue;
            }
            if (result.getDurability() == -1 || result.getDurability() == stack.getDurability()) {
                results.add(recipe);
            }
        }
        return results;
    }

    @Override
    public Recipe getRecipe(NamespacedKey recipeKey) {
        Preconditions.checkArgument(recipeKey != null, "NamespacedKey recipeKey cannot be null");

        return this.getServer().getRecipeManager().byKey(CraftRecipe.toMinecraft(recipeKey)).map(RecipeHolder::toBukkitRecipe).orElse(null);
    }

    private CraftingContainer createCraftingContainer() {
        // Create a players Crafting Inventory
        AbstractContainerMenu container = new AbstractContainerMenu(null, -1) {
            @Override
            public InventoryView getBukkitView() {
                return null;
            }

            @Override
            public boolean stillValid(net.minecraft.world.entity.player.Player player) {
                return false;
            }

            @Override
            public net.minecraft.world.item.ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int slot) {
                return net.minecraft.world.item.ItemStack.EMPTY;
            }
        };

        return new TransientCraftingContainer(container, 3, 3);
    }

    @Override
    public Recipe getCraftingRecipe(ItemStack[] craftingMatrix, World world) {
        return this.getNMSRecipe(craftingMatrix, this.createCraftingContainer(), (CraftWorld) world).map(RecipeHolder::toBukkitRecipe).orElse(null);
    }

    @Override
    public ItemStack craftItem(ItemStack[] craftingMatrix, World world, Player player) {
        return this.craftItemResult(craftingMatrix, world, player).getResult();
    }

    @Override
    public ItemCraftResult craftItemResult(ItemStack[] craftingMatrix, World world, Player player) {
        Preconditions.checkArgument(world != null, "world cannot be null");
        Preconditions.checkArgument(player != null, "player cannot be null");

        CraftWorld craftWorld = (CraftWorld) world;
        CraftPlayer craftPlayer = (CraftPlayer) player;

        // Create a players Crafting Inventory and get the recipe
        CraftingMenu container = new CraftingMenu(-1, craftPlayer.getHandle().getInventory());
        CraftingContainer craftingContainer = container.craftSlots;
        ResultContainer craftResult = container.resultSlots;

        Optional<RecipeHolder<CraftingRecipe>> recipe = this.getNMSRecipe(craftingMatrix, craftingContainer, craftWorld);

        // Generate the resulting ItemStack from the Crafting Matrix
        net.minecraft.world.item.ItemStack itemstack = net.minecraft.world.item.ItemStack.EMPTY;

        if (recipe.isPresent()) {
            RecipeHolder<CraftingRecipe> recipeCrafting = recipe.get();
            craftingContainer.setCurrentRecipe(recipeCrafting);
            if (craftResult.setRecipeUsed(craftPlayer.getHandle(), recipeCrafting)) {
                itemstack = recipeCrafting.value().assemble(craftingContainer.asCraftInput(), craftWorld.getHandle().registryAccess());
            }
        }

        // Call Bukkit event to check for matrix/result changes.
        net.minecraft.world.item.ItemStack result = CraftEventFactory.callPreCraftEvent(craftingContainer, craftResult, itemstack, container.getBukkitView(), recipe.map(RecipeHolder::value).orElse(null) instanceof RepairItemRecipe);

        return this.createItemCraftResult(recipe, CraftItemStack.asBukkitCopy(result), craftingContainer);
    }

    @Override
    public ItemStack craftItem(ItemStack[] craftingMatrix, World world) {
        return this.craftItemResult(craftingMatrix, world).getResult();
    }

    @Override
    public ItemCraftResult craftItemResult(ItemStack[] craftingMatrix, World world) {
        Preconditions.checkArgument(world != null, "world must not be null");

        CraftWorld craftWorld = (CraftWorld) world;

        // Create a players Crafting Inventory and get the recipe
        CraftingContainer craftingContainer = this.createCraftingContainer();

        Optional<RecipeHolder<CraftingRecipe>> recipe = this.getNMSRecipe(craftingMatrix, craftingContainer, craftWorld);

        // Generate the resulting ItemStack from the Crafting Matrix
        net.minecraft.world.item.ItemStack itemStack = net.minecraft.world.item.ItemStack.EMPTY;

        if (recipe.isPresent()) {
            itemStack = recipe.get().value().assemble(craftingContainer.asCraftInput(), craftWorld.getHandle().registryAccess());
        }

        return this.createItemCraftResult(recipe, CraftItemStack.asBukkitCopy(itemStack), craftingContainer);
    }

    private CraftItemCraftResult createItemCraftResult(Optional<RecipeHolder<CraftingRecipe>> recipe, ItemStack itemStack, CraftingContainer inventoryCrafting) {
        CraftItemCraftResult craftItemResult = new CraftItemCraftResult(itemStack);
        // tl;dr: this is an API adopted implementation of ResultSlot#onTake
        final CraftingInput.Positioned positionedCraftInput = inventoryCrafting.asPositionedCraftInput();
        final CraftingInput craftingInput = positionedCraftInput.input();
        recipe.map((holder) -> holder.value().getRemainingItems(craftingInput)).ifPresent((remainingItems) -> {
            // Set the resulting matrix items and overflow items
            for (int height = 0; height < craftingInput.height(); height++) {
                for (int width = 0; width < craftingInput.width(); width++) {
                    final int inventorySlot = width + positionedCraftInput.left() + (height + positionedCraftInput.top()) * inventoryCrafting.getWidth();
                    net.minecraft.world.item.ItemStack itemInMenu = inventoryCrafting.getItem(inventorySlot);
                    net.minecraft.world.item.ItemStack remainingItem = remainingItems.get(width + height * craftingInput.width());

                    if (!itemInMenu.isEmpty()) {
                        inventoryCrafting.removeItem(inventorySlot, 1);
                        itemInMenu = inventoryCrafting.getItem(inventorySlot);
                    }

                    if (!remainingItem.isEmpty()) {
                        if (itemInMenu.isEmpty()) {
                            inventoryCrafting.setItem(inventorySlot, remainingItem);
                        } else if (net.minecraft.world.item.ItemStack.isSameItemSameComponents(itemInMenu, remainingItem)) {
                            remainingItem.grow(itemInMenu.getCount());
                            inventoryCrafting.setItem(inventorySlot, remainingItem);
                        } else {
                            craftItemResult.getOverflowItems().add(CraftItemStack.asBukkitCopy(remainingItem));
                        }
                    }
                }
            }
        });

        for (int i = 0; i < inventoryCrafting.getContents().size(); i++) {
            craftItemResult.setResultMatrix(i, CraftItemStack.asBukkitCopy(inventoryCrafting.getItem(i)));
        }

        return craftItemResult;
    }

    private Optional<RecipeHolder<CraftingRecipe>> getNMSRecipe(ItemStack[] craftingMatrix, CraftingContainer inventoryCrafting, CraftWorld world) {
        Preconditions.checkArgument(craftingMatrix != null, "craftingMatrix must not be null");
        Preconditions.checkArgument(craftingMatrix.length == 9, "craftingMatrix must be an array of length 9");
        Preconditions.checkArgument(world != null, "world must not be null");

        for (int i = 0; i < craftingMatrix.length; i++) {
            inventoryCrafting.setItem(i, CraftItemStack.asNMSCopy(craftingMatrix[i]));
        }

        return this.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventoryCrafting.asCraftInput(), world.getHandle());
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return new RecipeIterator();
    }

    @Override
    public void clearRecipes() {
        this.console.getRecipeManager().clearRecipes();
    }

    @Override
    public void resetRecipes() {
        this.reloadData(); // Not ideal but hard to reload a subset of a resource pack
    }

    @Override
    public boolean removeRecipe(NamespacedKey recipeKey) {
        // Paper start - API for updating recipes on clients
        return this.removeRecipe(recipeKey, false);
    }

    @Override
    public boolean removeRecipe(NamespacedKey recipeKey, boolean resendRecipes) {
        // Paper end - API for updating recipes on clients
        Preconditions.checkArgument(recipeKey != null, "recipeKey == null");

        // Paper start - resend recipes on successful removal
        final ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> minecraftKey = CraftRecipe.toMinecraft(recipeKey);
        final boolean removed = this.getServer().getRecipeManager().removeRecipe(minecraftKey);
        if (removed/* && resendRecipes*/) { // TODO Always need to resend them rn - deprecate this method?
            this.playerList.reloadRecipes();
        }
        return removed;
        // Paper end - resend recipes on successful removal
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        ConfigurationSection section = this.commandsConfiguration.getConfigurationSection("aliases");
        Map<String, String[]> result = new LinkedHashMap<>();

        if (section != null) {
            for (String key : section.getKeys(false)) {
                List<String> commands;

                if (section.isList(key)) {
                    commands = section.getStringList(key);
                } else {
                    commands = ImmutableList.of(section.getString(key));
                }

                result.put(key, commands.toArray(new String[commands.size()]));
            }
        }

        return result;
    }

    @Override
    public net.kyori.adventure.text.Component shutdownMessage() {
        String msg = getShutdownMessage();
        return msg != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(msg) : null;
    }

    @Override
    @Deprecated // Paper
    public String getShutdownMessage() {
        return this.configuration.getString("settings.shutdown-message");
    }

    @Override
    public int getSpawnRadius() {
        return this.getServer().getSpawnProtectionRadius();
    }

    @Override
    public void setSpawnRadius(int value) {
        this.configuration.set("settings.spawn-radius", value);
        this.saveConfig();
    }

    @Override
    public boolean isEnforcingSecureProfiles() {
        return this.getServer().enforceSecureProfile();
    }

    @Override
    public boolean isAcceptingTransfers() {
        return this.getServer().acceptsTransfers();
    }

    @Override
    public boolean getHideOnlinePlayers() {
        return this.console.hidesOnlinePlayers();
    }

    @Override
    public boolean getOnlineMode() {
        return this.console.usesAuthentication();
    }

    @Override
    public @NotNull ServerConfiguration getServerConfig() {
        return serverConfig;
    }

    @Override
    public boolean getAllowFlight() {
        return this.console.isFlightAllowed();
    }

    @Override
    public boolean isHardcore() {
        return this.console.isHardcore();
    }

    public ChunkGenerator getGenerator(String world) {
        ConfigurationSection section = this.configuration.getConfigurationSection("worlds");
        ChunkGenerator result = null;

        if (section != null) {
            section = section.getConfigurationSection(world);

            if (section != null) {
                String name = section.getString("generator");

                if (name != null && !name.isEmpty()) {
                    String[] split = name.split(":", 2);
                    String id = (split.length > 1) ? split[1] : null;
                    Plugin plugin = this.pluginManager.getPlugin(split[0]);

                    if (plugin == null) {
                        this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
                    } else if (!plugin.isEnabled()) {
                        this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
                    } else {
                        try {
                            result = plugin.getDefaultWorldGenerator(world, id);
                            if (result == null) {
                                this.getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
                            }
                        } catch (Throwable t) {
                            plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
                        }
                    }
                }
            }
        }

        return result;
    }

    public BiomeProvider getBiomeProvider(String world) {
        ConfigurationSection section = this.configuration.getConfigurationSection("worlds");
        BiomeProvider result = null;

        if (section != null) {
            section = section.getConfigurationSection(world);

            if (section != null) {
                String name = section.getString("biome-provider");

                if (name != null && !name.isEmpty()) {
                    String[] split = name.split(":", 2);
                    String id = (split.length > 1) ? split[1] : null;
                    Plugin plugin = this.pluginManager.getPlugin(split[0]);

                    if (plugin == null) {
                        this.getLogger().severe("Could not set biome provider for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
                    } else if (!plugin.isEnabled()) {
                        this.getLogger().severe("Could not set biome provider for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
                    } else {
                        try {
                            result = plugin.getDefaultBiomeProvider(world, id);
                            if (result == null) {
                                this.getLogger().severe("Could not set biome provider for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world biome provider");
                            }
                        } catch (Throwable t) {
                            plugin.getLogger().log(Level.SEVERE, "Could not set biome provider for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public CraftMapView getMap(int id) {
        final net.minecraft.world.level.Level overworld = this.console.overworld();
        if (overworld == null) return null;

        final MapItemSavedData mapData = overworld.getMapData(new MapId(id));
        if (mapData == null) return null;

        return mapData.mapView;
    }

    @Override
    public CraftMapView createMap(World world) {
        Preconditions.checkArgument(world != null, "World cannot be null");

        ServerLevel level = ((CraftWorld) world).getHandle();
        // creates a new map at world spawn with the scale of 3, without tracking position and unlimited tracking
        BlockPos spawn = level.getLevelData().getSpawnPos();
        MapId newId = MapItem.createNewSavedData(level, spawn.getX(), spawn.getZ(), 3, false, false, level.dimension());
        return level.getMapData(newId).mapView;
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType) {
        return this.createExplorerMap(world, location, structureType, 100, true);
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType, int radius, boolean findUnexplored) {
        Preconditions.checkArgument(world != null, "World cannot be null");
        Preconditions.checkArgument(structureType != null, "StructureType cannot be null");
        Preconditions.checkArgument(structureType.getMapIcon() != null, "Cannot create explorer maps for StructureType %s", structureType.getName());

        ServerLevel level = ((CraftWorld) world).getHandle();
        Location structureLocation = world.locateNearestStructure(location, structureType, radius, findUnexplored);
        if (structureLocation == null) {
            throw new IllegalStateException("Could not find a structure for " + structureType);
        }
        BlockPos structurePos = CraftLocation.toBlockPosition(structureLocation);

        // Create map with trackingPosition = true, unlimitedTracking = true
        net.minecraft.world.item.ItemStack stack = MapItem.create(level, structurePos.getX(), structurePos.getZ(), MapView.Scale.NORMAL.getValue(), true, true);
        MapItem.renderBiomePreviewMap(level, stack);
        // "+" map ID taken from VillagerTrades$TreasureMapForEmeralds
        MapItemSavedData.addTargetDecoration(stack, structurePos, "+", CraftMapCursor.CraftType.bukkitToMinecraftHolder(structureType.getMapIcon()));

        return CraftItemStack.asBukkitCopy(stack);
    }

    // Paper start - copied from above (uses un-deprecated StructureType type)
    @Override
    public ItemStack createExplorerMap(World world, Location location, org.bukkit.generator.structure.StructureType structureType, org.bukkit.map.MapCursor.Type mapIcon, int radius, boolean findUnexplored) {
        Preconditions.checkArgument(world != null, "World cannot be null");
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(structureType != null, "StructureType cannot be null");
        Preconditions.checkArgument(mapIcon != null, "mapIcon cannot be null");

        ServerLevel level = ((CraftWorld) world).getHandle();
        final org.bukkit.util.StructureSearchResult structureSearchResult = world.locateNearestStructure(location, structureType, radius, findUnexplored);
        if (structureSearchResult == null) {
            return null;
        }
        Location structureLocation = structureSearchResult.getLocation();
        BlockPos structurePos = new BlockPos(structureLocation.getBlockX(), structureLocation.getBlockY(), structureLocation.getBlockZ());

        // Create map with trackingPosition = true, unlimitedTracking = true
        net.minecraft.world.item.ItemStack stack = MapItem.create(level, structurePos.getX(), structurePos.getZ(), MapView.Scale.NORMAL.getValue(), true, true);
        MapItem.renderBiomePreviewMap(level, stack);
        // "+" map ID taken from VillagerTrades$TreasureMapForEmeralds
        MapItemSavedData.addTargetDecoration(stack, structurePos, "+", CraftMapCursor.CraftType.bukkitToMinecraftHolder(mapIcon));

        return CraftItemStack.asBukkitCopy(stack);
    }
    // Paper end

    @Override
    public void shutdown() {
        this.console.halt(false);
    }

    @Override
    public int broadcast(net.kyori.adventure.text.Component message, String permission) {
        Set<CommandSender> recipients = new HashSet<>();
        for (Permissible permissible : this.getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender && !(permissible instanceof org.bukkit.command.BlockCommandSender) && permissible.hasPermission(permission)) { // Paper - Don't broadcast messages to command blocks
                recipients.add((CommandSender) permissible);
            }
        }

        BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(!Bukkit.isPrimaryThread(), message, recipients); // Paper - Adventure
        if (!broadcastMessageEvent.callEvent()) {
            return 0;
        }

        message = broadcastMessageEvent.message(); // Paper - Adventure

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    @Override
    public UUID getPlayerUniqueId(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null) {
            return player.getUniqueId();
        }
        GameProfile profile;
        // Only fetch an online UUID in online mode
        if (io.papermc.paper.configuration.GlobalConfiguration.get().proxies.isProxyOnlineMode()) {
            profile = console.getProfileCache().get(name).orElse(null);
        } else {
            // Make an OfflinePlayer using an offline mode UUID since the name has no profile
            profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8)), name);
        }
        return profile != null ? profile.getId() : null;
    }

    @Override
    @Deprecated
    public OfflinePlayer getOfflinePlayer(String name) {
        Preconditions.checkArgument(name != null, "name cannot be null");
        Preconditions.checkArgument(!name.isBlank(), "name cannot be empty");

        OfflinePlayer result = this.getPlayerExact(name);
        if (result == null) {
            GameProfile profile = null;
            // Only fetch an online UUID in online mode
            if (io.papermc.paper.configuration.GlobalConfiguration.get().proxies.isProxyOnlineMode()) { // Paper - Add setting for proxy online mode status
                // This is potentially blocking :(
                profile = this.console.getProfileCache().get(name).orElse(null);
            }

            if (profile == null) {
                // Make an OfflinePlayer using an offline mode UUID since the name has no profile
                result = this.getOfflinePlayer(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8)), name));
            } else {
                // Use the GameProfile even when we get a UUID so we ensure we still have a name
                result = this.getOfflinePlayer(profile);
            }
        } else {
            this.offlinePlayers.remove(result.getUniqueId());
        }

        return result;
    }

    @Override
    @Nullable
    public OfflinePlayer getOfflinePlayerIfCached(String name) {
        Preconditions.checkArgument(name != null, "Name cannot be null");
        Preconditions.checkArgument(!name.isEmpty(), "Name cannot be empty");

        OfflinePlayer result = getPlayerExact(name);
        if (result == null) {
            GameProfile profile = this.console.getProfileCache().getProfileIfCached(name);

            if (profile != null) {
                result = getOfflinePlayer(profile);
            }
        } else {
            this.offlinePlayers.remove(result.getUniqueId());
        }

        return result;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        Preconditions.checkArgument(id != null, "UUID id cannot be null");

        OfflinePlayer result = this.getPlayer(id);
        if (result == null) {
            result = this.offlinePlayers.get(id);
            if (result == null) {
                result = new CraftOfflinePlayer(this, new GameProfile(id, ""));
                this.offlinePlayers.put(id, result);
            }
        } else {
            this.offlinePlayers.remove(id);
        }

        return result;
    }

    @Override
    public PlayerProfile createPlayerProfile(UUID uniqueId, String name) {
        return new CraftPlayerProfile(uniqueId, name);
    }

    @Override
    public PlayerProfile createPlayerProfile(UUID uniqueId) {
        return new CraftPlayerProfile(uniqueId, null);
    }

    @Override
    public PlayerProfile createPlayerProfile(String name) {
        return new CraftPlayerProfile(null, name);
    }

    public OfflinePlayer getOfflinePlayer(GameProfile profile) {
        OfflinePlayer player = new CraftOfflinePlayer(this, profile);
        this.offlinePlayers.put(profile.getId(), player);
        return player;
    }

    @Override
    public Set<String> getIPBans() {
        return this.playerList.getIpBans().getEntries().stream().map(IpBanListEntry::getUser).collect(Collectors.toSet());
    }

    @Override
    public void banIP(String address) {
        Preconditions.checkArgument(address != null && !address.isBlank(), "Address cannot be null or blank.");

        this.getBanList(org.bukkit.BanList.Type.IP).addBan(address, null, null, null);
    }

    @Override
    public void unbanIP(String address) {
        Preconditions.checkArgument(address != null && !address.isBlank(), "Address cannot be null or blank.");

        this.getBanList(org.bukkit.BanList.Type.IP).pardon(address);
    }

    @Override
    public void banIP(InetAddress address) {
        Preconditions.checkArgument(address != null, "Address cannot be null.");

        ((CraftIpBanList) this.getBanList(BanList.Type.IP)).addBan(address, null, (Date) null, null);
    }

    @Override
    public void unbanIP(InetAddress address) {
        Preconditions.checkArgument(address != null, "Address cannot be null.");

        ((CraftIpBanList) this.getBanList(BanList.Type.IP)).pardon(address);
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        Set<OfflinePlayer> result = new HashSet<>();

        for (UserBanListEntry entry : this.playerList.getBans().getEntries()) {
            result.add(this.getOfflinePlayer(entry.getUser()));
        }

        return result;
    }

    @Override
    public <T extends BanList<?>> T getBanList(BanList.Type type) {
        Preconditions.checkArgument(type != null, "BanList.Type cannot be null");

        return switch (type) {
            case IP -> (T) new CraftIpBanList(this.playerList.getIpBans());
            case PROFILE, NAME -> (T) new CraftProfileBanList(this.playerList.getBans());
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <B extends BanList<E>, E> B getBanList(final io.papermc.paper.ban.BanListType<B> type) {
        Preconditions.checkArgument(type != null, "BanList.BanType cannot be null");
       if (type == io.papermc.paper.ban.BanListType.IP) {
           return (B) new CraftIpBanList(this.playerList.getIpBans());
       } else if (type == io.papermc.paper.ban.BanListType.PROFILE) {
          return (B) new CraftProfileBanList(this.playerList.getBans());
       } else {
           throw new IllegalArgumentException("Unknown BanListType: " + type);
       }
    }

    @Override
    public void setWhitelist(boolean value) {
        this.playerList.setUsingWhiteList(value);
        this.console.storeUsingWhiteList(value);
    }

    @Override
    public boolean isWhitelistEnforced() {
        return this.console.isEnforceWhitelist();
    }

    @Override
    public void setWhitelistEnforced(boolean value) {
        this.console.setEnforceWhitelist(value);
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        Set<OfflinePlayer> result = new LinkedHashSet<>();

        for (UserWhiteListEntry entry : this.playerList.getWhiteList().getEntries()) {
            result.add(this.getOfflinePlayer(entry.getUser()));
        }

        return result;
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        Set<OfflinePlayer> result = new HashSet<>();

        for (ServerOpListEntry entry : this.playerList.getOps().getEntries()) {
            result.add(this.getOfflinePlayer(entry.getUser()));
        }

        return result;
    }

    @Override
    public void reloadWhitelist() {
        this.playerList.reloadWhiteList();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(Optionull.mapOrDefault(
            this.console.getLevel(net.minecraft.world.level.Level.OVERWORLD),
            l -> l.serverLevelData.getGameType(),
            this.console.getProperties().gamemode
        ).getId());
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {
        Preconditions.checkArgument(mode != null, "GameMode cannot be null");

        for (World world : this.getWorlds()) {
            ((CraftWorld) world).getHandle().serverLevelData.setGameType(GameType.byId(mode.getValue()));
        }
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return this.console.console;
    }

    @Override
    public CommandSender createCommandSender(final java.util.function.Consumer<? super net.kyori.adventure.text.Component> feedback) {
        return new io.papermc.paper.commands.FeedbackForwardingSender(feedback, this);
    }

    public EntityMetadataStore getEntityMetadata() {
        return this.entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return this.playerMetadata;
    }

    public WorldMetadataStore getWorldMetadata() {
        return this.worldMetadata;
    }

    @Override
    public File getWorldContainer() {
        return this.getServer().storageSource.getDimensionPath(net.minecraft.world.level.Level.OVERWORLD).getParent().toFile();
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        PlayerDataStorage storage = this.console.playerDataStorage;
        String[] files = storage.getPlayerDir().list((dir, name) -> name.endsWith(".dat"));
        Set<OfflinePlayer> players = new HashSet<>();

        for (String file : files) {
            try {
                players.add(this.getOfflinePlayer(UUID.fromString(file.substring(0, file.length() - 4))));
            } catch (IllegalArgumentException ex) {
                // Who knows what is in this directory, just ignore invalid files
            }
        }

        players.addAll(this.getOnlinePlayers());

        return players.toArray(new OfflinePlayer[players.size()]);
    }

    @Override
    public Messenger getMessenger() {
        return this.messenger;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.getMessenger(), source, channel, message);

        for (Player player : this.getOnlinePlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<>();

        for (Player player : this.getOnlinePlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        Preconditions.checkArgument(type != null, "InventoryType cannot be null");
        Preconditions.checkArgument(type.isCreatable(), "InventoryType.%s cannot be used to create a inventory", type);
        return CraftInventoryCreator.INSTANCE.createInventory(owner, type);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, net.kyori.adventure.text.Component title) {
        Preconditions.checkArgument(type.isCreatable(), "Cannot open an inventory of type ", type);
        return CraftInventoryCreator.INSTANCE.createInventory(owner, type, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        Preconditions.checkArgument(type != null, "InventoryType cannot be null");
        Preconditions.checkArgument(type.isCreatable(), "InventoryType.%s cannot be used to create a inventory", type);
        Preconditions.checkArgument(title != null, "title cannot be null");
        return CraftInventoryCreator.INSTANCE.createInventory(owner, type, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        Preconditions.checkArgument(9 <= size && size <= 54 && size % 9 == 0, "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got %s)", size);
        return CraftInventoryCreator.INSTANCE.createInventory(owner, size);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, net.kyori.adventure.text.Component title) throws IllegalArgumentException {
        Preconditions.checkArgument(9 <= size && size <= 54 && size % 9 == 0, "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got " + size + ")");
        return CraftInventoryCreator.INSTANCE.createInventory(owner, size, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        Preconditions.checkArgument(9 <= size && size <= 54 && size % 9 == 0, "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got %s)", size);
        return CraftInventoryCreator.INSTANCE.createInventory(owner, size, title);
    }

    @Override
    public Merchant createMerchant(net.kyori.adventure.text.Component title) {
        return new org.bukkit.craftbukkit.inventory.CraftMerchantCustom(title == null ? InventoryType.MERCHANT.defaultTitle() : title);
    }

    @Override
    @Deprecated // Paper
    public Merchant createMerchant(String title) {
        return new CraftMerchantCustom(title == null ? InventoryType.MERCHANT.getDefaultTitle() : title);
    }

    @Override
    public @NotNull Merchant createMerchant() {
        return new CraftMerchantCustom();
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
        return this.getServer().getMaxChainedNeighborUpdates();
    }

    @Override
    public HelpMap getHelpMap() {
        return this.helpMap;
    }

    @Override
    public SimpleCommandMap getCommandMap() {
        return this.commandMap;
    }

    @Override
    @Deprecated
    public int getMonsterSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public int getAnimalSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.ANIMAL);
    }

    @Override
    @Deprecated
    public int getWaterAnimalSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public int getWaterAmbientSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public int getWaterUndergroundCreatureSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public int getAmbientSpawnLimit() {
        return this.getSpawnLimit(SpawnCategory.AMBIENT);
    }

    @Override
    public int getSpawnLimit(SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        Preconditions.checkArgument(CraftSpawnCategory.isValidForLimits(spawnCategory), "SpawnCategory." + spawnCategory + " does not have a spawn limit.");
        return this.getSpawnLimitUnsafe(spawnCategory);
    }

    public int getSpawnLimitUnsafe(final SpawnCategory spawnCategory) {
        return this.spawnCategoryLimit.getOrDefault(spawnCategory, -1);
    }

    @Override
    public boolean isPrimaryThread() {
        return ca.spottedleaf.moonrise.common.util.TickThread.isTickThread(); // Paper - rewrite chunk system
    }

    @Override
    public net.kyori.adventure.text.Component motd() {
        return this.console.motd();
    }

    @Override
    public void motd(final net.kyori.adventure.text.Component motd) {
        this.console.motd(motd);
    }

    @Override
    public String getMotd() {
        return this.console.getMotd();
    }

    @Override
    public void setMotd(String motd) {
        this.console.setMotd(motd);
    }

    @Override
    public ServerLinks getServerLinks() {
        return this.serverLinks;
    }

    @Override
    public WarningState getWarningState() {
        return this.warningState;
    }

    public List<String> tabComplete(CommandSender sender, String message, ServerLevel world, Vec3 pos, boolean forceCommand) {
        if (!(sender instanceof final Player player)) {
            return ImmutableList.of();
        }

        List<String> offers;
        if (message.startsWith("/") || forceCommand) {
            offers = this.tabCompleteCommand(player, message, world, pos);
        } else {
            offers = this.tabCompleteChat(player, message);
        }

        TabCompleteEvent tabEvent = new TabCompleteEvent(player, message, offers, message.startsWith("/") || forceCommand, pos != null ? CraftLocation.toBukkit(BlockPos.containing(pos), ((CraftWorld) player.getWorld()).getHandle()) : null); // Paper - AsyncTabCompleteEvent
        tabEvent.callEvent();

        return tabEvent.isCancelled() ? Collections.emptyList() : tabEvent.getCompletions();
    }

    public List<String> tabCompleteCommand(Player player, String message, ServerLevel world, Vec3 pos) {
        if ((org.spigotmc.SpigotConfig.tabComplete < 0 || message.length() <= org.spigotmc.SpigotConfig.tabComplete) && !message.contains(" ")) {
            return ImmutableList.of();
        }

        List<String> completions = null;
        try {
            if (message.startsWith("/")) {
                // Trim leading '/' if present (won't always be present in command blocks)
                message = message.substring(1);
            }
            if (pos == null) {
                completions = this.getCommandMap().tabComplete(player, message);
            } else {
                completions = this.getCommandMap().tabComplete(player, message, CraftLocation.toBukkit(pos, world.getWorld()));
            }
        } catch (CommandException ex) {
            player.sendMessage(Component.text("An internal error occurred while attempting to tab-complete this command", NamedTextColor.RED));
            this.getLogger().log(Level.SEVERE, "Exception when " + player.getName() + " attempted to tab complete " + message, ex);
        }

        return completions == null ? ImmutableList.<String>of() : completions;
    }

    public List<String> tabCompleteChat(Player player, String message) {
        List<String> completions = new ArrayList<>();
        PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
        String token = event.getLastToken();
        for (Player p : this.getOnlinePlayers()) {
            if (player.canSee(p) && StringUtil.startsWithIgnoreCase(p.getName(), token)) {
                completions.add(p.getName());
            }
        }
        event.callEvent();

        Iterator<?> it = completions.iterator();
        while (it.hasNext()) {
            Object current = it.next();
            if (!(current instanceof String)) {
                // Sanity
                it.remove();
            }
        }
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

    @Override
    public CraftItemFactory getItemFactory() {
        return CraftItemFactory.instance();
    }

    @Override
    public CraftEntityFactory getEntityFactory() {
        return CraftEntityFactory.instance();
    }

    @Override
    public CraftScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    @Override
    public Criteria getScoreboardCriteria(String name) {
        return CraftCriteria.getFromBukkit(name);
    }

    public void checkSaveState() {
        if (this.playerCommandState || this.printSaveWarning || this.console.autosavePeriod <= 0) {
            return;
        }
        this.printSaveWarning = true;
        this.getLogger().log(Level.WARNING, "A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.", this.warningState == WarningState.ON ? new Throwable() : null);
    }

    @Override
    public CraftIconCache getServerIcon() {
        return this.icon;
    }

    @Override
    public CraftIconCache loadServerIcon(File file) throws Exception {
        Preconditions.checkArgument(file != null, "File cannot be null");
        Preconditions.checkArgument(file.isFile(), "File (%s) is not a valid file", file);
        return CraftServer.loadServerIcon0(file);
    }

    static CraftIconCache loadServerIcon0(File file) throws Exception {
        return CraftServer.loadServerIcon0(ImageIO.read(file));
    }

    @Override
    public CraftIconCache loadServerIcon(BufferedImage image) throws Exception {
        Preconditions.checkArgument(image != null, "BufferedImage image cannot be null");
        return CraftServer.loadServerIcon0(image);
    }

    static CraftIconCache loadServerIcon0(BufferedImage image) throws Exception {
        Preconditions.checkArgument(image.getWidth() == 64, "BufferedImage must be 64 pixels wide (%s)", image.getWidth());
        Preconditions.checkArgument(image.getHeight() == 64, "BufferedImage must be 64 pixels high (%s)", image.getHeight());

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", arrayOutputStream);

        return new CraftIconCache(arrayOutputStream.toByteArray());
    }

    @Override
    public void setIdleTimeout(int threshold) {
        this.console.setPlayerIdleTimeout(threshold);
    }

    @Override
    public int getIdleTimeout() {
        return this.console.getPlayerIdleTimeout();
    }

    @Override
    public int getPauseWhenEmptyTime() {
        return this.getProperties().pauseWhenEmptySeconds;
    }

    @Override
    public void setPauseWhenEmptyTime(int seconds) {
        this.getProperties().pauseWhenEmptySeconds = seconds;
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        Preconditions.checkArgument(world != null, "World cannot be null");
        ServerLevel handle = ((CraftWorld) world).getHandle();
        return new OldCraftChunkData(world.getMinHeight(), world.getMaxHeight(), handle.registryAccess().lookupOrThrow(Registries.BIOME), world);
    }

    @Override
    public BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        return new CraftBossBar(title, color, style, flags);
    }

    @Override
    public KeyedBossBar createBossBar(NamespacedKey key, String title, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
        Preconditions.checkArgument(key != null, "NamespacedKey key cannot be null");
        Preconditions.checkArgument(barColor != null, "BarColor key cannot be null");
        Preconditions.checkArgument(barStyle != null, "BarStyle key cannot be null");

        CustomBossEvent bossBattleCustom = this.getServer().getCustomBossEvents().create(CraftNamespacedKey.toMinecraft(key), CraftChatMessage.fromString(title, true)[0]);
        CraftKeyedBossbar craftKeyedBossbar = new CraftKeyedBossbar(bossBattleCustom);
        craftKeyedBossbar.setColor(barColor);
        craftKeyedBossbar.setStyle(barStyle);
        for (BarFlag flag : barFlags) {
            if (flag == null) {
                continue;
            }
            craftKeyedBossbar.addFlag(flag);
        }

        return craftKeyedBossbar;
    }

    @Override
    public Iterator<KeyedBossBar> getBossBars() {
        return Iterators.unmodifiableIterator(Iterators.transform(
            this.getServer().getCustomBossEvents().getEvents().iterator(), CustomBossEvent::getBukkitEntity)
        );
    }

    @Override
    public KeyedBossBar getBossBar(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        net.minecraft.server.bossevents.CustomBossEvent bossBattleCustom = this.getServer().getCustomBossEvents().get(CraftNamespacedKey.toMinecraft(key));

        return (bossBattleCustom == null) ? null : bossBattleCustom.getBukkitEntity();
    }

    @Override
    public boolean removeBossBar(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        net.minecraft.server.bossevents.CustomBossEvents bossBattleCustomData = this.getServer().getCustomBossEvents();
        net.minecraft.server.bossevents.CustomBossEvent bossBattleCustom = bossBattleCustomData.get(CraftNamespacedKey.toMinecraft(key));

        if (bossBattleCustom != null) {
            bossBattleCustomData.remove(bossBattleCustom);
            return true;
        }

        return false;
    }

    @Override
    public Entity getEntity(UUID uuid) {
        Preconditions.checkArgument(uuid != null, "uuid cannot be null");

        for (ServerLevel world : this.getServer().getAllLevels()) {
            net.minecraft.world.entity.Entity entity = world.getEntity(uuid);
            if (entity != null) {
                return entity.getBukkitEntity();
            }
        }

        return null;
    }

    @Override
    public org.bukkit.advancement.Advancement getAdvancement(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "key cannot be null");

        AdvancementHolder advancement = this.console.getAdvancements().get(CraftNamespacedKey.toMinecraft(key));
        return (advancement == null) ? null : advancement.toBukkit();
    }

    @Override
    public Iterator<org.bukkit.advancement.Advancement> advancementIterator() {
        return Iterators.unmodifiableIterator(Iterators.transform(
            this.console.getAdvancements().getAllAdvancements().iterator(), AdvancementHolder::toBukkit)
        );
    }

    @Override
    public BlockData createBlockData(org.bukkit.Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");

        return this.createBlockData(material, (String) null);
    }

    @Override
    public BlockData createBlockData(org.bukkit.Material material, Consumer<? super BlockData> consumer) {
        BlockData data = this.createBlockData(material);

        if (consumer != null) {
            consumer.accept(data);
        }

        return data;
    }

    @Override
    public BlockData createBlockData(String data) throws IllegalArgumentException {
        Preconditions.checkArgument(data != null, "data cannot be null");

        return this.createBlockData(null, data);
    }

    @Override
    public BlockData createBlockData(org.bukkit.Material material, String data) {
        Preconditions.checkArgument(material != null || data != null, "Must provide one of material or data");
        BlockType type = null;
        if (material != null) {
            type = material.asBlockType();
            Preconditions.checkArgument(type != null, "Provided material must be a block");
        }

        return CraftBlockData.newData(type, data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Keyed> org.bukkit.Tag<T> getTag(String registry, NamespacedKey tag, Class<T> clazz) {
        Preconditions.checkArgument(registry != null, "registry cannot be null");
        Preconditions.checkArgument(tag != null, "NamespacedKey tag cannot be null");
        Preconditions.checkArgument(clazz != null, "Class clazz cannot be null");
        ResourceLocation key = CraftNamespacedKey.toMinecraft(tag);

        switch (registry) {
            case org.bukkit.Tag.REGISTRY_BLOCKS -> {
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Block namespace (%s) must have material type", clazz.getName());
                TagKey<Block> blockTagKey = TagKey.create(Registries.BLOCK, key);
                if (BuiltInRegistries.BLOCK.get(blockTagKey).isPresent()) {
                    return (org.bukkit.Tag<T>) new CraftBlockTag(BuiltInRegistries.BLOCK, blockTagKey);
                }
            }
            case org.bukkit.Tag.REGISTRY_ITEMS -> {
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Item namespace (%s) must have material type", clazz.getName());
                TagKey<Item> itemTagKey = TagKey.create(Registries.ITEM, key);
                if (BuiltInRegistries.ITEM.get(itemTagKey).isPresent()) {
                    return (org.bukkit.Tag<T>) new CraftItemTag(BuiltInRegistries.ITEM, itemTagKey);
                }
            }
            case org.bukkit.Tag.REGISTRY_FLUIDS -> {
                Preconditions.checkArgument(clazz == org.bukkit.Fluid.class, "Fluid namespace (%s) must have fluid type", clazz.getName());
                TagKey<Fluid> fluidTagKey = TagKey.create(Registries.FLUID, key);
                if (BuiltInRegistries.FLUID.get(fluidTagKey).isPresent()) {
                    return (org.bukkit.Tag<T>) new CraftFluidTag(BuiltInRegistries.FLUID, fluidTagKey);
                }
            }
            case org.bukkit.Tag.REGISTRY_ENTITY_TYPES -> {
                Preconditions.checkArgument(clazz == org.bukkit.entity.EntityType.class, "Entity type namespace (%s) must have entity type", clazz.getName());
                TagKey<EntityType<?>> entityTagKey = TagKey.create(Registries.ENTITY_TYPE, key);
                if (BuiltInRegistries.ENTITY_TYPE.get(entityTagKey).isPresent()) {
                    return (org.bukkit.Tag<T>) new CraftEntityTag(BuiltInRegistries.ENTITY_TYPE, entityTagKey);
                }
            }
            case org.bukkit.tag.DamageTypeTags.REGISTRY_DAMAGE_TYPES -> {
                Preconditions.checkArgument(clazz == org.bukkit.damage.DamageType.class, "Damage type namespace (%s) must have damage type", clazz.getName());
                TagKey<DamageType> damageTagKey = TagKey.create(Registries.DAMAGE_TYPE, key);
                net.minecraft.core.Registry<DamageType> damageRegistry = CraftRegistry.getMinecraftRegistry(Registries.DAMAGE_TYPE);
                if (damageRegistry.get(damageTagKey).isPresent()) {
                    return (org.bukkit.Tag<T>) new CraftDamageTag(damageRegistry, damageTagKey);
                }
            }
            case org.bukkit.Tag.REGISTRY_GAME_EVENTS -> {
                Preconditions.checkArgument(clazz == org.bukkit.GameEvent.class, "Game Event namespace must have GameEvent type");
                TagKey<net.minecraft.world.level.gameevent.GameEvent> gameEventTagKey = TagKey.create(net.minecraft.core.registries.Registries.GAME_EVENT, key);
                if (net.minecraft.core.registries.BuiltInRegistries.GAME_EVENT.get(gameEventTagKey).isPresent()) {
                    return (org.bukkit.Tag<T>) new CraftGameEventTag(net.minecraft.core.registries.BuiltInRegistries.GAME_EVENT, gameEventTagKey);
                }
            }
            default -> throw new IllegalArgumentException();
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Keyed> Iterable<org.bukkit.Tag<T>> getTags(String registry, Class<T> clazz) {
        Preconditions.checkArgument(registry != null, "registry cannot be null");
        Preconditions.checkArgument(clazz != null, "Class clazz cannot be null");
        switch (registry) {
            case org.bukkit.Tag.REGISTRY_BLOCKS -> {
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Block namespace (%s) must have material type", clazz.getName());
                net.minecraft.core.Registry<Block> blockTags = BuiltInRegistries.BLOCK;
                return blockTags.getTags().map(pair -> (org.bukkit.Tag<T>) new CraftBlockTag(blockTags, pair.key())).collect(ImmutableList.toImmutableList());
            }
            case org.bukkit.Tag.REGISTRY_ITEMS -> {
                Preconditions.checkArgument(clazz == org.bukkit.Material.class, "Item namespace (%s) must have material type", clazz.getName());
                net.minecraft.core.Registry<Item> itemTags = BuiltInRegistries.ITEM;
                return itemTags.getTags().map(pair -> (org.bukkit.Tag<T>) new CraftItemTag(itemTags, pair.key())).collect(ImmutableList.toImmutableList());
            }
            case org.bukkit.Tag.REGISTRY_FLUIDS -> {
                Preconditions.checkArgument(clazz == org.bukkit.Fluid.class, "Fluid namespace (%s) must have fluid type", clazz.getName());
                net.minecraft.core.Registry<Fluid> fluidTags = BuiltInRegistries.FLUID;
                return fluidTags.getTags().map(pair -> (org.bukkit.Tag<T>) new CraftFluidTag(fluidTags, pair.key())).collect(ImmutableList.toImmutableList());
            }
            case org.bukkit.Tag.REGISTRY_ENTITY_TYPES -> {
                Preconditions.checkArgument(clazz == org.bukkit.entity.EntityType.class, "Entity type namespace (%s) must have entity type", clazz.getName());
                net.minecraft.core.Registry<EntityType<?>> entityTags = BuiltInRegistries.ENTITY_TYPE;
                return entityTags.getTags().map(pair -> (org.bukkit.Tag<T>) new CraftEntityTag(entityTags, pair.key())).collect(ImmutableList.toImmutableList());
            }
            case org.bukkit.tag.DamageTypeTags.REGISTRY_DAMAGE_TYPES -> {
                Preconditions.checkArgument(clazz == org.bukkit.damage.DamageType.class, "Damage type namespace (%s) must have damage type", clazz.getName());
                net.minecraft.core.Registry<DamageType> damageTags = CraftRegistry.getMinecraftRegistry(Registries.DAMAGE_TYPE);
                return damageTags.getTags().map(pair -> (org.bukkit.Tag<T>) new CraftDamageTag(damageTags, pair.key())).collect(ImmutableList.toImmutableList());
            }
            case org.bukkit.Tag.REGISTRY_GAME_EVENTS -> {
                Preconditions.checkArgument(clazz == org.bukkit.GameEvent.class);
                net.minecraft.core.Registry<net.minecraft.world.level.gameevent.GameEvent> gameEvents = net.minecraft.core.registries.BuiltInRegistries.GAME_EVENT;
                return gameEvents.getTags().map(pair -> (org.bukkit.Tag<T>) new CraftGameEventTag(gameEvents, pair.key())).collect(ImmutableList.toImmutableList());
            }
            default -> throw new IllegalArgumentException();
        }
    }

    @Override
    public LootTable getLootTable(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey key cannot be null");

        ReloadableServerRegistries.Holder registry = this.getServer().reloadableRegistries();
        return registry.lookup().lookup(Registries.LOOT_TABLE)
                .flatMap((lookup) -> lookup.get(CraftLootTable.bukkitKeyToMinecraft(key)))
                .map((holder) -> new CraftLootTable(key, holder.value()))
                .orElse(null);
    }

    @Override
    public List<Entity> selectEntities(CommandSender sender, String selector) {
        Preconditions.checkArgument(selector != null, "selector cannot be null");
        Preconditions.checkArgument(sender != null, "CommandSender sender cannot be null");

        EntityArgument arg = EntityArgument.entities();
        List<? extends net.minecraft.world.entity.Entity> entities;

        try {
            StringReader reader = new StringReader(selector);
            entities = arg.parse(reader, true, true).findEntities(VanillaCommandWrapper.getListener(sender));
            Preconditions.checkArgument(!reader.canRead(), "Spurious trailing data in selector: %s", selector);
        } catch (CommandSyntaxException ex) {
            throw new IllegalArgumentException("Could not parse selector: " + selector, ex);
        }

        return new ArrayList<>(Lists.transform(entities, net.minecraft.world.entity.Entity::getBukkitEntity));
    }

    @Override
    public StructureManager getStructureManager() {
        return this.structureManager;
    }

    @Override
    public <T extends Keyed> Registry<T> getRegistry(Class<T> aClass) {
        return io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(aClass);
    }

    @Deprecated
    @Override
    public UnsafeValues getUnsafe() {
        return CraftMagicNumbers.INSTANCE;
    }

    @Override
    public long[] getTickTimes() {
        return this.getServer().tickTimes5s.getTimes();
    }

    @Override
    public double getAverageTickTime() {
        return this.getServer().tickTimes5s.getAverage();
    }

    private final org.bukkit.Server.Spigot spigot = new org.bukkit.Server.Spigot() {

        @Deprecated
        @Override
        public YamlConfiguration getConfig() {
            return org.spigotmc.SpigotConfig.config;
        }

        @Override
        public YamlConfiguration getBukkitConfig() {
            return configuration;
        }

        @Override
        public YamlConfiguration getSpigotConfig() {
            return org.spigotmc.SpigotConfig.config;
        }

        @Override
        public YamlConfiguration getPaperConfig() {
            return CraftServer.this.console.paperConfigurations.createLegacyObject(CraftServer.this.console);
        }

        @Override
        public void restart() {
            CraftServer.this.restart();
        }

        @Override
        public void broadcast(BaseComponent component) {
            for (Player player : CraftServer.this.getOnlinePlayers()) {
                player.spigot().sendMessage(component);
            }
        }

        @Override
        public void broadcast(BaseComponent... components) {
            for (Player player : CraftServer.this.getOnlinePlayers()) {
                player.spigot().sendMessage(components);
            }
        }
    };

    public org.bukkit.Server.Spigot spigot() {
        return this.spigot;
    }

    @Override
    public void restart() {
        org.spigotmc.RestartCommand.restart();
    }

    @Override
    public double[] getTPS() {
        return new double[] {
            net.minecraft.server.MinecraftServer.getServer().tps1.getAverage(),
            net.minecraft.server.MinecraftServer.getServer().tps5.getAverage(),
            net.minecraft.server.MinecraftServer.getServer().tps15.getAverage()
        };
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound) {
        if (sound.seed().isEmpty()) org.spigotmc.AsyncCatcher.catchOp("play sound; cannot generate seed with world random");
        final long seed = sound.seed().orElseGet(this.console.overworld().getRandom()::nextLong);
        for (ServerPlayer player : this.playerList.getPlayers()) {
            player.connection.send(io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, player.getX(), player.getY(), player.getZ(), seed, null));
        }
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound, final double x, final double y, final double z) {
        org.spigotmc.AsyncCatcher.catchOp("play sound");
        io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, x, y, z, sound.seed().orElseGet(this.console.overworld().getRandom()::nextLong), this.playSound0(x, y, z, this.console.getAllLevels()));
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound, final net.kyori.adventure.sound.Sound.Emitter emitter) {
        if (sound.seed().isEmpty()) org.spigotmc.AsyncCatcher.catchOp("play sound; cannot generate seed with world random");
        final long seed = sound.seed().orElseGet(this.console.overworld().getRandom()::nextLong);
        if (emitter == net.kyori.adventure.sound.Sound.Emitter.self()) {
            for (ServerPlayer player : this.playerList.getPlayers()) {
                player.connection.send(io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, player, seed, null));
            }
        } else if (emitter instanceof org.bukkit.craftbukkit.entity.CraftEntity craftEntity) {
            org.spigotmc.AsyncCatcher.catchOp("play sound; cannot use entity emitter");
            final net.minecraft.world.entity.Entity entity = craftEntity.getHandle();
            io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, entity, seed, this.playSound0(entity.getX(), entity.getY(), entity.getZ(), List.of((ServerLevel) entity.level())));
        } else {
            throw new IllegalArgumentException("Sound emitter must be an Entity or self(), but was: " + emitter);
        }
    }

    private java.util.function.BiConsumer<net.minecraft.network.protocol.Packet<?>, Float> playSound0(final double x, final double y, final double z, final Iterable<ServerLevel> levels) {
        return (packet, distance) -> {
            for (final ServerLevel level : levels) {
                level.getServer().getPlayerList().broadcast(null, x, y, z, distance, level.dimension(), packet);
            }
        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static java.nio.file.Path dumpHeap(java.nio.file.Path dir, String name) {
        try {
            java.nio.file.Files.createDirectories(dir);

            javax.management.MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
            java.nio.file.Path file;

            try {
                Class clazz = Class.forName("openj9.lang.management.OpenJ9DiagnosticsMXBean");
                Object openj9Mbean = java.lang.management.ManagementFactory.newPlatformMXBeanProxy(server, "openj9.lang.management:type=OpenJ9Diagnostics", clazz);
                java.lang.reflect.Method m = clazz.getMethod("triggerDumpToFile", String.class, String.class);
                file = dir.resolve(name + ".phd");
                m.invoke(openj9Mbean, "heap", file.toString());
            } catch (ClassNotFoundException e) {
                Class clazz = Class.forName("com.sun.management.HotSpotDiagnosticMXBean");
                Object hotspotMBean = java.lang.management.ManagementFactory.newPlatformMXBeanProxy(server, "com.sun.management:type=HotSpotDiagnostic", clazz);
                java.lang.reflect.Method m = clazz.getMethod("dumpHeap", String.class, boolean.class);
                file = dir.resolve(name + ".hprof");
                m.invoke(hotspotMBean, file.toString(), true);
            }

            return file;
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not write heap", t);
            return null;
        }
    }

    private Iterable<? extends net.kyori.adventure.audience.Audience> adventure$audiences;

    @Override
    public Iterable<? extends net.kyori.adventure.audience.Audience> audiences() {
        if (this.adventure$audiences == null) {
            this.adventure$audiences = com.google.common.collect.Iterables.concat(java.util.Collections.singleton(this.getConsoleSender()), this.getOnlinePlayers());
        }
        return this.adventure$audiences;
    }

    @Override
    public void reloadPermissions() {
        pluginManager.clearPermissions();
        if (io.papermc.paper.configuration.GlobalConfiguration.get().misc.loadPermissionsYmlBeforePlugins) loadCustomPermissions();
        for (Plugin plugin : pluginManager.getPlugins()) {
            for (Permission perm : plugin.getDescription().getPermissions()) {
                try {
                    pluginManager.addPermission(perm);
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                }
            }
        }
        if (!io.papermc.paper.configuration.GlobalConfiguration.get().misc.loadPermissionsYmlBeforePlugins) loadCustomPermissions();
        DefaultPermissions.registerCorePermissions();
        CraftDefaultPermissions.registerCorePermissions();
    }

    @Override
    public boolean reloadCommandAliases() {
        Set<String> removals = getCommandAliases().keySet().stream()
                .map(key -> key.toLowerCase(java.util.Locale.ENGLISH))
                .collect(java.util.stream.Collectors.toSet());
        getCommandMap().getKnownCommands().keySet().removeIf(removals::contains);
        File file = getCommandsConfigFile();
        try {
            commandsConfiguration.load(file);
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
            return false;
        }
        commandMap.registerServerAliases();
        return true;
    }

    @Override
    public boolean suggestPlayerNamesWhenNullTabCompletions() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().commands.suggestPlayerNamesWhenNullTabCompletions;
    }

    @Override
    public String getPermissionMessage() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand().serialize(io.papermc.paper.configuration.GlobalConfiguration.get().messages.noPermission);
    }

    @Override
    public net.kyori.adventure.text.Component permissionMessage() {
        return io.papermc.paper.configuration.GlobalConfiguration.get().messages.noPermission;
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfile(@NotNull UUID uuid) {
        return createProfile(uuid, null);
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfile(@NotNull String name) {
        return createProfile(null, name);
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
        Player player = uuid != null ? Bukkit.getPlayer(uuid) : (name != null ? Bukkit.getPlayerExact(name) : null);
        if (player != null) return new com.destroystokyo.paper.profile.CraftPlayerProfile((CraftPlayer) player);

        return new com.destroystokyo.paper.profile.CraftPlayerProfile(uuid, name);
    }

    @Override
    public com.destroystokyo.paper.profile.PlayerProfile createProfileExact(@Nullable UUID uuid, @Nullable String name) {
        Player player = uuid != null ? Bukkit.getPlayer(uuid) : (name != null ? Bukkit.getPlayerExact(name) : null);
        if (player == null) {
            return new com.destroystokyo.paper.profile.CraftPlayerProfile(uuid, name);
        }

        if (java.util.Objects.equals(uuid, player.getUniqueId()) && java.util.Objects.equals(name, player.getName())) {
            return new com.destroystokyo.paper.profile.CraftPlayerProfile((CraftPlayer) player);
        }

        final com.destroystokyo.paper.profile.CraftPlayerProfile profile = new com.destroystokyo.paper.profile.CraftPlayerProfile(uuid, name);
        profile.getGameProfile().getProperties().putAll(((CraftPlayer) player).getHandle().getGameProfile().getProperties());
        return profile;
    }

    @Override
    public int getCurrentTick() {
        return net.minecraft.server.MinecraftServer.currentTick;
    }

    @Override
    public boolean isStopping() {
        return net.minecraft.server.MinecraftServer.getServer().hasStopped();
    }

    private com.destroystokyo.paper.entity.ai.MobGoals mobGoals = new com.destroystokyo.paper.entity.ai.PaperMobGoals();

    @Override
    public com.destroystokyo.paper.entity.ai.MobGoals getMobGoals() {
        return mobGoals;
    }

    @Override
    public io.papermc.paper.datapack.PaperDatapackManager getDatapackManager() {
        return datapackManager;
    }

    @Override
    public io.papermc.paper.potion.PaperPotionBrewer getPotionBrewer() {
        return this.potionBrewer;
    }

    @Override
    public boolean isPaused() {
        return this.console.isTickPaused();
    }

    @Override
    public void allowPausing(final Plugin plugin, final boolean value) {
        this.console.addPluginAllowingSleep(plugin.getName(), value);
    }
}
