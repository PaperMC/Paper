
package org.bukkit;

import org.bukkit.entity.Player;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.command.PluginCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Represents a server implementation
 */
public interface Server {
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
     * @return The port number of this servers
     */
    public int getPort();

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
     * Broadcast a message to all players.
     *
     * @param message the message
     * @return the number of players
     */
    public int broadcastMessage(String message);

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
     * Gets the world with the given name
     *
     * @param name Name of the world to retrieve
     * @return World with the given name, or null if none exists
     */
    public World getWorld(String name);

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
}
