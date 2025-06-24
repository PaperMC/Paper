package org.bukkit.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles all plugin management from the Server
 */
@Deprecated(forRemoval = true) // Paper - This implementation may be replaced in a future version of Paper.
// Plugins may still reflect into this class to modify permission logic for the time being.
public final class SimplePluginManager implements PluginManager {
    private final Server server;
    private final Map<Pattern, PluginLoader> fileAssociations = new HashMap<Pattern, PluginLoader>();
    private final List<Plugin> plugins = new ArrayList<Plugin>();
    private final Map<String, Plugin> lookupNames = new HashMap<String, Plugin>();
    private MutableGraph<String> dependencyGraph = GraphBuilder.directed().build();
    private File updateDirectory;
    private final SimpleCommandMap commandMap;
    // Paper start
    public final Map<String, Permission> permissions = new HashMap<String, Permission>();
    public final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<Boolean, Set<Permission>>();
    public final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<String, Map<Permissible, Boolean>>();
    public final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<Boolean, Map<Permissible, Boolean>>();
    public PluginManager paperPluginManager;
    // Paper end
    private boolean useTimings = false;

    public SimplePluginManager(@NotNull Server instance, @NotNull SimpleCommandMap commandMap) {
        server = instance;
        this.commandMap = commandMap;

        defaultPerms.put(true, new LinkedHashSet<Permission>());
        defaultPerms.put(false, new LinkedHashSet<Permission>());
    }

    /**
     * Registers the specified plugin loader
     *
     * @param loader Class name of the PluginLoader to register
     * @throws IllegalArgumentException Thrown when the given Class is not a
     *     valid PluginLoader
     */
    @Override
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException {
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
    @Override
    @NotNull
    public Plugin[] loadPlugins(@NotNull File directory) {
        // Paper start - extra jars
        return this.loadPlugins(directory, java.util.Collections.emptyList());
    }
    @NotNull
    public Plugin[] loadPlugins(final @NotNull File directory, final @NotNull List<File> extraPluginJars) {
        // Paper end
        if (true) {
            List<Plugin> pluginList = new ArrayList<>();
            java.util.Collections.addAll(pluginList, this.paperPluginManager.loadPlugins(directory));
            for (File file : extraPluginJars) {
                try {
                    pluginList.add(this.paperPluginManager.loadPlugin(file));
                } catch (Exception e) {
                    this.server.getLogger().log(Level.SEVERE, "Plugin loading error!", e);
                }
            }
            return pluginList.toArray(new Plugin[0]);
        }
        Preconditions.checkArgument(directory != null, "Directory cannot be null");
        Preconditions.checkArgument(directory.isDirectory(), "Directory must be a directory");

        if (!(server.getUpdateFolder().equals(""))) {
            updateDirectory = new File(directory, server.getUpdateFolder());
        }

        return loadPlugins(directory.listFiles());
    }

    /**
     * Loads the plugins in the list of the files
     *
     * @param files List of files containing plugins to load
     * @return A list of all plugins loaded
     */
    @NotNull
    public Plugin[] loadPlugins(@NotNull File[] files) {
        // TODO Replace with Paper plugin loader
        Preconditions.checkArgument(files != null, "File list cannot be null");

        List<Plugin> result = new ArrayList<Plugin>();
        Set<Pattern> filters = fileAssociations.keySet();

        Map<String, File> plugins = new HashMap<String, File>();
        Set<String> loadedPlugins = new HashSet<String>();
        Map<String, String> pluginsProvided = new HashMap<>();
        Map<String, Collection<String>> dependencies = new HashMap<String, Collection<String>>();
        Map<String, Collection<String>> softDependencies = new HashMap<String, Collection<String>>();

        // This is where it figures out all possible plugins
        for (File file : files) {
            PluginLoader loader = null;
            for (Pattern filter : filters) {
                Matcher match = filter.matcher(file.getName());
                if (match.find()) {
                    loader = fileAssociations.get(filter);
                }
            }

            if (loader == null) continue;

            PluginDescriptionFile description = null;
            try {
                description = loader.getPluginDescription(file);
                String name = description.getName();
                if (name.equalsIgnoreCase("bukkit") || name.equalsIgnoreCase("minecraft") || name.equalsIgnoreCase("mojang")) {
                    server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "': Restricted Name");
                    continue;
                } else if (description.rawName.indexOf(' ') != -1) {
                    server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "': uses the space-character (0x20) in its name");
                    continue;
                }
            } catch (InvalidDescriptionException ex) {
                server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "'", ex);
                continue;
            }

