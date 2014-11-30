package org.bukkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import com.avaje.ebean.config.ServerConfig;
import com.google.common.collect.ImmutableList;

import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.ItemMeta;

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
     * Gets the name of this server implementation.
     *
     * @return name of this server implementation
     */
    public static String getName() {
        return server.getName();
    }

    /**
     * Gets the version string of this server implementation.
     *
     * @return version of this server implementation
     */
    public static String getVersion() {
        return server.getVersion();
    }

    /**
     * Gets the Bukkit version that this server is running.
     *
     * @return version of Bukkit
     */
    public static String getBukkitVersion() {
        return server.getBukkitVersion();
    }

    /**
     * Gets an array copy of all currently logged in players.
     * <p>
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @deprecated superseded by {@link #getOnlinePlayers()}
     * @return an array of Players that are currently online
     */
    @Deprecated
    public static Player[] _INVALID_getOnlinePlayers() {
        return server._INVALID_getOnlinePlayers();
    }

    /**
     * Gets a view of all currently logged in players. This {@linkplain
     * Collections#unmodifiableCollection(Collection) view} is a reused
     * object, making some operations like {@link Collection#size()}
     * zero-allocation.
     * <p>
     * The collection is a view backed by the internal representation, such
     * that, changes to the internal state of the server will be reflected
     * immediately. However, the reuse of the returned collection (identity)
     * is not strictly guaranteed for future or all implementations. Casting
     * the collection, or relying on interface implementations (like {@link
     * Serializable} or {@link List}), is deprecated.
     * <p>
     * Iteration behavior is undefined outside of self-contained main-thread
     * uses. Normal and immediate iterator use without consequences that
     * affect the collection are fully supported. The effects following
     * (non-exhaustive) {@link Entity#teleport(Location) teleportation},
     * {@link Player#setHealth(double) death}, and {@link Player#kickPlayer(
     * String) kicking} are undefined. Any use of this collection from
     * asynchronous threads is unsafe.
     * <p>
     * For safe consequential iteration or mimicking the old array behavior,
     * using {@link Collection#toArray(Object[])} is recommended. For making
     * snapshots, {@link ImmutableList#copyOf(Collection)} is recommended.
     *
     * @return a view of currently online players.
     */
    public static Collection<? extends Player> getOnlinePlayers() {
        return server.getOnlinePlayers();
    }

    /**
     * Get the maximum amount of players which can login to this server.
     *
     * @return the amount of players this server allows
     */
    public static int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    /**
     * Get the game port that the server runs on.
     *
     * @return the port number of this server
     */
    public static int getPort() {
        return server.getPort();
    }

    /**
     * Get the view distance from this server.
     *
     * @return the view distance from this server.
     */
    public static int getViewDistance() {
        return server.getViewDistance();
    }

    /**
     * Get the IP that this server is bound to, or empty string if not
     * specified.
     *
     * @return the IP string that this server is bound to, otherwise empty
     *     string
     */
    public static String getIp() {
        return server.getIp();
    }

    /**
     * Get the name of this server.
     *
     * @return the name of this server
     */
    public static String getServerName() {
        return server.getServerName();
    }

    /**
     * Get an ID of this server. The ID is a simple generally alphanumeric ID
     * that can be used for uniquely identifying this server.
     *
     * @return the ID of this server
     */
    public static String getServerId() {
        return server.getServerId();
    }
    
    /**
     * Get world type (level-type setting) for default world.
     *
     * @return the value of level-type (e.g. DEFAULT, FLAT, DEFAULT_1_1)
     */
    public static String getWorldType() {
        return server.getWorldType();
    }

    /**
     * Get generate-structures setting.
     *
     * @return true if structure generation is enabled, false otherwise
     */
    public static boolean getGenerateStructures() {
        return server.getGenerateStructures();
    }

    /**
     * Gets whether this server allows the End or not.
     *
     * @return whether this server allows the End or not
     */
    public static boolean getAllowEnd() {
        return server.getAllowEnd();
    }

    /**
     * Gets whether this server allows the Nether or not.
     *
     * @return whether this server allows the Nether or not
     */
    public static boolean getAllowNether() {
        return server.getAllowNether();
    }

    /**
     * Gets whether this server has a whitelist or not.
     *
     * @return whether this server has a whitelist or not
     */
    public static boolean hasWhitelist() {
        return server.hasWhitelist();
    }

    /**
     * Sets if the server is whitelisted.
     *
     * @param value true for whitelist on, false for off
     */
    public static void setWhitelist(boolean value) {
        server.setWhitelist(value);
    }

    /**
     * Gets a list of whitelisted players.
     *
     * @return a set containing all whitelisted players
     */
    public static Set<OfflinePlayer> getWhitelistedPlayers() {
        return server.getWhitelistedPlayers();
    }

    /**
     * Reloads the whitelist from disk.
     */
    public static void reloadWhitelist() {
        server.reloadWhitelist();
    }

    /**
     * Broadcast a message to all players.
     * <p>
     * This is the same as calling {@link #broadcast(java.lang.String,
     * java.lang.String)} to {@link Server#BROADCAST_CHANNEL_USERS}
     *
     * @param message the message
     * @return the number of players
     */
    public static int broadcastMessage(String message) {
        return server.broadcastMessage(message);
    }

    /**
     * Gets the name of the update folder. The update folder is used to safely
     * update plugins at the right moment on a plugin load.
     * <p>
     * The update folder name is relative to the plugins folder.
     *
     * @return the name of the update folder
     */
    public static String getUpdateFolder() {
        return server.getUpdateFolder();
    }

    /**
     * Gets the update folder. The update folder is used to safely update
     * plugins at the right moment on a plugin load.
     *
     * @return the update folder
     */
    public static File getUpdateFolderFile() {
        return server.getUpdateFolderFile();
    }

    /**
     * Gets the value of the connection throttle setting.
     *
     * @return the value of the connection throttle setting
     */
    public static long getConnectionThrottle() {
        return server.getConnectionThrottle();
    }

    /**
     * Gets default ticks per animal spawns value.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters
     *     every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters
     *     every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b> If set to 0, animal spawning will be disabled. We
     * recommend using spawn-animals to control this instead.
     * <p>
     * Minecraft default: 400.
     *
     * @return the default ticks per animal spawns value
     */
    public static int getTicksPerAnimalSpawns() {
        return server.getTicksPerAnimalSpawns();
    }

    /**
     * Gets the default ticks per monster spawns value.
     * <p>
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters
     *     every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters
     *     every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p>
     * <b>Note:</b> If set to 0, monsters spawning will be disabled. We
     * recommend using spawn-monsters to control this instead.
     * <p>
     * Minecraft default: 1.
     *
     * @return the default ticks per monsters spawn value
     */
    public static int getTicksPerMonsterSpawns() {
        return server.getTicksPerMonsterSpawns();
    }

    /**
     * Gets a player object by the given username.
     * <p>
     * This method may not return objects for offline players.
     *
     * @deprecated Use {@link #getPlayer(UUID)} as player names are no longer
     *     guaranteed to be unique
     * @param name the name to look up
     * @return a player if one was found, null otherwise
     */
    @Deprecated
    public static Player getPlayer(String name) {
        return server.getPlayer(name);
    }

    /**
     * Gets the player with the exact given name, case insensitive.
     *
     * @deprecated Use {@link #getPlayer(UUID)} as player names are no longer
     *     guaranteed to be unique
     * @param name Exact name of the player to retrieve
     * @return a player object if one was found, null otherwise
     */
    @Deprecated
    public static Player getPlayerExact(String name) {
        return server.getPlayerExact(name);
    }

    /**
     * Attempts to match any players with the given name, and returns a list
     * of all possibly matches.
     * <p>
     * This list is not sorted in any particular order. If an exact match is
     * found, the returned list will only contain a single result.
     *
     * @deprecated Use {@link #getPlayer(UUID)} as player names are no longer
     *     guaranteed to be unique
     * @param name the (partial) name to match
     * @return list of all possible players
     */
    @Deprecated
    public static List<Player> matchPlayer(String name) {
        return server.matchPlayer(name);
    }

    /**
     * Gets the player with the given UUID.
     *
     * @param id UUID of the player to retrieve
     * @return a player object if one was found, null otherwise
     */
    public static Player getPlayer(UUID id) {
        return server.getPlayer(id);
    }

    /**
     * Gets the plugin manager for interfacing with plugins.
     *
     * @return a plugin manager for this Server instance
     */
    public static PluginManager getPluginManager() {
        return server.getPluginManager();
    }

    /**
     * Gets the scheduler for managing scheduled events.
     *
     * @return a scheduling service for this server
     */
    public static BukkitScheduler getScheduler() {
        return server.getScheduler();
    }

    /**
     * Gets a services manager.
     *
     * @return s services manager
     */
    public static ServicesManager getServicesManager() {
        return server.getServicesManager();
    }

    /**
     * Gets a list of all worlds on this server.
     *
     * @return a list of worlds
     */
    public static List<World> getWorlds() {
        return server.getWorlds();
    }

    /**
     * Creates or loads a world with the given name using the specified
     * options.
     * <p>
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(creator.name()).
     *
     * @param creator the options to use when creating the world
     * @return newly created or loaded world
     */
    public static World createWorld(WorldCreator creator) {
        return server.createWorld(creator);
    }

    /**
     * Unloads a world with the given name.
     *
     * @param name Name of the world to unload
     * @param save whether to save the chunks before unloading
     * @return true if successful, false otherwise
     */
    public static boolean unloadWorld(String name, boolean save) {
        return server.unloadWorld(name, save);
    }

    /**
     * Unloads the given world.
     *
     * @param world the world to unload
     * @param save whether to save the chunks before unloading
     * @return true if successful, false otherwise
     */
    public static boolean unloadWorld(World world, boolean save) {
        return server.unloadWorld(world, save);
    }

    /**
     * Gets the world with the given name.
     *
     * @param name the name of the world to retrieve
     * @return a world with the given name, or null if none exists
     */
    public static World getWorld(String name) {
        return server.getWorld(name);
    }

    /**
     * Gets the world from the given Unique ID.
     *
     * @param uid a unique-id of the world to retrieve
     * @return a world with the given Unique ID, or null if none exists
     */
    public static World getWorld(UUID uid) {
        return server.getWorld(uid);
    }

    /**
     * Gets the map from the given item ID.
     *
     * @param id the id of the map to get
     * @return a map view if it exists, or null otherwise
     * @deprecated Magic value
     */
    @Deprecated
    public static MapView getMap(short id) {
        return server.getMap(id);
    }

    /**
     * Create a new map with an automatically assigned ID.
     *
     * @param world the world the map will belong to
     * @return a newly created map view
     */
    public static MapView createMap(World world) {
        return server.createMap(world);
    }

    /**
     * Reloads the server, refreshing settings and plugin information.
     */
    public static void reload() {
        server.reload();
    }

    /**
     * Returns the primary logger associated with this server instance.
     *
     * @return Logger associated with this server
     */
    public static Logger getLogger() {
        return server.getLogger();
    }

    /**
     * Gets a {@link PluginCommand} with the given name or alias.
     *
     * @param name the name of the command to retrieve
     * @return a plugin command if found, null otherwise
     */
    public static PluginCommand getPluginCommand(String name) {
        return server.getPluginCommand(name);
    }

    /**
     * Writes loaded players to disk.
     */
    public static void savePlayers() {
        server.savePlayers();
    }
    
    /**
     * Dispatches a command on this server, and executes it if found.
     *
     * @param sender the apparent sender of the command
     * @param commandLine the command + arguments. Example: <code>test abc
     *     123</code>
     * @return returns false if no target is found
     * @throws CommandException thrown when the executor for the given command
     *     fails with an unhandled exception
     */
    public static boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        return server.dispatchCommand(sender, commandLine);
    }

    /**
     * Populates a given {@link ServerConfig} with values attributes to this
     * server.
     *
     * @param config the server config to populate
     */
    public static void configureDbConfig(ServerConfig config) {
        server.configureDbConfig(config);
    }

    /**
     * Adds a recipe to the crafting manager.
     *
     * @param recipe the recipe to add
     * @return true if the recipe was added, false if it wasn't for some
     *     reason
     */
    public static boolean addRecipe(Recipe recipe) {
        return server.addRecipe(recipe);
    }

    /**
     * Get a list of all recipes for a given item. The stack size is ignored
     * in comparisons. If the durability is -1, it will match any data value.
     *
     * @param result the item to match against recipe results
     * @return a list of recipes with the given result
     */
    public static List<Recipe> getRecipesFor(ItemStack result) {
        return server.getRecipesFor(result);
    }

    /**
     * Get an iterator through the list of crafting recipes.
     *
     * @return an iterator
     */
    public static Iterator<Recipe> recipeIterator() {
        return server.recipeIterator();
    }

    /**
     * Clears the list of crafting recipes.
     */
    public static void clearRecipes() {
        server.clearRecipes();
    }

    /**
     * Resets the list of crafting recipes to the default.
     */
    public static void resetRecipes() {
        server.resetRecipes();
    }
    
    /**
     * Gets a list of command aliases defined in the server properties.
     *
     * @return a map of aliases to command names
     */
    public static Map<String, String[]> getCommandAliases() {
        return server.getCommandAliases();
    }

    /**
     * Gets the radius, in blocks, around each worlds spawn point to protect.
     *
     * @return spawn radius, or 0 if none
     */
    public static int getSpawnRadius() {
        return server.getSpawnRadius();
    }

    /**
     * Sets the radius, in blocks, around each worlds spawn point to protect.
     *
     * @param value new spawn radius, or 0 if none
     */
    public static void setSpawnRadius(int value) {
        server.setSpawnRadius(value);
    }

    /**
     * Gets whether the Server is in online mode or not.
     *
     * @return true if the server authenticates clients, false otherwise
     */
    public static boolean getOnlineMode() {
        return server.getOnlineMode();
    }

    /**
     * Gets whether this server allows flying or not.
     *
     * @return true if the server allows flight, false otherwise
     */
    public static boolean getAllowFlight() {
        return server.getAllowFlight();
    }

    /**
     * Gets whether the server is in hardcore mode or not.
     *
     * @return true if the server mode is hardcore, false otherwise
     */
    public static boolean isHardcore() {
        return server.isHardcore();
    }

    /**
     * Gets whether to use vanilla (false) or exact behaviour (true).
     *
     * <ul>
     * <li>Vanilla behaviour: check for collisions and move the player if
     *     needed.
     * <li>Exact behaviour: spawn players exactly where they should be.
     * </ul>
     *
     * @return true if exact location locations are used for spawning, false
     *     for vanilla collision detection or otherwise
     */
    public static boolean useExactLoginLocation() {
        return server.useExactLoginLocation();
    }
 
    /**
     * Shutdowns the server, stopping everything.
     */
    public static void shutdown() {
        server.shutdown();
    }

    /**
     * Broadcasts the specified message to every user with the given
     * permission name.
     *
     * @param message message to broadcast
     * @param permission the required permission {@link Permissible
     *     permissibles} must have to receive the broadcast
     * @return number of message recipients
     */
    public static int broadcast(String message, String permission) {
        return server.broadcast(message, permission);
    }

    /**
     * Gets the player by the given name, regardless if they are offline or
     * online.
     * <p>
     * This method may involve a blocking web request to get the UUID for the
     * given name.
     * <p>
     * This will return an object even if the player does not exist. To this
     * method, all players will exist.
     *
     * @deprecated Persistent storage of users should be by UUID as names are no longer
     *             unique past a single session.
     * @param name the name the player to retrieve
     * @return an offline player
     * @see #getOfflinePlayer(java.util.UUID)
     */
    @Deprecated
    public static OfflinePlayer getOfflinePlayer(String name) {
        return server.getOfflinePlayer(name);
    }

    /**
     * Gets the player by the given UUID, regardless if they are offline or
     * online.
     * <p>
     * This will return an object even if the player does not exist. To this
     * method, all players will exist.
     *
     * @param id the UUID of the player to retrieve
     * @return an offline player
     */
    public static OfflinePlayer getOfflinePlayer(UUID id) {
        return server.getOfflinePlayer(id);
    }

    /**
     * Gets a set containing all current IPs that are banned.
     *
     * @return a set containing banned IP addresses
     */
    public static Set<String> getIPBans() {
        return server.getIPBans();
    }

    /**
     * Bans the specified address from the server.
     *
     * @param address the IP address to ban
     */
    public static void banIP(String address) {
        server.banIP(address);
    }

    /**
     * Unbans the specified address from the server.
     *
     * @param address the IP address to unban
     */
    public static void unbanIP(String address) {
        server.unbanIP(address);
    }

    /**
     * Gets a set containing all banned players.
     *
     * @return a set containing banned players
     */
    public static Set<OfflinePlayer> getBannedPlayers() {
        return server.getBannedPlayers();
    }

    /**
     * Gets a ban list for the supplied type.
     * <p>
     * Bans by name are no longer supported and this method will return
     * null when trying to request them. The replacement is bans by UUID.
     *
     * @param type the type of list to fetch, cannot be null
     * @return a ban list of the specified type
     */
    public static BanList getBanList(BanList.Type type){
        return server.getBanList(type);
    }

    /**
     * Gets a set containing all player operators.
     *
     * @return a set containing player operators
     */
    public static Set<OfflinePlayer> getOperators() {
        return server.getOperators();
    }

    /**
     * Gets the default {@link GameMode} for new players.
     *
     * @return the default game mode
     */
    public static GameMode getDefaultGameMode() {
        return server.getDefaultGameMode();
    }

    /**
     * Sets the default {@link GameMode} for new players.
     *
     * @param mode the new game mode
     */
    public static void setDefaultGameMode(GameMode mode) {
        server.setDefaultGameMode(mode);
    }

    /**
     * Gets a {@link ConsoleCommandSender} that may be used as an input source
     * for this server.
     *
     * @return a console command sender
     */
    public static ConsoleCommandSender getConsoleSender() {
        return server.getConsoleSender();
    }

    /**
     * Gets the folder that contains all of the various {@link World}s.
     *
     * @return folder that contains all worlds
     */
    public static File getWorldContainer() {
        return server.getWorldContainer();
    }

    /**
     * Gets every player that has ever played on this server.
     *
     * @return an array containing all previous players
     */
    public static OfflinePlayer[] getOfflinePlayers() {
        return server.getOfflinePlayers();
    }

    /**
     * Gets the {@link Messenger} responsible for this server.
     *
     * @return messenger responsible for this server
     */
    public static Messenger getMessenger() {
        return server.getMessenger();
    }

    /**
     * Gets the {@link HelpMap} providing help topics for this server.
     *
     * @return a help map for this server
     */
    public static HelpMap getHelpMap() {
        return server.getHelpMap();
    }

    /**
     * Creates an empty inventory of the specified type. If the type is {@link
     * InventoryType#CHEST}, the new inventory has a size of 27; otherwise the
     * new inventory has the normal size for its type.
     *
     * @param owner the holder of the inventory, or null to indicate no holder
     * @param type the type of inventory to create
     * @return a new inventory
     */
    public static Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return server.createInventory(owner, type);
    }

    /**
     * Creates an empty inventory with the specified type and title. If the type
     * is {@link InventoryType#CHEST}, the new inventory has a size of 27;
     * otherwise the new inventory has the normal size for its type.<br>
     * It should be noted that some inventory types do not support titles and
     * may not render with said titles on the Minecraft client.
     *
     * @param owner The holder of the inventory; can be null if there's no holder.
     * @param type The type of inventory to create.
     * @param title The title of the inventory, to be displayed when it is viewed.
     * @return The new inventory.
     */
    public static Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return server.createInventory(owner, type, title);
    }

    /**
     * Creates an empty inventory of type {@link InventoryType#CHEST} with the
     * specified size.
     *
     * @param owner the holder of the inventory, or null to indicate no holder
     * @param size a multiple of 9 as the size of inventory to create
     * @return a new inventory
     * @throws IllegalArgumentException if the size is not a multiple of 9
     */
    public static Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        return server.createInventory(owner, size);
    }

    /**
     * Creates an empty inventory of type {@link InventoryType#CHEST} with the
     * specified size and title.
     *
     * @param owner the holder of the inventory, or null to indicate no holder
     * @param size a multiple of 9 as the size of inventory to create
     * @param title the title of the inventory, displayed when inventory is
     *     viewed
     * @return a new inventory
     * @throws IllegalArgumentException if the size is not a multiple of 9
     */
    public static Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        return server.createInventory(owner, size, title);
    }

    /**
     * Gets user-specified limit for number of monsters that can spawn in a
     * chunk.
     *
     * @return the monster spawn limit
     */
    public static int getMonsterSpawnLimit() {
        return server.getMonsterSpawnLimit();
    }

    /**
     * Gets user-specified limit for number of animals that can spawn in a
     * chunk.
     *
     * @return the animal spawn limit
     */
    public static int getAnimalSpawnLimit() {
        return server.getAnimalSpawnLimit();
    }

    /**
     * Gets user-specified limit for number of water animals that can spawn in
     * a chunk.
     *
     * @return the water animal spawn limit
     */
    public static int getWaterAnimalSpawnLimit() {
        return server.getWaterAnimalSpawnLimit();
    }
    
    /**
     * Gets user-specified limit for number of ambient mobs that can spawn in
     * a chunk.
     *
     * @return the ambient spawn limit
     */
    public static int getAmbientSpawnLimit() {
        return server.getAmbientSpawnLimit();
    }

    /**
     * Checks the current thread against the expected primary thread for the
     * server.
     * <p>
     * <b>Note:</b> this method should not be used to indicate the current
     * synchronized state of the runtime. A current thread matching the main
     * thread indicates that it is synchronized, but a mismatch <b>does not
     * preclude</b> the same assumption.
     *
     * @return true if the current thread matches the expected primary thread,
     *     false otherwise
     */
    public static boolean isPrimaryThread() {
        return server.isPrimaryThread();
    }

    /**
     * Gets the message that is displayed on the server list.
     *
     * @return the servers MOTD
     */
    public static String getMotd() {
        return server.getMotd();
    }

    /**
     * Gets the default message that is displayed when the server is stopped.
     *
     * @return the shutdown message
     */
    public static String getShutdownMessage() {
        return server.getShutdownMessage();
    }

    /**
     * Gets the current warning state for the server.
     *
     * @return the configured warning state
     */
    public static WarningState getWarningState() {
        return server.getWarningState();
    }

    /**
     * Gets the instance of the item factory (for {@link ItemMeta}).
     *
     * @return the item factory
     * @see ItemFactory
     */
    public static ItemFactory getItemFactory() {
        return server.getItemFactory();
    }

    /**
     * Gets the instance of the scoreboard manager.
     * <p>
     * This will only exist after the first world has loaded.
     *
     * @return the scoreboard manager or null if no worlds are loaded.
     */
    public static ScoreboardManager getScoreboardManager() {
        return server.getScoreboardManager();
    }

    /**
     * Gets an instance of the server's default server-icon.
     *
     * @return the default server-icon; null values may be used by the
     *     implementation to indicate no defined icon, but this behavior is
     *     not guaranteed
     */
    public static CachedServerIcon getServerIcon() {
        return server.getServerIcon();
    }

    /**
     * Loads an image from a file, and returns a cached image for the specific
     * server-icon.
     * <p>
     * Size and type are implementation defined. An incompatible file is
     * guaranteed to throw an implementation-defined {@link Exception}.
     *
     * @param file the file to load the from
     * @throws IllegalArgumentException if image is null
     * @throws Exception if the image does not meet current server server-icon
     *     specifications
     * @return a cached server-icon that can be used for a {@link
     *     ServerListPingEvent#setServerIcon(CachedServerIcon)}
     */
    public static CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        return server.loadServerIcon(file);
    }

    /**
     * Creates a cached server-icon for the specific image.
     * <p>
     * Size and type are implementation defined. An incompatible file is
     * guaranteed to throw an implementation-defined {@link Exception}.
     *
     * @param image the image to use
     * @throws IllegalArgumentException if image is null
     * @throws Exception if the image does not meet current server
     *     server-icon specifications
     * @return a cached server-icon that can be used for a {@link
     *     ServerListPingEvent#setServerIcon(CachedServerIcon)}
     */
    public static CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
        return server.loadServerIcon(image);
    }

    /**
     * Set the idle kick timeout. Any players idle for the specified amount of
     * time will be automatically kicked.
     * <p>
     * A value of 0 will disable the idle kick timeout.
     *
     * @param threshold the idle timeout in minutes
     */
    public static void setIdleTimeout(int threshold) {
        server.setIdleTimeout(threshold);
    }

    /**
     * Gets the idle kick timeout.
     *
     * @return the idle timeout in minutes
     */
    public static int getIdleTimeout() {
        return server.getIdleTimeout();
    }

    /**
     * @see UnsafeValues
     * @return the unsafe values instance
     */
    @Deprecated
    public static UnsafeValues getUnsafe() {
        return server.getUnsafe();
    }
}
