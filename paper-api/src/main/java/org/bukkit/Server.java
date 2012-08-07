package org.bukkit;

import java.io.File;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scheduler.BukkitScheduler;

import com.avaje.ebean.config.ServerConfig;

/**
 * Represents a server implementation
 */
public interface Server extends PluginMessageRecipient {
    /**
     * Used for all administrative messages, such as an operator using a command.
     * <p />
     * For use in {@link #broadcast(java.lang.String, java.lang.String)}
     */
    public static final String BROADCAST_CHANNEL_ADMINISTRATIVE = "bukkit.broadcast.admin";

    /**
     * Used for all announcement messages, such as informing users that a player has joined.
     * <p />
     * For use in {@link #broadcast(java.lang.String, java.lang.String)}
     */
    public static final String BROADCAST_CHANNEL_USERS = "bukkit.broadcast.user";

    /**
     * Gets the name of this server implementation
     *
     * @return name of this server implementation
     */
    public String getName();

    /**
     * Gets the version string of this server implementation.
     *
     * @return version of this server implementation
     */
    public String getVersion();

    /**
     * Gets the Bukkit version that this server is running.
     *
     * @return Version of Bukkit
     */
    public String getBukkitVersion();

    /**
     * Gets a list of all currently logged in players
     *
     * @return An array of Players that are currently online
     */
    public Player[] getOnlinePlayers();

    /**
     * Get the maximum amount of players which can login to this server
     *
     * @return The amount of players this server allows
     */
    public int getMaxPlayers();

    /**
     * Get the game port that the server runs on
     *
     * @return The port number of this server
     */
    public int getPort();

    /**
     * Get the view distance from this server.
     *
     * @return The view distance from this server.
     */
    public int getViewDistance();

    /**
     * Get the IP that this server is bound to or empty string if not specified
     *
     * @return The IP string that this server is bound to, otherwise empty string
     */
    public String getIp();

    /**
     * Get the name of this server
     *
     * @return The name of this server
     */
    public String getServerName();

    /**
     * Get an ID of this server. The ID is a simple generally alphanumeric
     * ID that can be used for uniquely identifying this server.
     *
     * @return The ID of this server
     */
    public String getServerId();

    /**
     * Get world type (level-type setting) for default world
     *
     * @return The value of level-type (e.g. DEFAULT, FLAT, DEFAULT_1_1)
     */
    public String getWorldType();

    /**
     * Get generate-structures setting
     *
     * @return true if structure generation is enabled, false if not
     */
    public boolean getGenerateStructures();

    /**
     * Gets whether this server allows the End or not.
     *
     * @return Whether this server allows the End or not.
     */
    public boolean getAllowEnd();

    /**
     * Gets whether this server allows the Nether or not.
     *
     * @return Whether this server allows the Nether or not.
     */
    public boolean getAllowNether();

    /**
     * Gets whether this server has a whitelist or not.
     *
     * @return Whether this server has a whitelist or not.
     */
    public boolean hasWhitelist();

    /**
     * Sets the whitelist on or off
     *
     * @param value true if whitelist is on, otherwise false
     */
    public void setWhitelist(boolean value);

    /**
     * Gets a list of whitelisted players
     *
     * @return Set containing all whitelisted players
     */
    public Set<OfflinePlayer> getWhitelistedPlayers();

    /**
     * Reloads the whitelist from disk
     */
    public void reloadWhitelist();

    /**
     * Broadcast a message to all players.
     * <p />
     * This is the same as calling {@link #broadcast(java.lang.String, java.lang.String)} to {@link #BROADCAST_CHANNEL_USERS}
     *
     * @param message the message
     * @return the number of players
     */
    public int broadcastMessage(String message);

    /**
     * Gets the name of the update folder. The update folder is used to safely update
     * plugins at the right moment on a plugin load.
     * <p />
     * The update folder name is relative to the plugins folder.
     *
     * @return The name of the update folder
     */
    public String getUpdateFolder();