            File replacedFile = plugins.put(description.getName(), file);
            if (replacedFile != null) {
                server.getLogger().severe(String.format(
                        "Ambiguous plugin name `%s' for files `%s' and `%s'",
                        description.getName(),
                        file.getPath(),
                        replacedFile.getPath()
                ));
            }

            String removedProvided = pluginsProvided.remove(description.getName());
            if (removedProvided != null) {
                server.getLogger().warning(String.format(
                        "Ambiguous plugin name `%s'. It is also provided by `%s'",
                        description.getName(),
                        removedProvided
                ));
            }

            for (String provided : description.getProvides()) {
                File pluginFile = plugins.get(provided);
                if (pluginFile != null) {
                    server.getLogger().warning(String.format(
                            "`%s provides `%s' while this is also the name of `%s'",
                            file.getPath(),
                            provided,
                            pluginFile.getPath()
                    ));
                } else {
                    String replacedPlugin = pluginsProvided.put(provided, description.getName());
                    if (replacedPlugin != null) {
                        server.getLogger().warning(String.format(
                                "`%s' is provided by both `%s' and `%s'",
                                provided,
                                description.getName(),
                                replacedPlugin
                        ));
                    }
                }
            }

            Collection<String> softDependencySet = description.getSoftDepend();
            if (softDependencySet != null && !softDependencySet.isEmpty()) {
                if (softDependencies.containsKey(description.getName())) {
                    // Duplicates do not matter, they will be removed together if applicable
                    softDependencies.get(description.getName()).addAll(softDependencySet);
                } else {
                    softDependencies.put(description.getName(), new LinkedList<String>(softDependencySet));
                }

                for (String depend : softDependencySet) {
                    dependencyGraph.putEdge(description.getName(), depend);
                }
            }

            Collection<String> dependencySet = description.getDepend();
            if (dependencySet != null && !dependencySet.isEmpty()) {
                dependencies.put(description.getName(), new LinkedList<String>(dependencySet));

                for (String depend : dependencySet) {
                    dependencyGraph.putEdge(description.getName(), depend);
                }
            }

