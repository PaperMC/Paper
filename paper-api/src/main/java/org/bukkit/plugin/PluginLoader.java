package org.bukkit.plugin;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a plugin loader, which handles direct access to specific types
 * of plugins
 */
public interface PluginLoader {

    /**
     * Loads the plugin contained in the specified file
     *
     * @param file File to attempt to load
     * @return Plugin that was contained in the specified file, or null if
     *     unsuccessful
     * @throws InvalidPluginException Thrown when the specified file is not a
     *     plugin
     * @throws UnknownDependencyException If a required dependency could not
     *     be found
     */
    @NotNull
    public Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, UnknownDependencyException;

    /**
     * Loads a PluginDescriptionFile from the specified file
     *
     * @param file File to attempt to load from
     * @return A new PluginDescriptionFile loaded from the plugin.yml in the
     *     specified file
     * @throws InvalidDescriptionException If the plugin description file
     *     could not be created
     */
    @NotNull
    public PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException;

    /**
     * Returns a list of all filename filters expected by this PluginLoader
     *
     * @return The filters
     */
    @NotNull
    public Pattern[] getPluginFileFilters();

    /**
     * Creates and returns registered listeners for the event classes used in
     * this listener
     *
     * @param listener The object that will handle the eventual call back
     * @param plugin The plugin to use when creating registered listeners
     * @return The registered listeners.
     */
    @NotNull
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener, @NotNull Plugin plugin);

    /**
     * Enables the specified plugin
     * <p>
     * Attempting to enable a plugin that is already enabled will have no
     * effect
     *
     * @param plugin Plugin to enable
     */
    public void enablePlugin(@NotNull Plugin plugin);

    /**
     * Disables the specified plugin
     * <p>
     * Attempting to disable a plugin that is not enabled will have no effect
     *
     * @param plugin Plugin to disable
     */
    public void disablePlugin(@NotNull Plugin plugin);
}
