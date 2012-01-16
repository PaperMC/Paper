package org.bukkit.plugin;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.*;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.FileUtil;

/**
 * Handles all plugin management from the Server
 */
public final class SimplePluginManager implements PluginManager {
    private final Server server;
    private final Map<Pattern, PluginLoader> fileAssociations = new HashMap<Pattern, PluginLoader>();
    private final List<Plugin> plugins = new ArrayList<Plugin>();
    private final Map<String, Plugin> lookupNames = new HashMap<String, Plugin>();
    private static File updateDirectory = null;
    private final SimpleCommandMap commandMap;
    private final Map<String, Permission> permissions = new HashMap<String, Permission>();
    private final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<Boolean, Set<Permission>>();
    private final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<String, Map<Permissible, Boolean>>();
    private final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<Boolean, Map<Permissible, Boolean>>();

    public SimplePluginManager(Server instance, SimpleCommandMap commandMap) {
        server = instance;
        this.commandMap = commandMap;

        defaultPerms.put(true, new HashSet<Permission>());
        defaultPerms.put(false, new HashSet<Permission>());
    }

    /**
     * Registers the specified plugin loader
     *
     * @param loader Class name of the PluginLoader to register
     * @throws IllegalArgumentException Thrown when the given Class is not a valid PluginLoader
     */
    public void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        PluginLoader instance;

        if (PluginLoader.class.isAssignableFrom(loader)) {
            Constructor<? extends PluginLoader> constructor;

            try {
                constructor = loader.getConstructor(Server.class);
                instance = constructor.newInstance(server);
            } catch (NoSuchMethodException ex) {
                String className = loader.getName();

                throw new IllegalArgumentException(String.format("Class %s does not have a public %s(Server) constructor", className, className), ex);
            } catch (Exception ex) {
                throw new IllegalArgumentException(String.format("Unexpected exception %s while attempting to construct a new instance of %s", ex.getClass().getName(), loader.getName()), ex);
            }
        } else {
            throw new IllegalArgumentException(String.format("Class %s does not implement interface PluginLoader", loader.getName()));
        }

        Pattern[] patterns = instance.getPluginFileFilters();

        synchronized (this) {
            for (Pattern pattern : patterns) {
                fileAssociations.put(pattern, instance);
            }
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

        boolean allFailed = false;
        boolean finalPass = false;

        LinkedList<File> filesList = new LinkedList<File>(Arrays.asList(files));

        if (!(server.getUpdateFolder().equals(""))) {
            updateDirectory = new File(directory, server.getUpdateFolder());
        }

        while (!allFailed || finalPass) {
            allFailed = true;
            Iterator<File> itr = filesList.iterator();

            while (itr.hasNext()) {
                File file = itr.next();
                Plugin plugin = null;

                try {
                    plugin = loadPlugin(file, finalPass);
                } catch (UnknownDependencyException ex) {
                    if (finalPass) {
                        server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': " + ex.getMessage(), ex);
                        itr.remove();
                    } else {
                        plugin = null;
                    }
                } catch (InvalidPluginException ex) {
                    server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': ", ex.getCause());
                    itr.remove();
                } catch (InvalidDescriptionException ex) {
                    server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': " + ex.getMessage(), ex);
                    itr.remove();
                }

                if (plugin != null) {
                    result.add(plugin);
                    allFailed = false;
                    finalPass = false;
                    itr.remove();
                }
            }
            if (finalPass) {
                break;
            } else if (allFailed) {
                finalPass = true;
            }
        }

        return result.toArray(new Plugin[result.size()]);
    }