    /**
     * Gets the update folder. The update folder is used to safely update
     * plugins at the right moment on a plugin load.
     *
     * @return The name of the update folder
     */
    public File getUpdateFolderFile();

    /**
     * Gets the value of the connection throttle setting
     *
     * @return the value of the connection throttle setting
     */
    public long getConnectionThrottle();

    /**
     * Gets default ticks per animal spawns value
     * <p />
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p />
     * <b>Note:</b>
     * If set to 0, animal spawning will be disabled. We recommend using spawn-animals to control this instead.
     * <p />
     * Minecraft default: 400.
     *
     * @return The default ticks per animal spawns value
     */
    public int getTicksPerAnimalSpawns();

    /**
     * Gets the default ticks per monster spawns value
     * <p />
     * <b>Example Usage:</b>
     * <ul>
     * <li>A value of 1 will mean the server will attempt to spawn monsters every tick.
     * <li>A value of 400 will mean the server will attempt to spawn monsters every 400th tick.
     * <li>A value below 0 will be reset back to Minecraft's default.
     * </ul>
     * <p />
     * <b>Note:</b>
     * If set to 0, monsters spawning will be disabled. We recommend using spawn-monsters to control this instead.
     * <p />
     * Minecraft default: 1.
     *
     * @return The default ticks per monsters spawn value
     */
    public int getTicksPerMonsterSpawns();

    /**
     * Gets a player object by the given username
     * <p />
     * This method may not return objects for offline players
     *
     * @param name Name to look up
     * @return Player if it was found, otherwise null
     */
    public Player getPlayer(String name);

    /**
     * Gets the player with the exact given name, case insensitive
     *
     * @param name Exact name of the player to retrieve
     * @return Player object or null if not found
     */
    public Player getPlayerExact(String name);

    /**
     * Attempts to match any players with the given name, and returns a list
     * of all possibly matches
     * <p />
     * This list is not sorted in any particular order. If an exact match is found,
     * the returned list will only contain a single result.
     *
     * @param name Name to match
     * @return List of all possible players
     */
    public List<Player> matchPlayer(String name);

    /**
     * Gets the PluginManager for interfacing with plugins
     *
     * @return PluginManager for this Server instance
     */
    public PluginManager getPluginManager();

    /**
     * Gets the Scheduler for managing scheduled events
     *
     * @return Scheduler for this Server instance
     */
    public BukkitScheduler getScheduler();

    /**
     * Gets a services manager
     *
     * @return Services manager
     */
    public ServicesManager getServicesManager();

    /**
     * Gets a list of all worlds on this server
     *
     * @return A list of worlds
     */
    public List<World> getWorlds();

    /**
     * Creates or loads a world with the given name using the specified options.
     * <p />
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(creator.name()).
     *
     * @param creator The options to use when creating the world.
     * @return Newly created or loaded world
     */
    public World createWorld(WorldCreator creator);

    /**
     * Unloads a world with the given name.
     *
     * @param name Name of the world to unload
     * @param save Whether to save the chunks before unloading.
     * @return Whether the action was Successful
     */
    public boolean unloadWorld(String name, boolean save);

    /**
     * Unloads the given world.
     *
     * @param world The world to unload
     * @param save Whether to save the chunks before unloading.
     * @return Whether the action was Successful
     */
    public boolean unloadWorld(World world, boolean save);

    /**
     * Gets the world with the given name
     *
     * @param name Name of the world to retrieve
     * @return World with the given name, or null if none exists
     */
    public World getWorld(String name);

    /**
     * Gets the world from the given Unique ID
     *
     * @param uid Unique ID of the world to retrieve.
     * @return World with the given Unique ID, or null if none exists.
     */
    public World getWorld(UUID uid);

    /**
     * Gets the map from the given item ID.
     *
     * @param id ID of the map to get.
     * @return The MapView if it exists, or null otherwise.
     */
    public MapView getMap(short id);

