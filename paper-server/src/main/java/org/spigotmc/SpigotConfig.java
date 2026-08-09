package org.spigotmc;

import com.google.common.base.Throwables;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.bukkit.Bukkit;
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

        SpigotConfig.version = SpigotConfig.getInt("config-version", 13);
        SpigotConfig.set("config-version", 13);
        SpigotConfig.readConfig(SpigotConfig.class, null);
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

    public static int tabComplete;
    public static boolean sendNamespaced;
    public static String whitelistMessage;
    public static String unknownCommandMessage;
    public static String serverFullMessage;
    public static String outdatedClientMessage = "Outdated client! Please use {0}";
    public static String outdatedServerMessage = "Outdated server! I'm still on {0}";
    public static int timeoutTime = 60;
    public static boolean restartOnCrash = true;
    public static String restartScript = "./start.sh";
    public static String restartMessage;
    public static boolean bungee;
    public static boolean disableStatSaving;
    public static Map<Identifier, Integer> forcedStats = new HashMap<>();
    public static int playerSample;
    public static int playerShuffle;
    public static boolean enableSpamExclusions = false;
    public static List<String> spamExclusions;

    public static boolean silentCommandBlocks;
    private static void silentCommandBlocks() {
        SpigotConfig.silentCommandBlocks = SpigotConfig.getBoolean("commands.silent-commandblock-console", false);
    }

    public static int userCacheCap;
    public static boolean saveUserCacheOnStopOnly;
    public static double movedWronglyThreshold;
    public static double movedTooQuicklyMultiplier;
    public static double maxAbsorption = 2048;
    public static double maxHealth = 1024;
    public static double movementSpeed = 1024;
    public static double attackDamage = 2048;

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

    public static boolean logVillagerDeaths;
    public static boolean logNamedDeaths;

    public static boolean disablePlayerDataSaving;

    public static boolean belowZeroGenerationInExistingChunks;
    private static void belowZeroGenerationInExistingChunks() {
        SpigotConfig.belowZeroGenerationInExistingChunks = SpigotConfig.getBoolean("world-settings.default.below-zero-generation-in-existing-chunks", true);
    }
}
