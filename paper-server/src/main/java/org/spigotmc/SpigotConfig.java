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
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotConfig
{

    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for Spigot.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "For a reference for any variable inside this file, check out the Spigot wiki at\n"
            + "http://www.spigotmc.org/wiki/spigot-configuration/\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Spigot,\n"
            + "join us at the Discord or drop by our forums and leave a post.\n"
            + "\n"
            + "Discord: https://www.spigotmc.org/go/discord\n"
            + "Forums: http://www.spigotmc.org/\n";
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    /*========================================================================*/
    private static Metrics metrics;

    public static void init(File configFile)
    {
        SpigotConfig.CONFIG_FILE = configFile;
        SpigotConfig.config = new YamlConfiguration();
        try
        {
            SpigotConfig.config.load( SpigotConfig.CONFIG_FILE );
        } catch ( IOException ex )
        {
        } catch ( InvalidConfigurationException ex )
        {
            Bukkit.getLogger().log( Level.SEVERE, "Could not load spigot.yml, please correct your syntax errors", ex );
            throw Throwables.propagate( ex );
        }

        SpigotConfig.config.options().header( SpigotConfig.HEADER );
        SpigotConfig.config.options().copyDefaults( true );

        SpigotConfig.commands = new HashMap<String, Command>();
        SpigotConfig.commands.put( "spigot", new SpigotCommand( "spigot" ) );

        SpigotConfig.version = SpigotConfig.getInt( "config-version", 12 );
        SpigotConfig.set( "config-version", 12 );
        SpigotConfig.readConfig( SpigotConfig.class, null );
    }

    public static void registerCommands()
    {
        for ( Map.Entry<String, Command> entry : SpigotConfig.commands.entrySet() )
        {
            MinecraftServer.getServer().server.getCommandMap().register( entry.getKey(), "Spigot", entry.getValue() );
        }

        if ( SpigotConfig.metrics == null )
        {
            try
            {
                SpigotConfig.metrics = new Metrics();
                SpigotConfig.metrics.start();
            } catch ( IOException ex )
            {
                Bukkit.getServer().getLogger().log( Level.SEVERE, "Could not start metrics service", ex );
            }
        }
    }

    static void readConfig(Class<?> clazz, Object instance)
    {
        for ( Method method : clazz.getDeclaredMethods() )
        {
            if ( Modifier.isPrivate( method.getModifiers() ) )
            {
                if ( method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE )
                {
                    try
                    {
                        method.setAccessible( true );
                        method.invoke( instance );
                    } catch ( InvocationTargetException ex )
                    {
                        throw Throwables.propagate( ex.getCause() );
                    } catch ( Exception ex )
                    {
                        Bukkit.getLogger().log( Level.SEVERE, "Error invoking " + method, ex );
                    }
                }
            }
        }

        try
        {
            SpigotConfig.config.save( SpigotConfig.CONFIG_FILE );
        } catch ( IOException ex )
        {
            Bukkit.getLogger().log( Level.SEVERE, "Could not save " + SpigotConfig.CONFIG_FILE, ex );
        }
    }

    private static void set(String path, Object val)
    {
        SpigotConfig.config.set( path, val );
    }

    private static boolean getBoolean(String path, boolean def)
    {
        SpigotConfig.config.addDefault( path, def );
        return SpigotConfig.config.getBoolean( path, SpigotConfig.config.getBoolean( path ) );
    }

    private static int getInt(String path, int def)
    {
        SpigotConfig.config.addDefault( path, def );
        return SpigotConfig.config.getInt( path, SpigotConfig.config.getInt( path ) );
    }

    private static <T> List getList(String path, T def)
    {
        SpigotConfig.config.addDefault( path, def );
        return (List<T>) SpigotConfig.config.getList( path, SpigotConfig.config.getList( path ) );
    }

    private static String getString(String path, String def)
    {
        SpigotConfig.config.addDefault( path, def );
        return SpigotConfig.config.getString( path, SpigotConfig.config.getString( path ) );
    }

    private static double getDouble(String path, double def)
    {
        SpigotConfig.config.addDefault( path, def );
        return SpigotConfig.config.getDouble( path, SpigotConfig.config.getDouble( path ) );
    }

    public static boolean logCommands;
    private static void logCommands()
    {
        SpigotConfig.logCommands = SpigotConfig.getBoolean( "commands.log", true );
    }

    public static int tabComplete;
    public static boolean sendNamespaced;
    private static void tabComplete()
    {
        if ( SpigotConfig.version < 6 )
        {
            boolean oldValue = SpigotConfig.getBoolean( "commands.tab-complete", true );
            if ( oldValue )
            {
                SpigotConfig.set( "commands.tab-complete", 0 );
            } else
            {
                SpigotConfig.set( "commands.tab-complete", -1 );
            }
        }
        SpigotConfig.tabComplete = SpigotConfig.getInt( "commands.tab-complete", 0 );
        SpigotConfig.sendNamespaced = SpigotConfig.getBoolean( "commands.send-namespaced", true );
    }

    public static String whitelistMessage;
    public static String unknownCommandMessage;
    public static String serverFullMessage;
    public static String outdatedClientMessage = "Outdated client! Please use {0}";
    public static String outdatedServerMessage = "Outdated server! I\'m still on {0}";
    private static String transform(String s)
    {
        return ChatColor.translateAlternateColorCodes( '&', s ).replaceAll( "\\\\n", "\n" );
    }
    private static void messages()
    {
        if (SpigotConfig.version < 8)
        {
            SpigotConfig.set( "messages.outdated-client", SpigotConfig.outdatedClientMessage );
            SpigotConfig.set( "messages.outdated-server", SpigotConfig.outdatedServerMessage );
        }

        SpigotConfig.whitelistMessage = SpigotConfig.transform( SpigotConfig.getString( "messages.whitelist", "You are not whitelisted on this server!" ) );
        SpigotConfig.unknownCommandMessage = SpigotConfig.transform( SpigotConfig.getString( "messages.unknown-command", "Unknown command. Type \"/help\" for help." ) );
        SpigotConfig.serverFullMessage = SpigotConfig.transform( SpigotConfig.getString( "messages.server-full", "The server is full!" ) );
        SpigotConfig.outdatedClientMessage = SpigotConfig.transform( SpigotConfig.getString( "messages.outdated-client", SpigotConfig.outdatedClientMessage ) );
        SpigotConfig.outdatedServerMessage = SpigotConfig.transform( SpigotConfig.getString( "messages.outdated-server", SpigotConfig.outdatedServerMessage ) );
    }
}
