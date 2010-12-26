
package org.bukkit.plugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.bukkit.Server;
import java.util.regex.Pattern;
import org.bukkit.event.player.PlayerEvent;

/**
 * Handles all plugin management from the Server
 */
public final class SimplePluginManager implements PluginManager {
    private final Server server;
    private final Map<Pattern, PluginLoader> fileAssociations = new HashMap<Pattern, PluginLoader>();
    private final List<Plugin> plugins = new ArrayList<Plugin>();
    private final Map<String, Plugin> lookupNames = new HashMap<String, Plugin>();
    private final Map<PlayerEvent.EventType, List<RegisteredListener>> playerListeners = new EnumMap<PlayerEvent.EventType, List<RegisteredListener>>(PlayerEvent.EventType.class);

    public SimplePluginManager(Server instance) {
        server = instance;
    }

    /**
     * Registers the specified plugin loader
     *
     * @param loader Class name of the PluginLoader to register
     * @throws IllegalArgumentException Thrown when the given Class is not a valid PluginLoader
     */
    public void RegisterInterface(Class loader) throws IllegalArgumentException {
        PluginLoader instance;

        if (PluginLoader.class.isAssignableFrom(loader)) {
            Constructor constructor;
            try {
                constructor = loader.getConstructor(Server.class);
                instance = (PluginLoader) constructor.newInstance(server);
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(String.format("Class %s does not have a public %s(Server) constructor", loader.getName()), ex);
            } catch (Exception ex) {
                throw new IllegalArgumentException(String.format("Unexpected exception %s while attempting to construct a new instance of %s", ex.getClass().getName(), loader.getName()), ex);
            }
        } else {
            throw new IllegalArgumentException(String.format("Class %s does not implement interface PluginLoader", loader.getName()));
        }

        Pattern[] patterns = instance.getPluginFileFilters();

        for (Pattern pattern : patterns) {
            fileAssociations.put(pattern, instance);
        }
    }

    /**
     * Loads the plugins contained within the specified directory
     *
     * @param directory Directory to check for plugins
     * @return A list of all plugins loaded
     */
    public Plugin[] loadPlugins(File directory) {
        List<Plugin> result = new ArrayList<Plugin>();
        File[] files = directory.listFiles();

        for (File file : files) {
            Plugin plugin = null;

            try {
                plugin = loadPlugin(file);
            } catch (InvalidPluginException ex) {
                Logger.getLogger(SimplePluginManager.class.getName()).log(Level.SEVERE, "Could not load " + file.getPath() + " in " + directory.getPath(), ex);
            }
            
            if (plugin != null) {
                result.add(plugin);
            }
        }

        return result.toArray(new Plugin[result.size()]);
    }

    /**
     * Loads the plugin in the specified file
     *
     * File must be valid according to the current enabled Plugin interfaces
     *
     * @param file File containing the plugin to load
     * @return The Plugin loaded, or null if it was invalid
     * @throws InvalidPluginException Thrown when the specified file is not a valid plugin
     */
    public Plugin loadPlugin(File file) throws InvalidPluginException {
        Set<Pattern> filters = fileAssociations.keySet();
        Plugin result = null;

        for (Pattern filter : filters) {
            String name = file.getName();
            Matcher match = filter.matcher(name);

            if (match.find()) {
                PluginLoader loader = fileAssociations.get(filter);
                result = loader.loadPlugin(file);
            }
        }

        if (result != null) {
            plugins.add(result);
            lookupNames.put(result.getDescription().getName(), result);
        }

        return result;
    }
    
    /**
     * Checks if the given plugin is loaded and returns it when applicable
     *
     * Please note that the name of the plugin is case-sensitive
     *
     * @param name Name of the plugin to check
     * @return Plugin if it exists, otherwise null
     */
    public Plugin getPlugin(String name) {
        return lookupNames.get(name);
    }

    /**
     * Checks if the given plugin is enabled or not
     *
     * Please note that the name of the plugin is case-sensitive.
     *
     * @param name Name of the plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    public boolean isPluginEnabled(String name) {
        Plugin plugin = getPlugin(name);

        return isPluginEnabled(plugin);
    }

    /**
     * Checks if the given plugin is enabled or not
     *
     * @param plugin Plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    public boolean isPluginEnabled(Plugin plugin) {
        if ((plugin != null) && (plugins.contains(plugin))) {
            return plugin.isEnabled();
        } else {
            return false;
        }
    }

    /**
     * Calls a player related event with the given details
     *
     * @param type Type of player related event to call
     * @param event Event details
     */
    public void callEvent(PlayerEvent.EventType type, PlayerEvent event) {
        List<RegisteredListener> listeners = playerListeners.get(type);

        if (listeners != null) {
            for (RegisteredListener registration : listeners) {
                Plugin plugin = registration.getPlugin();
                PluginLoader loader = plugin.getPluginLoader();

                if (plugin.isEnabled()) {
                    loader.callEvent(registration, type, event);
                }
            }
        }
    }
}