    /**
     * Create a new map with an automatically assigned ID.
     *
     * @param world The world the map will belong to.
     * @return The MapView just created.
     */
    public MapView createMap(World world);

    /**
     * Reloads the server, refreshing settings and plugin information
     */
    public void reload();

    /**
     * Returns the primary logger associated with this server instance
     *
     * @return Logger associated with this server
     */
    public Logger getLogger();

    /**
     * Gets a {@link PluginCommand} with the given name or alias
     *
     * @param name Name of the command to retrieve
     * @return PluginCommand if found, otherwise null
     */
    public PluginCommand getPluginCommand(String name);

    /**
     * Writes loaded players to disk
     */
    public void savePlayers();

    /**
     * Dispatches a command on the server, and executes it if found.
     *
     * @param sender The apparent sender of the command
     * @param commandLine command + arguments. Example: "test abc 123"
     * @return returns false if no target is found.
     * @throws CommandException Thrown when the executor for the given command fails with an unhandled exception
     */
    public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException;

    /**
     * Populates a given {@link ServerConfig} with values attributes to this server
     *
     * @param config ServerConfig to populate
     */
    public void configureDbConfig(ServerConfig config);

    /**
     * Adds a recipe to the crafting manager.
     *
     * @param recipe The recipe to add.
     * @return True if the recipe was added, false if it wasn't for some reason.
     */
    public boolean addRecipe(Recipe recipe);

    /**
     * Get a list of all recipes for a given item. The stack size is ignored in comparisons.
     * If the durability is -1, it will match any data value.
     *
     * @param result The item whose recipes you want
     * @return The list of recipes
     */
    public List<Recipe> getRecipesFor(ItemStack result);

    /**
     * Get an iterator through the list of crafting recipes.
     *
     * @return The iterator.
     */
    public Iterator<Recipe> recipeIterator();

    /**
     * Clears the list of crafting recipes.
     */
    public void clearRecipes();

    /**
     * Resets the list of crafting recipes to the default.
     */
    public void resetRecipes();

    /**
     * Gets a list of command aliases defined in the server properties.
     *
     * @return Map of aliases to command names
     */
    public Map<String, String[]> getCommandAliases();

    /**
     * Gets the radius, in blocks, around each worlds spawn point to protect
     *
     * @return Spawn radius, or 0 if none
     */
    public int getSpawnRadius();

    /**
     * Sets the radius, in blocks, around each worlds spawn point to protect
     *
     * @param value New spawn radius, or 0 if none
     */
    public void setSpawnRadius(int value);

    /**
     * Gets whether the Server is in online mode or not.
     *
     * @return Whether the server is in online mode.
     */
    public boolean getOnlineMode();

    /**
     * Gets whether this server allows flying or not.
     *
     * @return Whether this server allows flying or not.
     */
    public boolean getAllowFlight();

    /**
     * Gets whether to use vanilla (false) or exact behaviour (true).
     *
     * Vanilla behaviour: check for collisions and move the player if needed.
     * Exact behaviour: spawn players exactly where they should be.
     *
     * @return Whether to use vanilla (false) or exact behaviour (true).
     */
    public boolean useExactLoginLocation();

    /**
     * Shutdowns the server, stopping everything.
     */
    public void shutdown();

    /**
     * Broadcasts the specified message to every user with the given permission
     *
     * @param message Message to broadcast
     * @param permission Permission the users must have to receive the broadcast
     * @return Amount of users who received the message
     */
    public int broadcast(String message, String permission);

    /**
     * Gets the player by the given name, regardless if they are offline or online.
     * <p />
     * This will return an object even if the player does not exist. To this method, all players will exist.
     *
     * @param name Name of the player to retrieve
     * @return OfflinePlayer object
     */
    public OfflinePlayer getOfflinePlayer(String name);

