package org.bukkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Warning.WarningState;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import com.avaje.ebean.config.ServerConfig;

/**
 * Represents the Bukkit core, for version and Server singleton handling
 */
public final class Bukkit {
    private static Server server;

    /**
     * Static class cannot be initialized.
     */
    private Bukkit() {}

    /**
     * Gets the current {@link Server} singleton
     *
     * @return Server instance being ran
     */
    public static Server getServer() {
        return server;
    }

    /**
     * Attempts to set the {@link Server} singleton.
     * <p>
     * This cannot be done if the Server is already set.
     *
     * @param server Server instance
     */
    public static void setServer(Server server) {
        if (Bukkit.server != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");
        }

        Bukkit.server = server;
        server.getLogger().info("This server is running " + getName() + " version " + getVersion() + " (Implementing API version " + getBukkitVersion() + ")");
    }

    /**
     * @see Server#getName()
     */
    public static String getName() {
        return server.getName();
    }

    /**
     * @see Server#getVersion()
     */
    public static String getVersion() {
        return server.getVersion();
    }

    /**
     * @see Server#getBukkitVersion()
     */
    public static String getBukkitVersion() {
        return server.getBukkitVersion();
    }

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @Deprecated
     * @see Server#_INVALID_getOnlinePlayers()
     */
    @Deprecated
    public static Player[] _INVALID_getOnlinePlayers() {
        return server._INVALID_getOnlinePlayers();
    }

    /**
     * @see Server#getOnlinePlayers()
     */
    public static Collection<? extends Player> getOnlinePlayers() {
        return server.getOnlinePlayers();
    }

    /**
     * @see Server#getMaxPlayers()
     */
    public static int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    /**
     * @see Server#getPort()
     */
    public static int getPort() {
        return server.getPort();
    }

    /**
     * @see Server#getViewDistance()
     */
    public static int getViewDistance() {
        return server.getViewDistance();
    }

    /**
     * @see Server#getIp()
     */
    public static String getIp() {
        return server.getIp();
    }

    /**
     * @see Server#getServerName()
     */
    public static String getServerName() {
        return server.getServerName();
    }

    /**
     * @see Server#getServerId()
     */
    public static String getServerId() {
        return server.getServerId();
    }

    /**
     * @see Server#getWorldType()
     */
    public static String getWorldType() {
        return server.getWorldType();
    }

    /**
     * @see Server#getGenerateStructures()
     */
    public static boolean getGenerateStructures() {
        return server.getGenerateStructures();
    }

    /**
     * @see Server#getAllowNether()
     */
    public static boolean getAllowNether() {
        return server.getAllowNether();
    }

    /**
     * @see Server#hasWhitelist()
     */
    public static boolean hasWhitelist() {
        return server.hasWhitelist();
    }

    /**
     * @see Server#broadcastMessage(String message)
     */
    public static int broadcastMessage(String message) {
        return server.broadcastMessage(message);
    }

    /**
     * @see Server#getUpdateFolder()
     */
    public static String getUpdateFolder() {
        return server.getUpdateFolder();
    }

    /**
     * @see Server#getPlayer(String name)
     */
    @Deprecated
    public static Player getPlayer(String name) {
        return server.getPlayer(name);
    }

    /**
     * @see Server#matchPlayer(String name)
     */
    @Deprecated
    public static List<Player> matchPlayer(String name) {
        return server.matchPlayer(name);
    }

    /**
     * @see Server#getPlayer(java.util.UUID)
     */
    public static Player getPlayer(UUID id) {
        return server.getPlayer(id);
    }

    /**
     * @see Server#getPluginManager()
     */
    public static PluginManager getPluginManager() {
        return server.getPluginManager();
    }

    /**
     * @see Server#getScheduler()
     */
    public static BukkitScheduler getScheduler() {
        return server.getScheduler();
    }

    /**
     * @see Server#getServicesManager()
     */
    public static ServicesManager getServicesManager() {
        return server.getServicesManager();
    }

    /**
     * @see Server#getWorlds()
     */
    public static List<World> getWorlds() {
        return server.getWorlds();
    }

    /**
     * @see Server#createWorld(WorldCreator options)
     */
    public static World createWorld(WorldCreator options) {
        return server.createWorld(options);
    }

    /**
     * @see Server#unloadWorld(String name, boolean save)
     */
    public static boolean unloadWorld(String name, boolean save) {
        return server.unloadWorld(name, save);
    }

    /**
     * @see Server#unloadWorld(World world, boolean save)
     */
    public static boolean unloadWorld(World world, boolean save) {
        return server.unloadWorld(world, save);
    }

    /**
     * @see Server#getWorld(String name)
     */
    public static World getWorld(String name) {
        return server.getWorld(name);
    }

    /**
     * @see Server#getWorld(UUID uid)
     */
    public static World getWorld(UUID uid) {
        return server.getWorld(uid);
    }