    /**
     * Loads the plugin in the specified file
     * <p />
     * File must be valid according to the current enabled Plugin interfaces
     *
     * @param file File containing the plugin to load
     * @return The Plugin loaded, or null if it was invalid
     * @throws InvalidPluginException Thrown when the specified file is not a valid plugin
     * @throws InvalidDescriptionException Thrown when the specified file contains an invalid description
     * @throws UnknownDependencyException If a required dependency could not be found
     */
    public synchronized Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        return loadPlugin(file, true);
    }

    /**
     * Loads the plugin in the specified file
     * <p />
     * File must be valid according to the current enabled Plugin interfaces
     *
     * @param file File containing the plugin to load
     * @param ignoreSoftDependencies Loader will ignore soft dependencies if this flag is set to true
     * @return The Plugin loaded, or null if it was invalid
     * @throws InvalidPluginException Thrown when the specified file is not a valid plugin
     * @throws InvalidDescriptionException Thrown when the specified file contains an invalid description
     * @throws UnknownDependencyException If a required dependency could not be found
     */
    public synchronized Plugin loadPlugin(File file, boolean ignoreSoftDependencies) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        File updateFile = null;

        if (updateDirectory != null && updateDirectory.isDirectory() && (updateFile = new File(updateDirectory, file.getName())).isFile()) {
            if (FileUtil.copy(updateFile, file)) {
                updateFile.delete();
            }
        }

        Set<Pattern> filters = fileAssociations.keySet();
        Plugin result = null;

        for (Pattern filter : filters) {
            String name = file.getName();
            Matcher match = filter.matcher(name);

            if (match.find()) {
                PluginLoader loader = fileAssociations.get(filter);

                result = loader.loadPlugin(file, ignoreSoftDependencies);
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
     * <p />
     * Please note that the name of the plugin is case-sensitive
     *
     * @param name Name of the plugin to check
     * @return Plugin if it exists, otherwise null
     */
    public synchronized Plugin getPlugin(String name) {
        return lookupNames.get(name);
    }

    public synchronized Plugin[] getPlugins() {
        return plugins.toArray(new Plugin[0]);
    }

    /**
     * Checks if the given plugin is enabled or not
     * <p />
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

    public void enablePlugin(final Plugin plugin) {
        if (!plugin.isEnabled()) {
            List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);

            if (!pluginCommands.isEmpty()) {
                commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
            }

            try {
                plugin.getPluginLoader().enablePlugin(plugin);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
            }

            HandlerList.bakeAll();
        }
    }

    public void disablePlugins() {
        for (Plugin plugin : getPlugins()) {
            disablePlugin(plugin);
        }
    }

    public void disablePlugin(final Plugin plugin) {
        if (plugin.isEnabled()) {
            try {
                plugin.getPluginLoader().disablePlugin(plugin);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
            }

            try {
                server.getScheduler().cancelTasks(plugin);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while cancelling tasks for " + plugin.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
            }

            try {
                server.getServicesManager().unregisterAll(plugin);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering services for " + plugin.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
            }

            try {
                HandlerList.unregisterAll(plugin);
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while unregistering events for " + plugin.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
            }
        }
    }

    public void clearPlugins() {
        synchronized (this) {
            disablePlugins();
            plugins.clear();
            lookupNames.clear();
            HandlerList.unregisterAll();
            fileAssociations.clear();
            permissions.clear();
            defaultPerms.get(true).clear();
            defaultPerms.get(false).clear();
        }
    }

    /**
     * Calls an event with the given details
     *
     * @param event Event details
     */
    public synchronized void callEvent(Event event) {
        HandlerList handlers = event.getHandlers();
        handlers.bake();
        RegisteredListener[][] listeners = handlers.getRegisteredListeners();

        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                for (RegisteredListener registration : listeners[i]) {
                    if (!registration.getPlugin().isEnabled()) {
                        continue;
                    }

                    try {
                        long start = System.nanoTime();
                        registration.callEvent(event);
                        registration.getPlugin().incTiming(event.getType(), System.nanoTime() - start);
                    } catch (AuthorNagException ex) {
                        Plugin plugin = registration.getPlugin();

                        if (plugin.isNaggable()) {
                            plugin.setNaggable(false);

                            String author = "<NoAuthorGiven>";

                            if (plugin.getDescription().getAuthors().size() > 0) {
                                author = plugin.getDescription().getAuthors().get(0);
                            }
                            server.getLogger().log(Level.SEVERE, String.format(
                                    "Nag author: '%s' of '%s' about the following: %s",
                                    author,
                                    plugin.getDescription().getName(),
                                    ex.getMessage()
                            ));
                        }
                    } catch (Throwable ex) {
                        server.getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getName(), ex);
                    }
                }
            }
        }
        // This is an ugly hack to handle old-style custom events in old plugins without breakage. All in the name of plugin compatibility.
        if (event.getType() == Event.Type.CUSTOM_EVENT) {
            TransitionalCustomEvent.getHandlerList().bake();
            listeners = TransitionalCustomEvent.getHandlerList().getRegisteredListeners();
            if (listeners != null) {
                for (int i = 0; i < listeners.length; i++) {
                    for (RegisteredListener registration : listeners[i]) {
                        try {
                            registration.callEvent(event);
                        } catch (Throwable ex) {
                            server.getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getName(), ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * Registers the given event to the specified listener
     *
     * @param type EventType to register
     * @param listener PlayerListener to register
     * @param priority Priority of this event
     * @param plugin Plugin to register
     */
    public void registerEvent(Event.Type type, Listener listener, Priority priority, Plugin plugin) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + type + " while not enabled");
        }

        getEventListeners(type.getEventClass()).register(new RegisteredListener(listener, plugin.getPluginLoader().createExecutor(type, listener), priority.getNewPriority(), plugin));
    }

    /**
     * Registers the given event to the specified listener using a directly passed EventExecutor
     *
     * @param type EventType to register
     * @param listener PlayerListener to register
     * @param executor EventExecutor to register
     * @param priority Priority of this event
     * @param plugin Plugin to register
     */
    public void registerEvent(Event.Type type, Listener listener, EventExecutor executor, Priority priority, Plugin plugin) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + type + " while not enabled");
        }

        getEventListeners(type.getEventClass()).register(new RegisteredListener(listener, executor, priority.getNewPriority(), plugin));
    }

    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled()) {
                    throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
        }
        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
            Class<? extends Event> delegatedClass = getRegistrationClass(entry.getKey());
            if (!entry.getKey().equals(delegatedClass)) {
                plugin.getServer().getLogger().severe("Plugin attempted to register delegated event class " + entry.getKey() + ". It should be using " + delegatedClass + "!");
                continue;
            }
            getEventListeners(delegatedClass).registerAll(entry.getValue());
        }

    }

    public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");
        }

        getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin));
    }

    /**
     * Returns the specified event type's HandlerList
     *
     * @param type EventType to lookup
     * @return HandlerList The list of registered handlers for the event.
     */
    private HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList)method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && clazz.getSuperclass().isAssignableFrom(Event.class)) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }

    public Permission getPermission(String name) {
        return permissions.get(name.toLowerCase());
    }

    public void addPermission(Permission perm) {
        String name = perm.getName().toLowerCase();

        if (permissions.containsKey(name)) {
            throw new IllegalArgumentException("The permission " + name + " is already defined!");
        }

        permissions.put(name, perm);
        calculatePermissionDefault(perm);
    }

    public Set<Permission> getDefaultPermissions(boolean op) {
        return ImmutableSet.copyOf(defaultPerms.get(op));
    }

    public void removePermission(Permission perm) {
        removePermission(perm.getName().toLowerCase());
    }

    public void removePermission(String name) {
        permissions.remove(name);
    }

    public void recalculatePermissionDefaults(Permission perm) {
        if (permissions.containsValue(perm)) {
            defaultPerms.get(true).remove(perm);
            defaultPerms.get(false).remove(perm);

            calculatePermissionDefault(perm);
        }
    }

    private void calculatePermissionDefault(Permission perm) {
        if ((perm.getDefault() == PermissionDefault.OP) || (perm.getDefault() == PermissionDefault.TRUE)) {
            defaultPerms.get(true).add(perm);
            dirtyPermissibles(true);
        }
        if ((perm.getDefault() == PermissionDefault.NOT_OP) || (perm.getDefault() == PermissionDefault.TRUE)) {
            defaultPerms.get(false).add(perm);
            dirtyPermissibles(false);
        }
    }

    private void dirtyPermissibles(boolean op) {
        Set<Permissible> permissibles = getDefaultPermSubscriptions(op);

        for (Permissible p : permissibles) {
            p.recalculatePermissions();
        }
    }

    public void subscribeToPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase();
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            permSubs.put(name, map);
        }

        map.put(permissible, true);
    }

    public void unsubscribeFromPermission(String permission, Permissible permissible) {
        String name = permission.toLowerCase();
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map != null) {
            map.remove(permissible);

            if (map.isEmpty()) {
                permSubs.remove(name);
            }
        }
    }

    public Set<Permissible> getPermissionSubscriptions(String permission) {
        String name = permission.toLowerCase();
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map == null) {
            return ImmutableSet.of();
        } else {
            return ImmutableSet.copyOf(map.keySet());
        }
    }

    public void subscribeToDefaultPerms(boolean op, Permissible permissible) {
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            defSubs.put(op, map);
        }

        map.put(permissible, true);
    }

    public void unsubscribeFromDefaultPerms(boolean op, Permissible permissible) {
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map != null) {
            map.remove(permissible);

            if (map.isEmpty()) {
                defSubs.remove(op);
            }
        }
    }

    public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map == null) {
            return ImmutableSet.of();
        } else {
            return ImmutableSet.copyOf(map.keySet());
        }
    }

    public Set<Permission> getPermissions() {
        return new HashSet<Permission>(permissions.values());
    }
}