    /**
     * Gets a set containing all current IPs that are banned
     *
     * @return Set containing banned IP addresses
     */
    public Set<String> getIPBans();

    /**
     * Bans the specified address from the server
     *
     * @param address IP address to ban
     */
    public void banIP(String address);

    /**
     * Unbans the specified address from the server
     *
     * @param address IP address to unban
     */
    public void unbanIP(String address);

    /**
     * Gets a set containing all banned players
     *
     * @return Set containing banned players
     */
    public Set<OfflinePlayer> getBannedPlayers();

    /**
     * Gets a set containing all player operators
     *
     * @return Set containing player operators
     */
    public Set<OfflinePlayer> getOperators();

    /**
     * Gets the default {@link GameMode} for new players
     *
     * @return Default game mode
     */
    public GameMode getDefaultGameMode();

    /**
     * Sets the default {@link GameMode} for new players
     *
     * @param mode New game mode
     */
    public void setDefaultGameMode(GameMode mode);

    /**
     * Gets the {@link ConsoleCommandSender} that may be used as an input source
     * for this server.
     *
     * @return The Console CommandSender
     */
    public ConsoleCommandSender getConsoleSender();

    /**
     * Gets the folder that contains all of the various {@link World}s.
     *
     * @return World container folder
     */
    public File getWorldContainer();

    /**
     * Gets every player that has ever played on this server.
     *
     * @return Array containing all players
     */
    public OfflinePlayer[] getOfflinePlayers();

    /**
     * Gets the {@link Messenger} responsible for this server.
     *
     * @return Messenger responsible for this server.
     */
    public Messenger getMessenger();

    /**
     * Gets the {@link HelpMap} providing help topics for this server.
     *
     * @return The server's HelpMap.
     */
    public HelpMap getHelpMap();

    /**
     * Creates an empty inventory of the specified type. If the type is {@link InventoryType#CHEST},
     * the new inventory has a size of 27; otherwise the new inventory has the normal size for
     * its type.
     * @param owner The holder of the inventory; can be null if there's no holder.
     * @param type The type of inventory to create.
     * @return The new inventory.
     */
    Inventory createInventory(InventoryHolder owner, InventoryType type);

    /**
     * Creates an empty inventory of type {@link InventoryType#CHEST} with the specified size.
     * @param owner The holder of the inventory; can be null if there's no holder.
     * @param size The size of inventory to create; must be a multiple of 9.
     * @return The new inventory.
     * @throws IllegalArgumentException If the size is not a multiple of 9.
     */
    Inventory createInventory(InventoryHolder owner, int size);

    /**
     * Creates an empty inventory of type {@link InventoryType#CHEST} with the specified size and title.
     * @param owner The holder of the inventory; can be null if there's no holder.
     * @param size The size of inventory to create; must be a multiple of 9.
     * @param title The title of the inventory, to be displayed when it is viewed.
     * @return The new inventory.
     * @throws IllegalArgumentException If the size is not a multiple of 9.
     */
    Inventory createInventory(InventoryHolder owner, int size, String title);

    /**
     * Gets user-specified limit for number of monsters that can spawn in a chunk
     * @returns The monster spawn limit
     */
    int getMonsterSpawnLimit();

    /**
     * Gets user-specified limit for number of animals that can spawn in a chunk
     * @returns The animal spawn limit
     */
    int getAnimalSpawnLimit();

    /**
     * Gets user-specified limit for number of water animals that can spawn in a chunk
     * @returns The water animal spawn limit
     */
    int getWaterAnimalSpawnLimit();

    /**
     * Returns true if the current {@link Thread} is the server's primary thread
     */
    boolean isPrimaryThread();

    /**
     * Gets the message that is displayed on the server list
     *
     * @returns the servers MOTD
     */
    String getMotd();

    /**
     * Gets the current warning state for the server
     *
     * @return The configured WarningState
     */
    public WarningState getWarningState();
}