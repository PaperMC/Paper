package com.destroystokyo.paper;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsManager;
import org.spigotmc.SpigotConfig;

public class PaperConfig {

    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for Paper.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Paper,\n"
            + "join us in our Discord or IRC channel.\n"
            + "\n"
            + "Discord: https://paperdiscord.emc.gs\n"
            + "IRC: #paper @ irc.spi.gt ( http://irc.spi.gt/iris/?channels=paper )\n"
            + "Website: https://papermc.io/ \n"
            + "Docs: https://paper.readthedocs.org/ \n";
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    private static boolean verbose;
    private static boolean fatalError;
    /*========================================================================*/
    private static boolean metricsStarted;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException ex) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load paper.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);
        verbose = getBoolean("verbose", false);

        commands = new HashMap<String, Command>();
        commands.put("paper", new PaperCommand("paper"));

        version = getInt("config-version", 20);
        set("config-version", 20);
        readConfig(PaperConfig.class, null);
    }

    protected static void logError(String s) {
        Bukkit.getLogger().severe(s);
    }

    protected static void fatal(String s) {
        fatalError = true;
        throw new RuntimeException("Fatal paper.yml config error: " + s);
    }

    protected static void log(String s) {
        if (verbose) {
            Bukkit.getLogger().info(s);
        }
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), "Paper", entry.getValue());
        }

        if (!metricsStarted) {
            Metrics.PaperMetrics.startMetrics();
            metricsStarted = true;
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
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
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern NOT_NUMERIC = Pattern.compile("[^-\\d.]");
    public static int getSeconds(String str) {
        str = SPACE.matcher(str).replaceAll("");
        final char unit = str.charAt(str.length() - 1);
        str = NOT_NUMERIC.matcher(str).replaceAll("");
        double num;
        try {
            num = Double.parseDouble(str);
        } catch (Exception e) {
            num = 0D;
        }
        switch (unit) {
            case 'd': num *= (double) 60*60*24; break;
            case 'h': num *= (double) 60*60; break;
            case 'm': num *= (double) 60; break;
            default: case 's': break;
        }
        return (int) num;
    }

    protected static String timeSummary(int seconds) {
        String time = "";

        if (seconds > 60 * 60 * 24) {
            time += TimeUnit.SECONDS.toDays(seconds) + "d";
            seconds %= 60 * 60 * 24;
        }

        if (seconds > 60 * 60) {
            time += TimeUnit.SECONDS.toHours(seconds) + "h";
            seconds %= 60 * 60;
        }

        if (seconds > 0) {
            time += TimeUnit.SECONDS.toMinutes(seconds) + "m";
        }
        return time;
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static float getFloat(String path, float def) {
        // TODO: Figure out why getFloat() always returns the default value.
        return (float) getDouble(path, (double) def);
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return (List<T>) config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    public static String timingsServerName;
    private static void timings() {
        boolean timings = getBoolean("timings.enabled", true);
        boolean verboseTimings = getBoolean("timings.verbose", true);
        TimingsManager.privacy = getBoolean("timings.server-name-privacy", false);
        TimingsManager.hiddenConfigs = getList("timings.hidden-config-entries", Lists.newArrayList("database", "settings.bungeecord-addresses"));
        int timingHistoryInterval = getInt("timings.history-interval", 300);
        int timingHistoryLength = getInt("timings.history-length", 3600);
        timingsServerName = getString("timings.server-name", "Unknown Server");


        Timings.setVerboseTimingsEnabled(verboseTimings);
        Timings.setTimingsEnabled(timings);
        Timings.setHistoryInterval(timingHistoryInterval * 20);
        Timings.setHistoryLength(timingHistoryLength * 20);

        log("Timings: " + timings +
                " - Verbose: " + verboseTimings +
                " - Interval: " + timeSummary(Timings.getHistoryInterval() / 20) +
                " - Length: " + timeSummary(Timings.getHistoryLength() / 20) +
                " - Server Name: " + timingsServerName);
    }

    public static boolean loadPermsBeforePlugins = true;
    private static void loadPermsBeforePlugins() {
        loadPermsBeforePlugins = getBoolean("settings.load-permissions-yml-before-plugins", true);
    }

    public static int regionFileCacheSize = 256;
    private static void regionFileCacheSize() {
        regionFileCacheSize = Math.max(getInt("settings.region-file-cache-size", 256), 4);
    }

    public static boolean enablePlayerCollisions = true;
    private static void enablePlayerCollisions() {
        enablePlayerCollisions = getBoolean("settings.enable-player-collisions", true);
    }

    public static boolean saveEmptyScoreboardTeams = false;
    private static void saveEmptyScoreboardTeams() {
        saveEmptyScoreboardTeams = getBoolean("settings.save-empty-scoreboard-teams", false);
    }

    public static boolean bungeeOnlineMode = true;
    private static void bungeeOnlineMode() {
        bungeeOnlineMode = getBoolean("settings.bungee-online-mode", true);
    }

    public static boolean isProxyOnlineMode() {
        return Bukkit.getOnlineMode() || (SpigotConfig.bungee && bungeeOnlineMode);
    }

    public static int packetInSpamThreshold = 300;
    private static void packetInSpamThreshold() {
        if (version < 11) {
            int oldValue = getInt("settings.play-in-use-item-spam-threshold", 300);
            set("settings.incoming-packet-spam-threshold", oldValue);
        }
        packetInSpamThreshold = getInt("settings.incoming-packet-spam-threshold", 300);
    }

    public static String flyingKickPlayerMessage = "Flying is not enabled on this server";
    public static String flyingKickVehicleMessage = "Flying is not enabled on this server";
    private static void flyingKickMessages() {
        flyingKickPlayerMessage = getString("messages.kick.flying-player", flyingKickPlayerMessage);
        flyingKickVehicleMessage = getString("messages.kick.flying-vehicle", flyingKickVehicleMessage);
    }

    public static boolean suggestPlayersWhenNullTabCompletions = true;
    private static void suggestPlayersWhenNull() {
        suggestPlayersWhenNullTabCompletions = getBoolean("settings.suggest-player-names-when-null-tab-completions", suggestPlayersWhenNullTabCompletions);
    }
}
