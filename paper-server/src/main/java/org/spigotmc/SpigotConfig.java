package org.spigotmc;

import com.google.common.base.Throwables;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotConfig {

    private static File CONFIG_FILE;
    private static final String HEADER = """
        This is the Spigot configuration file for Paper.
        As you can see, there's tons to configure. Some options may impact gameplay, so use
        with caution, and make sure you know what each option does before configuring.
        
        If you need help with the configuration or have any questions related to Paper,
        join us in our Discord or check the docs page.
        
        File Reference: https://docs.papermc.io/paper/reference/spigot-configuration/
        Docs: https://docs.papermc.io/
        Discord: https://discord.gg/papermc
        Website: https://papermc.io/
        """;
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    /*========================================================================*/

    public static void init(File configFile) {
        SpigotConfig.CONFIG_FILE = configFile;
        SpigotConfig.config = new YamlConfiguration();
        try {
            SpigotConfig.config.load(SpigotConfig.CONFIG_FILE);
        } catch (IOException ignored) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load spigot.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }

        SpigotConfig.config.options().header(SpigotConfig.HEADER);
        SpigotConfig.config.options().copyDefaults(true);

        SpigotConfig.commands = new HashMap<>();
        SpigotConfig.commands.put("spigot", new SpigotCommand("spigot"));

        SpigotConfig.version = SpigotConfig.getInt("config-version", 12);
        SpigotConfig.set("config-version", 12);
        SpigotConfig.readConfig(SpigotConfig.class, null);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : SpigotConfig.commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Spigot", entry.getValue());
        }
    }

    public static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            SpigotConfig.config.save(SpigotConfig.CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + SpigotConfig.CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        SpigotConfig.config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        SpigotConfig.config.addDefault(path, def);
        return SpigotConfig.config.getBoolean(path, SpigotConfig.config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        SpigotConfig.config.addDefault(path, def);
        return SpigotConfig.config.getInt(path, SpigotConfig.config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        SpigotConfig.config.addDefault(path, def);
        return (List<T>) SpigotConfig.config.getList(path, SpigotConfig.config.getList(path));
    }

    private static String getString(String path, String def) {
        SpigotConfig.config.addDefault(path, def);
        return SpigotConfig.config.getString(path, SpigotConfig.config.getString(path));
    }

    private static double getDouble(String path, double def) {
        SpigotConfig.config.addDefault(path, def);
        return SpigotConfig.config.getDouble(path, SpigotConfig.config.getDouble(path));
    }

    public static boolean logCommands;
    private static void logCommands() {
        SpigotConfig.logCommands = SpigotConfig.getBoolean("commands.log", true);
    }

    public static int tabComplete;
    public static boolean sendNamespaced;
    private static void tabComplete() {
        if (SpigotConfig.version < 6) {
            boolean oldValue = SpigotConfig.getBoolean("commands.tab-complete", true);
            if (oldValue) {
                SpigotConfig.set("commands.tab-complete", 0);
            } else {
                SpigotConfig.set("commands.tab-complete", -1);
            }
        }
        SpigotConfig.tabComplete = SpigotConfig.getInt("commands.tab-complete", 0);
        SpigotConfig.sendNamespaced = SpigotConfig.getBoolean("commands.send-namespaced", true);
    }

    public static String whitelistMessage;
    public static String unknownCommandMessage;
    public static String serverFullMessage;
    public static String outdatedClientMessage = "Outdated client! Please use {0}";
    public static String outdatedServerMessage = "Outdated server! I'm still on {0}";

    private static String transform(String s) {
        return ChatColor.translateAlternateColorCodes('&', s).replaceAll("\\\\n", "\n");
    }

    private static void messages() {
        if (SpigotConfig.version < 8) {
            SpigotConfig.set("messages.outdated-client", SpigotConfig.outdatedClientMessage);
            SpigotConfig.set("messages.outdated-server", SpigotConfig.outdatedServerMessage);
        }

        SpigotConfig.whitelistMessage = SpigotConfig.transform(SpigotConfig.getString("messages.whitelist", "You are not whitelisted on this server!"));
        SpigotConfig.unknownCommandMessage = SpigotConfig.transform(SpigotConfig.getString("messages.unknown-command", "Unknown command. Type \"/help\" for help."));
        SpigotConfig.serverFullMessage = SpigotConfig.transform(SpigotConfig.getString("messages.server-full", "The server is full!"));
        SpigotConfig.outdatedClientMessage = SpigotConfig.transform(SpigotConfig.getString("messages.outdated-client", SpigotConfig.outdatedClientMessage));
        SpigotConfig.outdatedServerMessage = SpigotConfig.transform(SpigotConfig.getString("messages.outdated-server", SpigotConfig.outdatedServerMessage));
    }

    public static int timeoutTime = 60;
    public static boolean restartOnCrash = true;
    public static String restartScript = "./start.sh";
    public static String restartMessage;
    private static void watchdog() {
        SpigotConfig.timeoutTime = SpigotConfig.getInt("settings.timeout-time", SpigotConfig.timeoutTime);
        SpigotConfig.restartOnCrash = SpigotConfig.getBoolean("settings.restart-on-crash", SpigotConfig.restartOnCrash);
        SpigotConfig.restartScript = SpigotConfig.getString("settings.restart-script", SpigotConfig.restartScript);
        SpigotConfig.restartMessage = SpigotConfig.transform(SpigotConfig.getString("messages.restart", "Server is restarting"));
        SpigotConfig.commands.put("restart", new RestartCommand("restart"));
    }

    public static boolean bungee;
    private static void bungee() {
        if (SpigotConfig.version < 4) {
            SpigotConfig.set("settings.bungeecord", false);
            System.out.println("Outdated config, disabling BungeeCord support!");
        }
        SpigotConfig.bungee = SpigotConfig.getBoolean("settings.bungeecord", false);
    }

    private static void nettyThreads() {
        int count = SpigotConfig.getInt("settings.netty-threads", 4);
        System.setProperty("io.netty.eventLoopThreads", Integer.toString(count));
        Bukkit.getLogger().log(Level.INFO, "Using {0} threads for Netty based IO", count);
    }

    public static boolean disableStatSaving;
    public static Map<ResourceLocation, Integer> forcedStats = new HashMap<>();

    private static void stats() {
        SpigotConfig.disableStatSaving = SpigotConfig.getBoolean("stats.disable-saving", false);

        if (!SpigotConfig.config.contains("stats.forced-stats")) {
            SpigotConfig.config.createSection("stats.forced-stats");
        }

        ConfigurationSection section = SpigotConfig.config.getConfigurationSection("stats.forced-stats");
        for (String name : section.getKeys(true)) {
            if (section.isInt(name)) {
                try {
                    ResourceLocation key = ResourceLocation.parse(name);
                    if (BuiltInRegistries.CUSTOM_STAT.get(key) == null) {
                        Bukkit.getLogger().log(Level.WARNING, "Ignoring non existent stats.forced-stats " + name);
                        continue;
                    }
                    SpigotConfig.forcedStats.put(key, section.getInt(name));
                } catch (Exception ex) {
                    Bukkit.getLogger().log(Level.WARNING, "Ignoring invalid stats.forced-stats " + name);
                }
            }
        }
    }

    private static void tpsCommand() {
        SpigotConfig.commands.put("tps", new TicksPerSecondCommand("tps"));
    }

    public static int playerSample;
    private static void playerSample() {
        SpigotConfig.playerSample = Math.max(SpigotConfig.getInt("settings.sample-count", 12), 0); // Paper - Avoid negative counts
        Bukkit.getLogger().log(Level.INFO, "Server Ping Player Sample Count: {0}", playerSample); // Paper - Use logger
    }

    public static int playerShuffle;
    private static void playerShuffle() {
        SpigotConfig.playerShuffle = SpigotConfig.getInt("settings.player-shuffle", 0);
    }

    public static boolean enableSpamExclusions = false;
    public static List<String> spamExclusions;
    private static void spamExclusions() {
        SpigotConfig.spamExclusions = SpigotConfig.getList("commands.spam-exclusions", List.of("/skill"));
        Object enabled = SpigotConfig.config.get("commands.enable-spam-exclusions");
        if (enabled instanceof Boolean value) {
            SpigotConfig.enableSpamExclusions = value;
        } else {
            if (spamExclusions.size() == 1 && Objects.equals(spamExclusions.getFirst(), "/skill")) {
                SpigotConfig.enableSpamExclusions = false;
                SpigotConfig.set("commands.enable-spam-exclusions", false);
            } else {
                SpigotConfig.enableSpamExclusions = true;
                SpigotConfig.set("commands.enable-spam-exclusions", true);
            }
        }

    }

    public static boolean silentCommandBlocks;
    private static void silentCommandBlocks() {
        SpigotConfig.silentCommandBlocks = SpigotConfig.getBoolean("commands.silent-commandblock-console", false);
    }

    public static Set<String> replaceCommands;
    private static void replaceCommands() {
        if (SpigotConfig.config.contains("replace-commands")) {
            SpigotConfig.set("commands.replace-commands", SpigotConfig.config.getStringList("replace-commands"));
            SpigotConfig.config.set("replace-commands", null);
        }
        SpigotConfig.replaceCommands = new HashSet<>(SpigotConfig.getList("commands.replace-commands",
            Arrays.asList("setblock", "summon", "testforblock", "tellraw")));
    }

    public static int userCacheCap;
    private static void userCacheCap() {
        SpigotConfig.userCacheCap = SpigotConfig.getInt("settings.user-cache-size", 1000);
    }

    public static boolean saveUserCacheOnStopOnly;
    private static void saveUserCacheOnStopOnly() {
        SpigotConfig.saveUserCacheOnStopOnly = SpigotConfig.getBoolean("settings.save-user-cache-on-stop-only", false);
    }

    public static double movedWronglyThreshold;
    private static void movedWronglyThreshold() {
        SpigotConfig.movedWronglyThreshold = SpigotConfig.getDouble("settings.moved-wrongly-threshold", 0.0625D);
    }

    public static double movedTooQuicklyMultiplier;
    private static void movedTooQuicklyMultiplier() {
        SpigotConfig.movedTooQuicklyMultiplier = SpigotConfig.getDouble("settings.moved-too-quickly-multiplier", 10.0D);
    }

    public static double maxAbsorption = 2048;
    public static double maxHealth = 1024;
    public static double movementSpeed = 1024;
    public static double attackDamage = 2048;
    private static void attributeMaxes() {
        SpigotConfig.maxAbsorption = SpigotConfig.getDouble("settings.attribute.maxAbsorption.max", SpigotConfig.maxAbsorption);
        ((RangedAttribute) Attributes.MAX_ABSORPTION.value()).maxValue = SpigotConfig.maxAbsorption;
        SpigotConfig.maxHealth = SpigotConfig.getDouble("settings.attribute.maxHealth.max", SpigotConfig.maxHealth);
        ((RangedAttribute) Attributes.MAX_HEALTH.value()).maxValue = SpigotConfig.maxHealth;
        SpigotConfig.movementSpeed = SpigotConfig.getDouble("settings.attribute.movementSpeed.max", SpigotConfig.movementSpeed);
        ((RangedAttribute) Attributes.MOVEMENT_SPEED.value()).maxValue = SpigotConfig.movementSpeed;
        SpigotConfig.attackDamage = SpigotConfig.getDouble("settings.attribute.attackDamage.max", SpigotConfig.attackDamage);
        ((RangedAttribute) Attributes.ATTACK_DAMAGE.value()).maxValue = SpigotConfig.attackDamage;
    }

    public static boolean debug;
    private static void debug() {
        SpigotConfig.debug = SpigotConfig.getBoolean("settings.debug", false);

        if (SpigotConfig.debug && !LogManager.getRootLogger().isTraceEnabled()) {
            // Enable debug logging
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            Configuration conf = ctx.getConfiguration();
            conf.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(org.apache.logging.log4j.Level.ALL);
            ctx.updateLoggers(conf);
        }

        if (LogManager.getRootLogger().isTraceEnabled()) {
            Bukkit.getLogger().info("Debug logging is enabled");
        }
    }

    public static boolean disableAdvancementSaving;
    public static List<String> disabledAdvancements;
    private static void disabledAdvancements() {
        SpigotConfig.disableAdvancementSaving = SpigotConfig.getBoolean("advancements.disable-saving", false);
        SpigotConfig.disabledAdvancements = SpigotConfig.getList("advancements.disabled", List.of("minecraft:story/disabled"));
    }

    public static boolean logVillagerDeaths;
    public static boolean logNamedDeaths;
    private static void logDeaths() {
        SpigotConfig.logVillagerDeaths = SpigotConfig.getBoolean("settings.log-villager-deaths", true);
        SpigotConfig.logNamedDeaths = SpigotConfig.getBoolean("settings.log-named-deaths", true);
    }

    public static boolean disablePlayerDataSaving;
    private static void disablePlayerDataSaving() {
        SpigotConfig.disablePlayerDataSaving = SpigotConfig.getBoolean("players.disable-saving", false);
    }

    public static boolean belowZeroGenerationInExistingChunks;
    private static void belowZeroGenerationInExistingChunks() {
        SpigotConfig.belowZeroGenerationInExistingChunks = SpigotConfig.getBoolean("world-settings.default.below-zero-generation-in-existing-chunks", true);
    }
}
