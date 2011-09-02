package org.bukkit;

import com.avaje.ebean.config.ServerConfig;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;

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
     *
     * This cannot be done if the Server is already set.
     *
     * @param server Server instance
     */
    public static void setServer(Server server) {
        if (Bukkit.server != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");
        }

        Bukkit.server = server;
        server.getLogger().info("This server is running " + getName() + " version " + getVersion());
    }

    public static String getName() {
        return server.getName();
    }

    public static String getVersion() {
        return server.getVersion();
    }

    public static Player[] getOnlinePlayers() {
        return server.getOnlinePlayers();
    }

    public static int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    public static int getPort() {
        return server.getPort();
    }

    public static int getViewDistance() {
        return server.getViewDistance();
    }

    public static String getIp() {
        return server.getIp();
    }

    public static String getServerName() {
        return server.getServerName();
    }

    public static String getServerId() {
        return server.getServerId();
    }

    public static boolean getAllowNether() {
        return server.getAllowNether();
    }

    public static boolean hasWhitelist() {
        return server.hasWhitelist();
    }

    public static int broadcastMessage(String message) {
        return server.broadcastMessage(message);
    }

    public static String getUpdateFolder() {
        return server.getUpdateFolder();
    }

    public static Player getPlayer(String name) {
        return server.getPlayer(name);
    }

    public static List<Player> matchPlayer(String name) {
        return server.matchPlayer(name);
    }

    public static PluginManager getPluginManager() {
        return server.getPluginManager();
    }

    public static BukkitScheduler getScheduler() {
        return server.getScheduler();
    }

    public static ServicesManager getServicesManager() {
        return server.getServicesManager();
    }

    public static List<World> getWorlds() {
        return server.getWorlds();
    }

    public static World createWorld(String name, Environment environment) {
        return server.createWorld(name, environment);
    }

    public static World createWorld(String name, Environment environment, long seed) {
        return server.createWorld(name, environment, seed);
    }

    public static World createWorld(String name, Environment environment, ChunkGenerator generator) {
        return server.createWorld(name, environment, generator);
    }

    public static World createWorld(String name, Environment environment, long seed, ChunkGenerator generator) {
        return server.createWorld(name, environment, seed, generator);
    }

    public static boolean unloadWorld(String name, boolean save) {
        return server.unloadWorld(name, save);
    }

    public static boolean unloadWorld(World world, boolean save) {
        return server.unloadWorld(world, save);
    }

    public static World getWorld(String name) {
        return server.getWorld(name);
    }

    public static World getWorld(UUID uid) {
        return server.getWorld(uid);
    }

    public static MapView getMap(short id) {
        return server.getMap(id);
    }

    public static MapView createMap(World world) {
        return server.createMap(world);
    }

    public static void reload() {
        server.reload();
    }

    public static Logger getLogger() {
        return server.getLogger();
    }

    public static PluginCommand getPluginCommand(String name) {
        return server.getPluginCommand(name);
    }

    public static void savePlayers() {
        server.savePlayers();
    }

    public static boolean dispatchCommand(CommandSender sender, String commandLine) {
        return server.dispatchCommand(sender, commandLine);
    }

    public static void configureDbConfig(ServerConfig config) {
        server.configureDbConfig(config);
    }

    public static boolean addRecipe(Recipe recipe) {
        return server.addRecipe(recipe);
    }

    public static Map<String, String[]> getCommandAliases() {
        return server.getCommandAliases();
    }

    public static int getSpawnRadius() {
        return server.getSpawnRadius();
    }

    public static void setSpawnRadius(int value) {
        server.setSpawnRadius(value);
    }

    public static boolean getOnlineMode() {
        return server.getOnlineMode();
    }

    public static boolean getAllowFlight() {
        return server.getAllowFlight();
    }

    public static void shutdown() {
        server.shutdown();
    }
}
