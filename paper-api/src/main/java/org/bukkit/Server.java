package org.bukkit;

import org.bukkit.generator.ChunkGenerator;
import com.avaje.ebean.config.ServerConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.command.PluginCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Represents a server implementation
 */
public interface Server {
    /**
     * Used for all administrative messages, such as an operator using a command.
     *
     * For use in {@link #broadcast(java.lang.String, java.lang.String)}
     */
    public static final String BROADCAST_CHANNEL_ADMINISTRATIVE = "bukkit.broadcast.admin";

    /**
     * Used for all announcement messages, such as informing users that a player has joined.
     *
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
     * Broadcast a message to all players.
     *
     * This is the same as calling {@link #broadcast(java.lang.String, java.lang.String)} to {@link #BROADCAST_CHANNEL_USERS}
     *
     * @param message the message
     * @return the number of players
     */
    public int broadcastMessage(String message);

    /**
     * Gets the name of the update folder. The update folder is used to safely update
     * plugins at the right moment on a plugin load.
     *
     * @return The name of the update folder
     */
    public String getUpdateFolder();

    /**
     * Gets a player object by the given username
     *
     * This method may not return objects for offline players
     *
     * @param name Name to look up
     * @return Player if it was found, otherwise null
     */
    public Player getPlayer(String name);

    /**
     * Attempts to match any players with the given name, and returns a list
     * of all possibly matches
     *
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
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @return Newly created or loaded World
     */
    public World createWorld(String name, World.Environment environment);

    /**
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @param seed Seed value to create the world with
     * @return Newly created or loaded World
     */
    public World createWorld(String name, World.Environment environment, long seed);

    /**
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @param generator ChunkGenerator to use in the construction of the new world
     * @return Newly created or loaded World
     */
    public World createWorld(String name, World.Environment environment, ChunkGenerator generator);

    /**
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @param seed Seed value to create the world with
     * @param generator ChunkGenerator to use in the construction of the new world
     * @return Newly created or loaded World
     */
    public World createWorld(String name, World.Environment environment, long seed, ChunkGenerator generator);

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
     * @param cmdLine command + arguments. Example: "test abc 123"
     * @return targetFound returns false if no target is found.
     * @throws CommandException Thrown when the executor for the given command fails with an unhandled exception
     */
    public boolean dispatchCommand(CommandSender sender, String commandLine);

    /**
     * Populates a given {@link ServerConfig} with values attributes to this server
     *
     * @param config ServerConfig to populate
     */
    public void configureDbConfig(ServerConfig config);

    /**
     * Adds a recipe to the crafting manager.
     * @param recipe The recipe to add.
     * @return True to indicate that the recipe was added.
     */
    public boolean addRecipe(Recipe recipe);

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
     *
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
}