    /**
     * @see Server#getMap(short id)
     * @deprecated Magic value
     */
    @Deprecated
    public static MapView getMap(short id) {
        return server.getMap(id);
    }

    /**
     * @see Server#createMap(World world)
     */
    public static MapView createMap(World world) {
        return server.createMap(world);
    }

    /**
     * @see Server#reload()
     */
    public static void reload() {
        server.reload();
    }

    /**
     * @see Server#getLogger()
     */
    public static Logger getLogger() {
        return server.getLogger();
    }

    /**
     * @see Server#getPluginCommand(String name)
     */
    public static PluginCommand getPluginCommand(String name) {
        return server.getPluginCommand(name);
    }

    /**
     * @see Server#savePlayers()
     */
    public static void savePlayers() {
        server.savePlayers();
    }

    /**
     * @see Server#dispatchCommand(CommandSender sender, String commandLine)
     */
    public static boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        return server.dispatchCommand(sender, commandLine);
    }

    /**
     * @see Server#configureDbConfig(ServerConfig config)
     */
    public static void configureDbConfig(ServerConfig config) {
        server.configureDbConfig(config);
    }

    /**
     * @see Server#addRecipe(Recipe recipe)
     */
    public static boolean addRecipe(Recipe recipe) {
        return server.addRecipe(recipe);
    }

    /**
     * @see Server#getRecipesFor(ItemStack result)
     */
    public static List<Recipe> getRecipesFor(ItemStack result) {
        return server.getRecipesFor(result);
    }

    /**
     * @see Server#recipeIterator()
     */
    public static Iterator<Recipe> recipeIterator() {
        return server.recipeIterator();
    }

    /**
     * @see Server#clearRecipes()
     */
    public static void clearRecipes() {
        server.clearRecipes();
    }

    /**
     * @see Server#resetRecipes()
     */
    public static void resetRecipes() {
        server.resetRecipes();
    }

    /**
     * @see Server#getCommandAliases()
     */
    public static Map<String, String[]> getCommandAliases() {
        return server.getCommandAliases();
    }

    /**
     * @see Server#getSpawnRadius()
     */
    public static int getSpawnRadius() {
        return server.getSpawnRadius();
    }

    /**
     * @see Server#setSpawnRadius(int value)
     */
    public static void setSpawnRadius(int value) {
        server.setSpawnRadius(value);
    }

    /**
     * @see Server#getOnlineMode()
     */
    public static boolean getOnlineMode() {
        return server.getOnlineMode();
    }

    /**
     * @see Server#getAllowFlight()
     */
    public static boolean getAllowFlight() {
        return server.getAllowFlight();
    }

    /**
     * @see Server#isHardcore()
     */
    public static boolean isHardcore() {
        return server.isHardcore();
    }

    /**
     * @see Server#shutdown()
     */
    public static void shutdown() {
        server.shutdown();
    }

    /**
     * @see Server#broadcast(String message, String permission)
     */
    public static int broadcast(String message, String permission) {
        return server.broadcast(message, permission);
    }

    /**
     * @see Server#getOfflinePlayer(String name)
     */
    @Deprecated
    public static OfflinePlayer getOfflinePlayer(String name) {
        return server.getOfflinePlayer(name);
    }

    /**
     * @see Server#getOfflinePlayer(java.util.UUID)
     */
    public static OfflinePlayer getOfflinePlayer(UUID id) {
        return server.getOfflinePlayer(id);
    }

    /**
     * @see Server#getPlayerExact(String name)
     */
    @Deprecated
    public static Player getPlayerExact(String name) {
        return server.getPlayerExact(name);
    }

    /**
     * @see Server#getIPBans()
     */
    public static Set<String> getIPBans() {
        return server.getIPBans();
    }

    /**
     * @see Server#banIP(String address)
     */
    public static void banIP(String address) {
        server.banIP(address);
    }

    /**
     * @see Server#unbanIP(String address)
     */
    public static void unbanIP(String address) {
        server.unbanIP(address);
    }

    /**
     * @see Server#getBannedPlayers()
     */
    public static Set<OfflinePlayer> getBannedPlayers() {
        return server.getBannedPlayers();
    }

    /**
     * @see Server#getBanList(BanList.Type)
     */
    public static BanList getBanList(BanList.Type type){
        return server.getBanList(type);
    }

    /**
     * @see Server#setWhitelist(boolean value)
     */
    public static void setWhitelist(boolean value) {
        server.setWhitelist(value);
    }

    /**
     * @see Server#getWhitelistedPlayers()
     */
    public static Set<OfflinePlayer> getWhitelistedPlayers() {
        return server.getWhitelistedPlayers();
    }

    /**
     * @see Server#reloadWhitelist()
     */
    public static void reloadWhitelist() {
        server.reloadWhitelist();
    }

    /**
     * @see Server#getConsoleSender()
     */
    public static ConsoleCommandSender getConsoleSender() {
        return server.getConsoleSender();
    }

    /**
     * @see Server#getOperators()
     */
    public static Set<OfflinePlayer> getOperators() {
        return server.getOperators();
    }

    /**
     * @see Server#getWorldContainer()
     */
    public static File getWorldContainer() {
        return server.getWorldContainer();
    }

    /**
     * @see Server#getMessenger()
     */
    public static Messenger getMessenger() {
        return server.getMessenger();
    }

    /**
     * @see Server#getAllowEnd()
     */
    public static boolean getAllowEnd() {
        return server.getAllowEnd();
    }

    /**
     * @see Server#getUpdateFolderFile()
     */
    public static File getUpdateFolderFile() {
        return server.getUpdateFolderFile();
    }

    /**
     * @see Server#getConnectionThrottle()
     */
    public static long getConnectionThrottle() {
        return server.getConnectionThrottle();
    }

    /**
     * @see Server#getTicksPerAnimalSpawns()
     */
    public static int getTicksPerAnimalSpawns() {
        return server.getTicksPerAnimalSpawns();
    }

    /**
     * @see Server#getTicksPerMonsterSpawns()
     */
    public static int getTicksPerMonsterSpawns() {
        return server.getTicksPerMonsterSpawns();
    }

    /**
     * @see Server#useExactLoginLocation()
     */
    public static boolean useExactLoginLocation() {
        return server.useExactLoginLocation();
    }

    /**
     * @see Server#getDefaultGameMode()
     */
    public static GameMode getDefaultGameMode() {
        return server.getDefaultGameMode();
    }

    /**
     * @see Server#setDefaultGameMode(GameMode mode)
     */
    public static void setDefaultGameMode(GameMode mode) {
        server.setDefaultGameMode(mode);
    }

    /**
     * @see Server#getOfflinePlayers()
     */
    public static OfflinePlayer[] getOfflinePlayers() {
        return server.getOfflinePlayers();
    }

    /**
     * @see Server#createInventory(InventoryHolder owner, InventoryType type)
     */
    public static Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return server.createInventory(owner, type);
    }

    /**
     * @see Server#createInventory(InventoryHolder owner, InventoryType type, String title)
     */
    public static Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return server.createInventory(owner, type, title);
    }

    /**
     * @see Server#createInventory(InventoryHolder owner, int size)
     */
    public static Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        return server.createInventory(owner, size);
    }

    /**
     * @see Server#createInventory(InventoryHolder owner, int size, String
     *     title)
     */
    public static Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        return server.createInventory(owner, size, title);
    }

    /**
     * @see Server#getHelpMap()
     */
    public static HelpMap getHelpMap() {
        return server.getHelpMap();
    }

    /**
     * @see Server#getMonsterSpawnLimit()
     */
    public static int getMonsterSpawnLimit() {
        return server.getMonsterSpawnLimit();
    }

    /**
     * @see Server#getAnimalSpawnLimit()
     */
    public static int getAnimalSpawnLimit() {
        return server.getAnimalSpawnLimit();
    }

    /**
     * @see Server#getWaterAnimalSpawnLimit()
     */
    public static int getWaterAnimalSpawnLimit() {
        return server.getWaterAnimalSpawnLimit();
    }

    /**
     * @see Server#getAmbientSpawnLimit()
     */
    public static int getAmbientSpawnLimit() {
        return server.getAmbientSpawnLimit();
    }

    /**
     * @see Server#isPrimaryThread()
     */
    public static boolean isPrimaryThread() {
        return server.isPrimaryThread();
    }

    /**
     * @see Server#getMotd()
     */
    public static String getMotd() {
        return server.getMotd();
    }

    /**
     * @see Server#getShutdownMessage()
     */
    public static String getShutdownMessage() {
        return server.getShutdownMessage();
    }

    /**
     * @see Server#getWarningState()
     */
    public static WarningState getWarningState() {
        return server.getWarningState();
    }

    /**
     * @see Server#getItemFactory()
     */
    public static ItemFactory getItemFactory() {
        return server.getItemFactory();
    }

    /**
     * @see Server#getScoreboardManager()
     */
    public static ScoreboardManager getScoreboardManager() {
        return server.getScoreboardManager();
    }

    /**
     * @see Server#getServerIcon()
     */
    public static CachedServerIcon getServerIcon() {
        return server.getServerIcon();
    }

    /**
     * @see Server#loadServerIcon(File)
     */
    public static CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        return server.loadServerIcon(file);
    }

    /**
     * @see Server#loadServerIcon(BufferedImage)
     */
    public static CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
        return server.loadServerIcon(image);
    }

    /**
     * @see Server#setIdleTimeout(int)
     */
    public static void setIdleTimeout(int threshold) {
        server.setIdleTimeout(threshold);
    }

    /**
     * @see Server#getIdleTimeout()
     */
    public static int getIdleTimeout() {
        return server.getIdleTimeout();
    }

    /**
     * @see Server#getUnsafe()
     */
    @Deprecated
    public static UnsafeValues getUnsafe() {
        return server.getUnsafe();
    }
}
