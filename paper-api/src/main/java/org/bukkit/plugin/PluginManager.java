package org.bukkit.plugin;

import java.io.File;
import java.util.Set;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles all plugin management from the Server
 */
public interface PluginManager {

    /**
     * Registers the specified plugin loader
     *
     * @param loader Class name of the PluginLoader to register
     * @throws IllegalArgumentException Thrown when the given Class is not a
     *     valid PluginLoader
     */
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException;

    /**
     * Checks if the given plugin is loaded and returns it when applicable
     * <p>
     * Please note that the name of the plugin is case-sensitive
     *
     * @param name Name of the plugin to check
     * @return Plugin if it exists, otherwise null
     */
    @Nullable
    public Plugin getPlugin(@NotNull String name);

    /**
     * Gets a list of all currently loaded plugins
     *
     * @return Array of Plugins
     */
    @NotNull
    public Plugin[] getPlugins();

    /**
     * Checks if the given plugin is enabled or not
     * <p>
     * Please note that the name of the plugin is case-sensitive.
     *
     * @param name Name of the plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    public boolean isPluginEnabled(@NotNull String name);

    /**
     * Checks if the given plugin is enabled or not
     *
     * @param plugin Plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    @Contract("null -> false")
    public boolean isPluginEnabled(@Nullable Plugin plugin);

    /**
     * Loads the plugin in the specified file
     * <p>
     * File must be valid according to the current enabled Plugin interfaces
     *
     * @param file File containing the plugin to load
     * @return The Plugin loaded, or null if it was invalid
     * @throws InvalidPluginException Thrown when the specified file is not a
     *     valid plugin
     * @throws InvalidDescriptionException Thrown when the specified file
     *     contains an invalid description
     * @throws UnknownDependencyException If a required dependency could not
     *     be resolved
     */
    @Nullable
    public Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException;

    /**
     * Loads the plugins contained within the specified directory
     *
     * @param directory Directory to check for plugins
     * @return A list of all plugins loaded
     */
    @NotNull
    public Plugin[] loadPlugins(@NotNull File directory);

    /**
     * Disables all the loaded plugins
     */
    public void disablePlugins();

    /**
     * Disables and removes all plugins
     */
    public void clearPlugins();

    /**
     * Calls an event with the given details
     *
     * @param event Event details
     * @throws IllegalStateException Thrown when an asynchronous event is
     *     fired from synchronous code.
     *     <p>
     *     <i>Note: This is best-effort basis, and should not be used to test
     *     synchronized state. This is an indicator for flawed flow logic.</i>
     */
    public void callEvent(@NotNull Event event) throws IllegalStateException;

    /**
     * Registers all the events in the given listener class
     *
     * @param listener Listener to register
     * @param plugin Plugin to register
     */
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin);

    /**
     * Registers the specified executor to the given event class
     *
     * @param event Event type to register
     * @param listener Listener to register
     * @param priority Priority to register this event at
     * @param executor EventExecutor to register
     * @param plugin Plugin to register
     */
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin);

    /**
     * Registers the specified executor to the given event class
     *
     * @param event Event type to register
     * @param listener Listener to register
     * @param priority Priority to register this event at
     * @param executor EventExecutor to register
     * @param plugin Plugin to register
     * @param ignoreCancelled Whether to pass cancelled events or not
     */
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled);

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

    /**
     * Gets a {@link Permission} from its fully qualified name
     *
     * @param name Name of the permission
     * @return Permission, or null if none
     */
    @Nullable
    public Permission getPermission(@NotNull String name);

    /**
     * Adds a {@link Permission} to this plugin manager.
     * <p>
     * If a permission is already defined with the given name of the new
     * permission, an exception will be thrown.
     *
     * @param perm Permission to add
     * @throws IllegalArgumentException Thrown when a permission with the same
     *     name already exists
     */
    public void addPermission(@NotNull Permission perm);

    /**
     * Removes a {@link Permission} registration from this plugin manager.
     * <p>
     * If the specified permission does not exist in this plugin manager,
     * nothing will happen.
     * <p>
     * Removing a permission registration will <b>not</b> remove the
     * permission from any {@link Permissible}s that have it.
     *
     * @param perm Permission to remove
     */
    public void removePermission(@NotNull Permission perm);

    /**
     * Removes a {@link Permission} registration from this plugin manager.
     * <p>
     * If the specified permission does not exist in this plugin manager,
     * nothing will happen.
     * <p>
     * Removing a permission registration will <b>not</b> remove the
     * permission from any {@link Permissible}s that have it.
     *
     * @param name Permission to remove
     */
    public void removePermission(@NotNull String name);

    /**
     * Gets the default permissions for the given op status
     *
     * @param op Which set of default permissions to get
     * @return The default permissions
     */
    @NotNull
    public Set<Permission> getDefaultPermissions(boolean op);

    /**
     * Recalculates the defaults for the given {@link Permission}.
     * <p>
     * This will have no effect if the specified permission is not registered
     * here.
     *
     * @param perm Permission to recalculate
     */
    public void recalculatePermissionDefaults(@NotNull Permission perm);

    /**
     * Subscribes the given Permissible for information about the requested
     * Permission, by name.
     * <p>
     * If the specified Permission changes in any form, the Permissible will
     * be asked to recalculate.
     *
     * @param permission Permission to subscribe to
     * @param permissible Permissible subscribing
     */
    public void subscribeToPermission(@NotNull String permission, @NotNull Permissible permissible);

    /**
     * Unsubscribes the given Permissible for information about the requested
     * Permission, by name.
     *
     * @param permission Permission to unsubscribe from
     * @param permissible Permissible subscribing
     */
    public void unsubscribeFromPermission(@NotNull String permission, @NotNull Permissible permissible);

    /**
     * Gets a set containing all subscribed {@link Permissible}s to the given
     * permission, by name
     *
     * @param permission Permission to query for
     * @return Set containing all subscribed permissions
     */
    @NotNull
    public Set<Permissible> getPermissionSubscriptions(@NotNull String permission);

    /**
     * Subscribes to the given Default permissions by operator status
     * <p>
     * If the specified defaults change in any form, the Permissible will be
     * asked to recalculate.
     *
     * @param op Default list to subscribe to
     * @param permissible Permissible subscribing
     */
    public void subscribeToDefaultPerms(boolean op, @NotNull Permissible permissible);

    /**
     * Unsubscribes from the given Default permissions by operator status
     *
     * @param op Default list to unsubscribe from
     * @param permissible Permissible subscribing
     */
    public void unsubscribeFromDefaultPerms(boolean op, @NotNull Permissible permissible);

    /**
     * Gets a set containing all subscribed {@link Permissible}s to the given
     * default list, by op status
     *
     * @param op Default list to query for
     * @return Set containing all subscribed permissions
     */
    @NotNull
    public Set<Permissible> getDefaultPermSubscriptions(boolean op);

    /**
     * Gets a set of all registered permissions.
     * <p>
     * This set is a copy and will not be modified live.
     *
     * @return Set containing all current registered permissions
     */
    @NotNull
    public Set<Permission> getPermissions();

    /**
     * Returns whether or not timing code should be used for event calls
     *
     * @return True if event timings are to be used
     */
    public boolean useTimings();
}