            Collection<String> loadBeforeSet = description.getLoadBefore();
            if (loadBeforeSet != null && !loadBeforeSet.isEmpty()) {
                for (String loadBeforeTarget : loadBeforeSet) {
                    if (softDependencies.containsKey(loadBeforeTarget)) {
                        softDependencies.get(loadBeforeTarget).add(description.getName());
                    } else {
                        // softDependencies is never iterated, so 'ghost' plugins aren't an issue
                        Collection<String> shortSoftDependency = new LinkedList<String>();
                        shortSoftDependency.add(description.getName());
                        softDependencies.put(loadBeforeTarget, shortSoftDependency);
                    }

                    dependencyGraph.putEdge(loadBeforeTarget, description.getName());
                }
            }
        }

        while (!plugins.isEmpty()) {
            boolean missingDependency = true;
            Iterator<Map.Entry<String, File>> pluginIterator = plugins.entrySet().iterator();

            while (pluginIterator.hasNext()) {
                Map.Entry<String, File> entry = pluginIterator.next();
                String plugin = entry.getKey();

                if (dependencies.containsKey(plugin)) {
                    Iterator<String> dependencyIterator = dependencies.get(plugin).iterator();

                    while (dependencyIterator.hasNext()) {
                        String dependency = dependencyIterator.next();

                        // Dependency loaded
                        if (loadedPlugins.contains(dependency)) {
                            dependencyIterator.remove();

                        // We have a dependency not found
                        } else if (!plugins.containsKey(dependency) && !pluginsProvided.containsKey(dependency)) {
                            missingDependency = false;
                            pluginIterator.remove();
                            softDependencies.remove(plugin);
                            dependencies.remove(plugin);

                            server.getLogger().log(
                                    Level.SEVERE,
                                    "Could not load '" + entry.getValue().getPath() + "'",
                                    new UnknownDependencyException("Unknown dependency " + dependency + ". Please download and install " + dependency + " to run this plugin."));
                            break;
                        }
                    }

                    if (dependencies.containsKey(plugin) && dependencies.get(plugin).isEmpty()) {
                        dependencies.remove(plugin);
                    }
                }
                if (softDependencies.containsKey(plugin)) {
                    Iterator<String> softDependencyIterator = softDependencies.get(plugin).iterator();

                    while (softDependencyIterator.hasNext()) {
                        String softDependency = softDependencyIterator.next();

                        // Soft depend is no longer around
                        if (!plugins.containsKey(softDependency) && !pluginsProvided.containsKey(softDependency)) {
                            softDependencyIterator.remove();
                        }
                    }

                    if (softDependencies.get(plugin).isEmpty()) {
                        softDependencies.remove(plugin);
                    }
                }
                if (!(dependencies.containsKey(plugin) || softDependencies.containsKey(plugin)) && plugins.containsKey(plugin)) {
                    // We're clear to load, no more soft or hard dependencies left
                    File file = plugins.get(plugin);
                    pluginIterator.remove();
                    missingDependency = false;

                    try {
                        Plugin loadedPlugin = loadPlugin(file);
                        if (loadedPlugin != null) {
                            result.add(loadedPlugin);
                            loadedPlugins.add(loadedPlugin.getName());
                            loadedPlugins.addAll(loadedPlugin.getDescription().getProvides());
                        } else {
                            server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "'");
                        }
                        continue;
                    } catch (InvalidPluginException ex) {
                        server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "'", ex);
                    }
                }
            }

            if (missingDependency) {
                // We now iterate over plugins until something loads
                // This loop will ignore soft dependencies
                pluginIterator = plugins.entrySet().iterator();

                while (pluginIterator.hasNext()) {
                    Map.Entry<String, File> entry = pluginIterator.next();
                    String plugin = entry.getKey();

                    if (!dependencies.containsKey(plugin)) {
                        softDependencies.remove(plugin);
                        missingDependency = false;
                        File file = entry.getValue();
                        pluginIterator.remove();

                        try {
                            Plugin loadedPlugin = loadPlugin(file);
                            if (loadedPlugin != null) {
                                result.add(loadedPlugin);
                                loadedPlugins.add(loadedPlugin.getName());
                                loadedPlugins.addAll(loadedPlugin.getDescription().getProvides());
                            } else {
                                server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "'");
                            }
                            break;
                        } catch (InvalidPluginException ex) {
                            server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "'", ex);
                        }
                    }
                }
                // We have no plugins left without a depend
                if (missingDependency) {
                    softDependencies.clear();
                    dependencies.clear();
                    Iterator<File> failedPluginIterator = plugins.values().iterator();

                    while (failedPluginIterator.hasNext()) {
                        File file = failedPluginIterator.next();
                        failedPluginIterator.remove();
                        server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "': circular dependency detected");
                    }
                }
            }
        }

        return result.toArray(new Plugin[result.size()]);
    }

    /**
     * Loads the plugin in the specified file
     * <p>
     * File must be valid according to the current enabled Plugin interfaces
     *
     * @param file File containing the plugin to load
     * @return The Plugin loaded, or null if it was invalid
     * @throws InvalidPluginException Thrown when the specified file is not a
     *     valid plugin
     * @throws UnknownDependencyException If a required dependency could not
     *     be found
     */
    @Override
    @Nullable
    public synchronized Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, UnknownDependencyException {
        Preconditions.checkArgument(file != null, "File cannot be null");
        // Paper start
        if (true) {
            try {
                return this.paperPluginManager.loadPlugin(file);
            } catch (org.bukkit.plugin.InvalidDescriptionException ignored) {
                return null;
            }
        }
        // Paper end

        checkUpdate(file);

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
            lookupNames.put(result.getDescription().getName().toLowerCase(java.util.Locale.ENGLISH), result); // Paper
            for (String provided : result.getDescription().getProvides()) {
                lookupNames.putIfAbsent(provided.toLowerCase(java.util.Locale.ENGLISH), result); // Paper
            }
        }

        return result;
    }

    private void checkUpdate(@NotNull File file) {
        if (updateDirectory == null || !updateDirectory.isDirectory()) {
            return;
        }

        File updateFile = new File(updateDirectory, file.getName());
        if (updateFile.isFile() && FileUtil.copy(updateFile, file)) {
            updateFile.delete();
        }
    }

    /**
     * Checks if the given plugin is loaded and returns it when applicable
     * <p>
     * Please note that the name of the plugin is case-insensitive
     *
     * @param name Name of the plugin to check
     * @return Plugin if it exists, otherwise null
     */
    @Override
    @Nullable
    public synchronized Plugin getPlugin(@NotNull String name) {
        if (true) {return this.paperPluginManager.getPlugin(name);} // Paper
        return lookupNames.get(name.replace(' ', '_').toLowerCase(java.util.Locale.ENGLISH)); // Paper
    }

    @Override
    @NotNull
    public synchronized Plugin[] getPlugins() {
        if (true) {return this.paperPluginManager.getPlugins();} // Paper
        return plugins.toArray(new Plugin[plugins.size()]);
    }

    /**
     * Checks if the given plugin is enabled or not
     * <p>
     * Please note that the name of the plugin is case-insensitive.
     *
     * @param name Name of the plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        if (true) {return this.paperPluginManager.isPluginEnabled(name);} // Paper
        Plugin plugin = getPlugin(name);

        return isPluginEnabled(plugin);
    }

    /**
     * Checks if the given plugin is enabled or not
     *
     * @param plugin Plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    @Override
    public boolean isPluginEnabled(@Nullable Plugin plugin) {
        if (true) {return this.paperPluginManager.isPluginEnabled(plugin);} // Paper
        if ((plugin != null) && (plugins.contains(plugin))) {
            return plugin.isEnabled();
        } else {
            return false;
        }
    }

    @Override
    public void enablePlugin(@NotNull final Plugin plugin) {
        if (true) {this.paperPluginManager.enablePlugin(plugin); return;} // Paper
        if (!plugin.isEnabled()) {
            List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);

            if (!pluginCommands.isEmpty()) {
                commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
            }

            try {
                plugin.getPluginLoader().enablePlugin(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while enabling "
                        + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin);
            }

            HandlerList.bakeAll();
        }
    }

    @Override
    public void disablePlugins() {
        if (true) {this.paperPluginManager.disablePlugins(); return;} // Paper
        Plugin[] plugins = getPlugins();
        for (int i = plugins.length - 1; i >= 0; i--) {
            disablePlugin(plugins[i]);
        }
    }

    @Override
    public void disablePlugin(@NotNull final Plugin plugin) {
        if (true) {this.paperPluginManager.disablePlugin(plugin); return;} // Paper
        if (plugin.isEnabled()) {
            try {
                plugin.getPluginLoader().disablePlugin(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while disabling "
                        + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                server.getScheduler().cancelTasks(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while cancelling tasks for "
                        + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                server.getServicesManager().unregisterAll(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while unregistering services for "
                        + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                HandlerList.unregisterAll(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while unregistering events for "
                        + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                server.getMessenger().unregisterIncomingPluginChannel(plugin);
                server.getMessenger().unregisterOutgoingPluginChannel(plugin);
            } catch (Throwable ex) {
                handlePluginException("Error occurred (in the plugin loader) while unregistering plugin channels for "
                        + plugin.getDescription().getFullName() + " (Is it up to date?)", ex, plugin); // Paper
            }

            try {
                for (World world : server.getWorlds()) {
                    world.removePluginChunkTickets(plugin);
                }
            } catch (Throwable ex) {
                server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while removing chunk tickets for " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
            }
        }
    }

    // Paper start
    private void handlePluginException(String msg, Throwable ex, Plugin plugin) {
        server.getLogger().log(Level.SEVERE, msg, ex);
        callEvent(new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerPluginEnableDisableException(msg, ex, plugin)));
    }
    // Paper end

    @Override
    public void clearPlugins() {
        if (true) {this.paperPluginManager.clearPlugins(); return;} // Paper
        synchronized (this) {
            disablePlugins();
            plugins.clear();
            lookupNames.clear();
            dependencyGraph = GraphBuilder.directed().build();
            HandlerList.unregisterAll();
            fileAssociations.clear();
            permissions.clear();
            defaultPerms.get(true).clear();
            defaultPerms.get(false).clear();
        }
    }

    /**
     * Calls an event with the given details.
     *
     * @param event Event details
     */
    @Override
    public void callEvent(@NotNull Event event) {
        if (true) {this.paperPluginManager.callEvent(event); return;} // Paper
        if (event.isAsynchronous()) {
            if (Thread.holdsLock(this)) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from inside synchronized code.");
            }
            if (server.isPrimaryThread()) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from primary server thread.");
            }
        } else {
            if (!server.isPrimaryThread()) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from another thread.");
            }
        }

        fireEvent(event);
    }

    private void fireEvent(@NotNull Event event) {
        HandlerList handlers = event.getHandlers();
        RegisteredListener[] listeners = handlers.getRegisteredListeners();

        for (RegisteredListener registration : listeners) {
            if (!registration.getPlugin().isEnabled()) {
                continue;
            }

            try {
                registration.callEvent(event);
            } catch (AuthorNagException ex) {
                Plugin plugin = registration.getPlugin();

                if (plugin.isNaggable()) {
                    plugin.setNaggable(false);

                    server.getLogger().log(Level.SEVERE, String.format(
                            "Nag author(s): '%s' of '%s' about the following: %s",
                            plugin.getDescription().getAuthors(),
                            plugin.getDescription().getFullName(),
                            ex.getMessage()
                            ));
                }
            } catch (Throwable ex) {
                // Paper start - error reporting
                String msg = "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getFullName();
                server.getLogger().log(Level.SEVERE, msg, ex);
                if (!(event instanceof com.destroystokyo.paper.event.server.ServerExceptionEvent)) { // We don't want to cause an endless event loop
                    callEvent(new com.destroystokyo.paper.event.server.ServerExceptionEvent(new com.destroystokyo.paper.exception.ServerEventException(msg, ex, registration.getPlugin(), registration.getListener(), event)));
                }
                // Paper end
            }
        }
    }

    @Override
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {
        if (true) {this.paperPluginManager.registerEvents(listener, plugin); return;} // Paper
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
        }

        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
        }

    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin) {
        registerEvent(event, listener, priority, executor, plugin, false);
    }

    /**
     * Registers the given event to the specified listener using a directly
     * passed EventExecutor
     *
     * @param event Event class to register
     * @param listener PlayerListener to register
     * @param priority Priority of this event
     * @param executor EventExecutor to register
     * @param plugin Plugin to register
     * @param ignoreCancelled Do not call executor if event was already
     *     cancelled
     */
    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled) {
        Preconditions.checkArgument(listener != null, "Listener cannot be null");
        Preconditions.checkArgument(priority != null, "Priority cannot be null");
        Preconditions.checkArgument(executor != null, "Executor cannot be null");
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        if (true) {this.paperPluginManager.registerEvent(event, listener, priority, executor, plugin, ignoreCancelled); return;} // Paper

        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");
        }

        executor = new co.aikar.timings.TimedEventExecutor(executor, plugin, null, event); // Paper
        if (false) { // Spigot - RL handles useTimings check now // Paper
            getEventListeners(event).register(new TimedRegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
        } else {
            getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
        }
    }

    @NotNull
    private HandlerList getEventListeners(@NotNull Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);

            if (!Modifier.isStatic(method.getModifiers())) {
                throw new IllegalAccessException("getHandlerList must be static");
            }

            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException("Error while registering listener for event type " + type.toString() + ": " + e.toString());
        }
    }

    @NotNull
    private Class<? extends Event> getRegistrationClass(@NotNull Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }

    @Override
    @Nullable
    public Permission getPermission(@NotNull String name) {
        if (true) {return this.paperPluginManager.getPermission(name);} // Paper
        return permissions.get(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public void addPermission(@NotNull Permission perm) {
        if (true) {this.paperPluginManager.addPermission(perm); return;} // Paper
        addPermission(perm, true);
    }

    @Deprecated(since = "1.12")
    public void addPermission(@NotNull Permission perm, boolean dirty) {
        if (true) {this.paperPluginManager.addPermission(perm); return;} // Paper - This just has a performance implication, use the better api to avoid this.
        String name = perm.getName().toLowerCase(Locale.ROOT);

        if (permissions.containsKey(name)) {
            throw new IllegalArgumentException("The permission " + name + " is already defined!");
        }

        permissions.put(name, perm);
        calculatePermissionDefault(perm, dirty);
    }

    @Override
    @NotNull
    public Set<Permission> getDefaultPermissions(boolean op) {
        if (true) {return this.paperPluginManager.getDefaultPermissions(op);} // Paper
        return ImmutableSet.copyOf(defaultPerms.get(op));
    }

    @Override
    public void removePermission(@NotNull Permission perm) {
        if (true) {this.paperPluginManager.removePermission(perm); return;} // Paper
        removePermission(perm.getName());
    }

    @Override
    public void removePermission(@NotNull String name) {
        if (true) {this.paperPluginManager.removePermission(name); return;} // Paper
        permissions.remove(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public void recalculatePermissionDefaults(@NotNull Permission perm) {
        if (true) {this.paperPluginManager.recalculatePermissionDefaults(perm); return;} // Paper
        if (perm != null && permissions.containsKey(perm.getName().toLowerCase(Locale.ROOT))) {
            defaultPerms.get(true).remove(perm);
            defaultPerms.get(false).remove(perm);

            calculatePermissionDefault(perm, true);
        }
    }

    private void calculatePermissionDefault(@NotNull Permission perm, boolean dirty) {
        if ((perm.getDefault() == PermissionDefault.OP) || (perm.getDefault() == PermissionDefault.TRUE)) {
            defaultPerms.get(true).add(perm);
            if (dirty) {
                dirtyPermissibles(true);
            }
        }
        if ((perm.getDefault() == PermissionDefault.NOT_OP) || (perm.getDefault() == PermissionDefault.TRUE)) {
            defaultPerms.get(false).add(perm);
            if (dirty) {
                dirtyPermissibles(false);
            }
        }
    }

    @Deprecated(since = "1.12")
    public void dirtyPermissibles() {
        dirtyPermissibles(true);
        dirtyPermissibles(false);
    }

    private void dirtyPermissibles(boolean op) {
        Set<Permissible> permissibles = getDefaultPermSubscriptions(op);

        for (Permissible p : permissibles) {
            p.recalculatePermissions();
        }
    }

    @Override
    public void subscribeToPermission(@NotNull String permission, @NotNull Permissible permissible) {
        if (true) {this.paperPluginManager.subscribeToPermission(permission, permissible); return;} // Paper
        String name = permission.toLowerCase(Locale.ROOT);
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            permSubs.put(name, map);
        }

        map.put(permissible, true);
    }

    @Override
    public void unsubscribeFromPermission(@NotNull String permission, @NotNull Permissible permissible) {
        if (true) {this.paperPluginManager.unsubscribeFromPermission(permission, permissible); return;} // Paper
        String name = permission.toLowerCase(Locale.ROOT);
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map != null) {
            map.remove(permissible);

            if (map.isEmpty()) {
                permSubs.remove(name);
            }
        }
    }

    @Override
    @NotNull
    public Set<Permissible> getPermissionSubscriptions(@NotNull String permission) {
        if (true) {return this.paperPluginManager.getPermissionSubscriptions(permission);} // Paper
        String name = permission.toLowerCase(Locale.ROOT);
        Map<Permissible, Boolean> map = permSubs.get(name);

        if (map == null) {
            return ImmutableSet.of();
        } else {
            return ImmutableSet.copyOf(map.keySet());
        }
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, @NotNull Permissible permissible) {
        if (true) {this.paperPluginManager.subscribeToDefaultPerms(op, permissible); return;} // Paper
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map == null) {
            map = new WeakHashMap<Permissible, Boolean>();
            defSubs.put(op, map);
        }

        map.put(permissible, true);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, @NotNull Permissible permissible) {
        if (true) {this.paperPluginManager.unsubscribeFromDefaultPerms(op, permissible); return;} // Paper
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map != null) {
            map.remove(permissible);

            if (map.isEmpty()) {
                defSubs.remove(op);
            }
        }
    }

    @Override
    @NotNull
    public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        if (true) {return this.paperPluginManager.getDefaultPermSubscriptions(op);} // Paper
        Map<Permissible, Boolean> map = defSubs.get(op);

        if (map == null) {
            return ImmutableSet.of();
        } else {
            return ImmutableSet.copyOf(map.keySet());
        }
    }

    @Override
    @NotNull
    public Set<Permission> getPermissions() {
        if (true) {return this.paperPluginManager.getPermissions();} // Paper
        return new HashSet<Permission>(permissions.values());
    }

    public boolean isTransitiveDepend(@NotNull PluginDescriptionFile plugin, @NotNull PluginDescriptionFile depend) {
        Preconditions.checkArgument(plugin != null, "plugin");
        Preconditions.checkArgument(depend != null, "depend");

        if (dependencyGraph.nodes().contains(plugin.getName())) {
            Set<String> reachableNodes = Graphs.reachableNodes(dependencyGraph, plugin.getName());
            if (reachableNodes.contains(depend.getName())) {
                return true;
            }
            for (String provided : depend.getProvides()) {
                if (reachableNodes.contains(provided)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean useTimings() {
        if (true) {return this.paperPluginManager.useTimings();} // Paper
        return co.aikar.timings.Timings.isTimingsEnabled(); // Spigot
    }

    /**
     * Sets whether or not per event timing code should be used
     *
     * @param use True if per event timing code should be used
     */
    @Deprecated(forRemoval = true)
    public void useTimings(boolean use) {
        co.aikar.timings.Timings.setTimingsEnabled(use); // Paper
    }

    // Paper start
    public void clearPermissions() {
        if (true) {this.paperPluginManager.clearPermissions(); return;} // Paper
        permissions.clear();
        defaultPerms.get(true).clear();
        defaultPerms.get(false).clear();
    }

    @Override
    public boolean isTransitiveDependency(io.papermc.paper.plugin.configuration.PluginMeta pluginMeta, io.papermc.paper.plugin.configuration.PluginMeta dependencyConfig) {
        return this.paperPluginManager.isTransitiveDependency(pluginMeta, dependencyConfig);
    }

    @Override
    public void overridePermissionManager(@NotNull Plugin plugin, @Nullable io.papermc.paper.plugin.PermissionManager permissionManager) {
        this.paperPluginManager.overridePermissionManager(plugin, permissionManager);
    }

    @Override
    public void addPermissions(@NotNull List<Permission> perm) {
        this.paperPluginManager.addPermissions(perm);
    }
    // Paper end
}
