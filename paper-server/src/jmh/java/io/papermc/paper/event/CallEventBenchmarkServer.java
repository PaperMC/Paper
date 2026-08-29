package io.papermc.paper.event;

import io.papermc.paper.configuration.ServerConfiguration;
import io.papermc.paper.plugin.manager.PaperPluginManagerImpl;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.BanList;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.ServerLinks;
import org.bukkit.ServerTickManager;
import org.bukkit.StructureType;
import org.bukkit.Tag;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.util.Versioning;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityFactory;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemCraftResult;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.packs.ResourcePack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CallEventBenchmarkServer implements Server {

    private final Thread primaryThread = Thread.currentThread();
    private final Logger logger = Logger.getLogger(CallEventBenchmarkServer.class.getCanonicalName());
    private final Server.Spigot spigot = new Server.Spigot();
    private final SimpleCommandMap commandMap = new SimpleCommandMap(this, new HashMap<>());
    private final SimplePluginManager pluginManager = new SimplePluginManager(this, this.commandMap);
    private final PaperPluginManagerImpl paperPluginManager = new PaperPluginManagerImpl(this, this.commandMap, this.pluginManager);

    private CallEventBenchmarkServer() {
        // Match CraftServer's event-dispatch wiring.
        this.pluginManager.paperPluginManager = this.paperPluginManager;
    }

    static Server setup() {
        return new CallEventBenchmarkServer();
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException("Unsupported Server method in CallEventBenchmarkServer");
    }

    private String benchmarkVersion() {
        final String implementationVersion = CallEventBenchmarkServer.class.getPackage().getImplementationVersion();
        return implementationVersion != null ? implementationVersion : "benchmark";
    }

    private String benchmarkMinecraftVersion() {
        return Versioning.getBukkitVersion().split("-")[0];
    }

    @Override
    public @NotNull PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    @org.jetbrains.annotations.ApiStatus.Internal
    public @NotNull Logger getLogger() {
        return this.logger;
    }

    @Override
    public @NotNull String getName() {
        return "Paper";
    }

    @Override
    public @NotNull String getVersion() {
        return this.benchmarkVersion();
    }

    @Override
    public @NotNull String getBukkitVersion() {
        return Versioning.getBukkitVersion();
    }

    @Override
    public @NotNull String getMinecraftVersion() {
        return this.benchmarkMinecraftVersion();
    }

    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread() == this.primaryThread;
    }

    @Override
    public boolean isStopping() {
        return false;
    }

    @Override
    public @NotNull WarningState getWarningState() {
        return WarningState.DEFAULT;
    }

    @Override
    @Deprecated(since = "1.21.4", forRemoval = true)
    public @NotNull Server.Spigot spigot() {
        return this.spigot;
    }

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {
        return Set.of();
    }

    @Override
    public @NotNull Iterable<? extends net.kyori.adventure.audience.Audience> audiences() {
        return List.of();
    }

    @Override
    public @NotNull File getPluginsFolder() {
        throw this.unsupported();
    }

    @Override
    public @NotNull Collection<? extends Player> getOnlinePlayers() {
        throw this.unsupported();
    }

    @Override
    public int getMaxPlayers() {
        throw this.unsupported();
    }

    @Override
    public void setMaxPlayers(final int maxPlayers) {
        throw this.unsupported();
    }

    @Override
    public int getPort() {
        throw this.unsupported();
    }

    @Override
    public int getViewDistance() {
        throw this.unsupported();
    }

    @Override
    public int getSimulationDistance() {
        throw this.unsupported();
    }

    @Override
    public @NotNull String getIp() {
        throw this.unsupported();
    }

    @Override
    public @NotNull String getWorldType() {
        throw this.unsupported();
    }

    @Override
    public boolean getGenerateStructures() {
        throw this.unsupported();
    }

    @Override
    public int getMaxWorldSize() {
        throw this.unsupported();
    }

    @Override
    public boolean getAllowEnd() {
        throw this.unsupported();
    }

    @Override
    public boolean getAllowNether() {
        throw this.unsupported();
    }

    @Override
    public boolean isLoggingIPs() {
        throw this.unsupported();
    }

    @Override
    public @NotNull List<String> getInitialEnabledPacks() {
        throw this.unsupported();
    }

    @Override
    public @NotNull List<String> getInitialDisabledPacks() {
        throw this.unsupported();
    }

    @Override
    public @NotNull ServerTickManager getServerTickManager() {
        throw this.unsupported();
    }

    @Override
    public @Nullable ResourcePack getServerResourcePack() {
        throw this.unsupported();
    }

    @Override
    public @NotNull String getResourcePack() {
        throw this.unsupported();
    }

    @Override
    public @NotNull String getResourcePackHash() {
        throw this.unsupported();
    }

    @Override
    public @NotNull String getResourcePackPrompt() {
        throw this.unsupported();
    }

    @Override
    public boolean isResourcePackRequired() {
        throw this.unsupported();
    }

    @Override
    public boolean hasWhitelist() {
        throw this.unsupported();
    }

    @Override
    public void setWhitelist(final boolean value) {
        throw this.unsupported();
    }

    @Override
    public boolean isWhitelistEnforced() {
        throw this.unsupported();
    }

    @Override
    public void setWhitelistEnforced(final boolean value) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Set<OfflinePlayer> getWhitelistedPlayers() {
        throw this.unsupported();
    }

    @Override
    public void reloadWhitelist() {
        throw this.unsupported();
    }

    @Override
    public @NotNull String getUpdateFolder() {
        throw this.unsupported();
    }

    @Override
    public @NotNull File getUpdateFolderFile() {
        throw this.unsupported();
    }

    @Override
    public long getConnectionThrottle() {
        throw this.unsupported();
    }

    @Override
    public int getTicksPerSpawns(@NotNull final SpawnCategory spawnCategory) {
        throw this.unsupported();
    }

    @Override
    public @Nullable Player getPlayer(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @Nullable Player getPlayerExact(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @NotNull List<Player> matchPlayer(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @Nullable Player getPlayer(@NotNull final UUID id) {
        throw this.unsupported();
    }

    @Override
    public @Nullable UUID getPlayerUniqueId(@NotNull final String playerName) {
        throw this.unsupported();
    }

    @Override
    public @NotNull BukkitScheduler getScheduler() {
        throw this.unsupported();
    }

    @Override
    public @NotNull ServicesManager getServicesManager() {
        throw this.unsupported();
    }

    @Override
    public @NotNull List<World> getWorlds() {
        throw this.unsupported();
    }

    @Override
    public boolean isTickingWorlds() {
        throw this.unsupported();
    }

    @Override
    public @Nullable World createWorld(@NotNull final WorldCreator creator) {
        throw this.unsupported();
    }

    @Override
    public boolean unloadWorld(@NotNull final String name, final boolean save) {
        throw this.unsupported();
    }

    @Override
    public boolean unloadWorld(@NotNull final World world, final boolean save) {
        throw this.unsupported();
    }

    @Override
    public @NotNull World getRespawnWorld() {
        throw this.unsupported();
    }

    @Override
    public void setRespawnWorld(@NotNull final World world) {
        throw this.unsupported();
    }

    @Override
    @ApiStatus.Obsolete
    public @Nullable World getWorld(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @Nullable World getWorld(@NotNull final UUID uid) {
        throw this.unsupported();
    }

    @Override
    public @Nullable World getWorld(@NotNull final net.kyori.adventure.key.Key worldKey) {
        throw this.unsupported();
    }

    @Override
    public @NotNull WorldBorder createWorldBorder() {
        throw this.unsupported();
    }

    @Override
    public @Nullable MapView getMap(final int id) {
        throw this.unsupported();
    }

    @Override
    public @NotNull MapView createMap(@NotNull final World world) {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public @NotNull ItemStack createExplorerMap(@NotNull final World world, @NotNull final Location location, @NotNull final StructureType structureType, final int radius, final boolean findUnexplored) {
        throw this.unsupported();
    }

    @Override
    public @Nullable ItemStack createExplorerMap(@NotNull final World world, @NotNull final Location location, @NotNull final org.bukkit.generator.structure.StructureType structureType, @NotNull final org.bukkit.map.MapCursor.Type mapIcon, final int radius, final boolean findUnexplored) {
        throw this.unsupported();
    }

    @Override
    public void reload() {
        throw this.unsupported();
    }

    @Override
    public void reloadData() {
        throw this.unsupported();
    }

    @Override
    public void updateResources() {
        throw this.unsupported();
    }

    @Override
    public void updateRecipes() {
        throw this.unsupported();
    }

    @Override
    public @Nullable PluginCommand getPluginCommand(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public void savePlayers() {
        throw this.unsupported();
    }

    @Override
    public boolean dispatchCommand(@NotNull final CommandSender sender, @NotNull final String commandLine) throws CommandException {
        throw this.unsupported();
    }

    @Override
    @Contract("null, _ -> false")
    public boolean addRecipe(@Nullable final Recipe recipe, final boolean resendRecipes) {
        throw this.unsupported();
    }

    @Override
    public @NotNull List<Recipe> getRecipesFor(@NotNull final ItemStack result) {
        throw this.unsupported();
    }

    @Override
    public @Nullable Recipe getRecipe(@NotNull final NamespacedKey recipeKey) {
        throw this.unsupported();
    }

    @Override
    public @Nullable Recipe getCraftingRecipe(@NotNull final ItemStack @NotNull [] craftingMatrix, @NotNull final World world) {
        throw this.unsupported();
    }

    @Override
    public @NotNull ItemCraftResult craftItemResult(@NotNull final ItemStack @NotNull [] craftingMatrix, @NotNull final World world, @NotNull final Player player) {
        throw this.unsupported();
    }

    @Override
    public @NotNull ItemCraftResult craftItemResult(@NotNull final ItemStack @NotNull [] craftingMatrix, @NotNull final World world) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Iterator<Recipe> recipeIterator() {
        throw this.unsupported();
    }

    @Override
    public void clearRecipes() {
        throw this.unsupported();
    }

    @Override
    public void resetRecipes() {
        throw this.unsupported();
    }

    @Override
    public boolean removeRecipe(@NotNull final NamespacedKey key, final boolean resendRecipes) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Map<String, String[]> getCommandAliases() {
        throw this.unsupported();
    }

    @Override
    public int getSpawnRadius() {
        throw this.unsupported();
    }

    @Override
    public void setSpawnRadius(final int value) {
        throw this.unsupported();
    }

    @Override
    public boolean isEnforcingSecureProfiles() {
        throw this.unsupported();
    }

    @Override
    public boolean isAcceptingTransfers() {
        throw this.unsupported();
    }

    @Override
    public boolean getHideOnlinePlayers() {
        throw this.unsupported();
    }

    @Override
    public boolean getOnlineMode() {
        throw this.unsupported();
    }

    @Override
    public @NotNull ServerConfiguration getServerConfig() {
        throw this.unsupported();
    }

    @Override
    public boolean getAllowFlight() {
        throw this.unsupported();
    }

    @Override
    public boolean isHardcore() {
        throw this.unsupported();
    }

    @Override
    public void shutdown() {
        throw this.unsupported();
    }

    @Override
    public int broadcast(final net.kyori.adventure.text.@NotNull Component message, @NotNull final String permission) {
        throw this.unsupported();
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayerIfCached(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull final UUID id) {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.18.1")
    public @NotNull PlayerProfile createPlayerProfile(@Nullable final UUID uniqueId, @Nullable final String name) {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.18.1")
    public @NotNull PlayerProfile createPlayerProfile(@NotNull final UUID uniqueId) {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.18.1")
    public @NotNull PlayerProfile createPlayerProfile(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Set<String> getIPBans() {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.20.1")
    public void banIP(@NotNull final String address) {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.20.1")
    public void unbanIP(@NotNull final String address) {
        throw this.unsupported();
    }

    @Override
    public void banIP(@NotNull final InetAddress address) {
        throw this.unsupported();
    }

    @Override
    public void unbanIP(@NotNull final InetAddress address) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Set<OfflinePlayer> getBannedPlayers() {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public <T extends BanList<?>> @NotNull T getBanList(@NotNull final BanList.Type type) {
        throw this.unsupported();
    }

    @Override
    public <B extends BanList<E>, E> @NotNull B getBanList(@NotNull final io.papermc.paper.ban.BanListType<B> type) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Set<OfflinePlayer> getOperators() {
        throw this.unsupported();
    }

    @Override
    public @NotNull GameMode getDefaultGameMode() {
        throw this.unsupported();
    }

    @Override
    public void setDefaultGameMode(@NotNull final GameMode mode) {
        throw this.unsupported();
    }

    @Override
    public boolean forcesDefaultGameMode() {
        throw this.unsupported();
    }

    @Override
    public @NotNull ConsoleCommandSender getConsoleSender() {
        throw this.unsupported();
    }

    @Override
    public @NotNull CommandSender createCommandSender(final @NotNull Consumer<? super net.kyori.adventure.text.Component> feedback) {
        throw this.unsupported();
    }

    @Override
    @ApiStatus.Obsolete
    public @NotNull File getWorldContainer() {
        throw this.unsupported();
    }

    @Override
    public @NotNull Path getLevelDirectory() {
        throw this.unsupported();
    }

    @Override
    public @NotNull OfflinePlayer @NotNull [] getOfflinePlayers() {
        throw this.unsupported();
    }

    @Override
    public @NotNull Messenger getMessenger() {
        throw this.unsupported();
    }

    @Override
    public @NotNull HelpMap getHelpMap() {
        throw this.unsupported();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable final InventoryHolder owner, @NotNull final InventoryType type) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable final InventoryHolder owner, @NotNull final InventoryType type, final net.kyori.adventure.text.@NotNull Component title) {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public @NotNull Inventory createInventory(@Nullable final InventoryHolder owner, @NotNull final InventoryType type, @NotNull final String title) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable final InventoryHolder owner, final int size) throws IllegalArgumentException {
        throw this.unsupported();
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable final InventoryHolder owner, final int size, final net.kyori.adventure.text.@NotNull Component title) throws IllegalArgumentException {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public @NotNull Inventory createInventory(@Nullable final InventoryHolder owner, final int size, @NotNull final String title) throws IllegalArgumentException {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.21.4")
    public @NotNull Merchant createMerchant(final net.kyori.adventure.text.@Nullable Component title) {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public @NotNull Merchant createMerchant(@Nullable final String title) {
        throw this.unsupported();
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
        throw this.unsupported();
    }

    @Override
    public @NotNull Merchant createMerchant() {
        throw this.unsupported();
    }

    @Override
    public int getSpawnLimit(@NotNull final SpawnCategory spawnCategory) {
        throw this.unsupported();
    }

    @Override
    public net.kyori.adventure.text.@NotNull Component motd() {
        throw this.unsupported();
    }

    @Override
    public void motd(final net.kyori.adventure.text.@NotNull Component motd) {
        throw this.unsupported();
    }

    @Override
    public net.kyori.adventure.text.@Nullable Component shutdownMessage() {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public @NotNull String getMotd() {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public void setMotd(@NotNull final String motd) {
        throw this.unsupported();
    }

    @Override
    public @NotNull ServerLinks getServerLinks() {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public @Nullable String getShutdownMessage() {
        throw this.unsupported();
    }

    @Override
    public @NotNull ItemFactory getItemFactory() {
        throw this.unsupported();
    }

    @Override
    public @NotNull EntityFactory getEntityFactory() {
        throw this.unsupported();
    }

    @Override
    public @NotNull ScoreboardManager getScoreboardManager() {
        throw this.unsupported();
    }

    @Override
    public @NotNull Criteria getScoreboardCriteria(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @Nullable CachedServerIcon getServerIcon() {
        throw this.unsupported();
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull final File file) throws IllegalArgumentException, Exception {
        throw this.unsupported();
    }

    @Override
    public @NotNull CachedServerIcon loadServerIcon(@NotNull final BufferedImage image) throws IllegalArgumentException, Exception {
        throw this.unsupported();
    }

    @Override
    public void setIdleTimeout(final int threshold) {
        throw this.unsupported();
    }

    @Override
    public int getIdleTimeout() {
        throw this.unsupported();
    }

    @Override
    public int getPauseWhenEmptyTime() {
        throw this.unsupported();
    }

    @Override
    public void setPauseWhenEmptyTime(final int seconds) {
        throw this.unsupported();
    }

    @Override
    public @NotNull ChunkGenerator.ChunkData createChunkData(@NotNull final World world) {
        throw this.unsupported();
    }

    @Override
    public @NotNull BossBar createBossBar(@Nullable final String title, @NotNull final BarColor color, @NotNull final BarStyle style, @NotNull final BarFlag... flags) {
        throw this.unsupported();
    }

    @Override
    public @NotNull KeyedBossBar createBossBar(@NotNull final NamespacedKey key, @Nullable final String title, @NotNull final BarColor color, @NotNull final BarStyle style, @NotNull final BarFlag... flags) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Iterator<KeyedBossBar> getBossBars() {
        throw this.unsupported();
    }

    @Override
    public @Nullable KeyedBossBar getBossBar(@NotNull final NamespacedKey key) {
        throw this.unsupported();
    }

    @Override
    public boolean removeBossBar(@NotNull final NamespacedKey key) {
        throw this.unsupported();
    }

    @Override
    public @Nullable Entity getEntity(@NotNull final UUID uuid) {
        throw this.unsupported();
    }

    @Override
    public double @NotNull [] getTPS() {
        throw this.unsupported();
    }

    @Override
    public long @NotNull [] getTickTimes() {
        throw this.unsupported();
    }

    @Override
    public double getAverageTickTime() {
        throw this.unsupported();
    }

    @Override
    public @NotNull org.bukkit.command.CommandMap getCommandMap() {
        throw this.unsupported();
    }

    @Override
    public @Nullable Advancement getAdvancement(@NotNull final NamespacedKey key) {
        throw this.unsupported();
    }

    @Override
    public @NotNull Iterator<Advancement> advancementIterator() {
        throw this.unsupported();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull final Material material) {
        throw this.unsupported();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull final Material material, @Nullable final Consumer<? super BlockData> consumer) {
        throw this.unsupported();
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull final String data) throws IllegalArgumentException {
        throw this.unsupported();
    }

    @Override
    @Contract("null, null -> fail")
    public @NotNull BlockData createBlockData(@Nullable final Material material, @Nullable final String data) throws IllegalArgumentException {
        throw this.unsupported();
    }

    @Override
    public <T extends Keyed> @Nullable Tag<T> getTag(@NotNull final String registry, @NotNull final NamespacedKey tag, @NotNull final Class<T> clazz) {
        throw this.unsupported();
    }

    @Override
    public <T extends Keyed> @NotNull Iterable<Tag<T>> getTags(@NotNull final String registry, @NotNull final Class<T> clazz) {
        throw this.unsupported();
    }

    @Override
    public @Nullable LootTable getLootTable(@NotNull final NamespacedKey key) {
        throw this.unsupported();
    }

    @Override
    public @NotNull List<Entity> selectEntities(@NotNull final CommandSender sender, @NotNull final String selector) throws IllegalArgumentException {
        throw this.unsupported();
    }

    @Override
    public @NotNull StructureManager getStructureManager() {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.20.6")
    public <T extends Keyed> @Nullable Registry<T> getRegistry(@NotNull final Class<T> tClass) {
        throw this.unsupported();
    }

    @Override
    @Deprecated(since = "1.7.2")
    public @NotNull UnsafeValues getUnsafe() {
        throw this.unsupported();
    }

    @Override
    public void restart() {
        throw this.unsupported();
    }

    @Override
    public void reloadPermissions() {
        throw this.unsupported();
    }

    @Override
    public boolean reloadCommandAliases() {
        throw this.unsupported();
    }

    @Override
    public boolean suggestPlayerNamesWhenNullTabCompletions() {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public @NotNull String getPermissionMessage() {
        throw this.unsupported();
    }

    @Override
    public @NotNull net.kyori.adventure.text.Component permissionMessage() {
        throw this.unsupported();
    }

    @Override
    public @NotNull com.destroystokyo.paper.profile.PlayerProfile createProfile(@NotNull final UUID uuid) {
        throw this.unsupported();
    }

    @Override
    public @NotNull com.destroystokyo.paper.profile.PlayerProfile createProfile(@NotNull final String name) {
        throw this.unsupported();
    }

    @Override
    public @NotNull com.destroystokyo.paper.profile.PlayerProfile createProfile(@Nullable final UUID uuid, @Nullable final String name) {
        throw this.unsupported();
    }

    @Override
    public @NotNull com.destroystokyo.paper.profile.PlayerProfile createProfileExact(@Nullable final UUID uuid, @Nullable final String name) {
        throw this.unsupported();
    }

    @Override
    public int getCurrentTick() {
        throw this.unsupported();
    }

    @Override
    public @NotNull com.destroystokyo.paper.entity.ai.MobGoals getMobGoals() {
        throw this.unsupported();
    }

    @Override
    public @NotNull io.papermc.paper.datapack.DatapackManager getDatapackManager() {
        throw this.unsupported();
    }

    @Override
    public @NotNull org.bukkit.potion.PotionBrewer getPotionBrewer() {
        throw this.unsupported();
    }

    @Override
    public @NotNull io.papermc.paper.threadedregions.scheduler.RegionScheduler getRegionScheduler() {
        throw this.unsupported();
    }

    @Override
    public @NotNull io.papermc.paper.threadedregions.scheduler.AsyncScheduler getAsyncScheduler() {
        throw this.unsupported();
    }

    @Override
    public @NotNull io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler getGlobalRegionScheduler() {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final World world, @NotNull final io.papermc.paper.math.Position position) {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final World world, @NotNull final io.papermc.paper.math.Position position, final int squareRadiusChunks) {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final Location location) {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final Location location, final int squareRadiusChunks) {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final World world, final int chunkX, final int chunkZ) {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final World world, final int chunkX, final int chunkZ, final int squareRadiusChunks) {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final World world, final int minChunkX, final int minChunkZ, final int maxChunkX, final int maxChunkZ) {
        throw this.unsupported();
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull final Entity entity) {
        throw this.unsupported();
    }

    @Override
    public boolean isGlobalTickThread() {
        throw this.unsupported();
    }

    @Override
    public boolean isPaused() {
        throw this.unsupported();
    }

    @Override
    public void allowPausing(@NotNull final Plugin plugin, final boolean value) {
        throw this.unsupported();
    }

    @Override
    public void sendPluginMessage(@NotNull final Plugin source, @NotNull final String channel, final byte @NotNull [] message) {
        throw this.unsupported();
    }
}
