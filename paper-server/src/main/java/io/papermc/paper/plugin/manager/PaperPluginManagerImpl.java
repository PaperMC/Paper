package io.papermc.paper.plugin.manager;

import com.google.common.graph.MutableGraph;
import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.PermissionManager;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PaperPluginManagerImpl implements PluginManager, DependencyContext {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    final PaperPluginInstanceManager instanceManager;
    final PaperEventManager paperEventManager;
    PermissionManager permissionManager;

    public PaperPluginManagerImpl(Server server, CommandMap commandMap) {
        this.instanceManager = new PaperPluginInstanceManager(this, commandMap, server);
        this.paperEventManager = new PaperEventManager(server);
        PermissionManager possibleCustomImpl = PermissionRegistrar.INSTANCE.getRegistered();
        if (possibleCustomImpl != null) {
            this.permissionManager = possibleCustomImpl;
            LOGGER.info("Custom Permission Manager Implementation from plugin {}", PermissionRegistrar.INSTANCE.getRegisteredOwner());
        } else {
            this.permissionManager = new NormalPaperPermissionManager();
        }
    }

    // REMOVE THIS WHEN SimplePluginManager is removed.
    // Just cast and use Bukkit.getServer().getPluginManager()
    public static PaperPluginManagerImpl getInstance() {
        return ((CraftServer) (Bukkit.getServer())).paperPluginManager;
    }

    // Plugin Manipulation

    @Override
    public @Nullable Plugin getPlugin(@NotNull String name) {
        return this.instanceManager.getPlugin(name);
    }

    @Override
    public @NotNull Plugin[] getPlugins() {
        return this.instanceManager.getPlugins();
    }

    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        return this.instanceManager.isPluginEnabled(name);
    }

    @Override
    public boolean isPluginEnabled(@Nullable Plugin plugin) {
        return this.instanceManager.isPluginEnabled(plugin);
    }

    public void loadPlugin(Plugin plugin) {
        this.instanceManager.loadPlugin(plugin);
    }

    @Override
    public @Nullable Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        return this.instanceManager.loadPlugin(file.toPath());
    }

    @Override
    public @NotNull Plugin[] loadPlugins(@NotNull File directory) {
        return this.instanceManager.loadPlugins(directory.toPath());
    }

    @Override
    public @NotNull Plugin[] loadPlugins(final @NotNull File[] files) {
        return this.instanceManager.loadPlugins(files);
    }

    @Override
    public void disablePlugins() {
        this.instanceManager.disablePlugins();
    }

    @Override
    public synchronized void clearPlugins() {
        this.instanceManager.clearPlugins();
        this.permissionManager.clearPermissions();
        this.paperEventManager.clearEvents();
    }

    @Override
    public void enablePlugin(@NotNull Plugin plugin) {
        this.instanceManager.enablePlugin(plugin);
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        this.instanceManager.disablePlugin(plugin);
    }

    @Override
    public boolean isTransitiveDependency(PluginMeta pluginMeta, PluginMeta dependencyConfig) {
        return this.instanceManager.isTransitiveDepend(pluginMeta, dependencyConfig);
    }

    @Override
    public boolean hasDependency(String pluginIdentifier) {
        return this.instanceManager.hasDependency(pluginIdentifier);
    }

    // Event manipulation

    @Override
    public void callEvent(@NotNull Event event) throws IllegalStateException {
        this.paperEventManager.callEvent(event);
    }

    @Override
    public void registerEvents(@NotNull Listener listener, @NotNull Plugin plugin) {
        this.paperEventManager.registerEvents(listener, plugin);
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin) {
        this.paperEventManager.registerEvent(event, listener, priority, executor, plugin);
    }

    @Override
    public void registerEvent(@NotNull Class<? extends Event> event, @NotNull Listener listener, @NotNull EventPriority priority, @NotNull EventExecutor executor, @NotNull Plugin plugin, boolean ignoreCancelled) {
        this.paperEventManager.registerEvent(event, listener, priority, executor, plugin, ignoreCancelled);
    }

    // Permission manipulation

    @Override
    public @Nullable Permission getPermission(@NotNull String name) {
        return this.permissionManager.getPermission(name);
    }

    @Override
    public void addPermission(@NotNull Permission perm) {
        this.permissionManager.addPermission(perm);
    }

    @Override
    public void removePermission(@NotNull Permission perm) {
        this.permissionManager.removePermission(perm);
    }

    @Override
    public void removePermission(@NotNull String name) {
        this.permissionManager.removePermission(name);
    }

    @Override
    public @NotNull Set<Permission> getDefaultPermissions(boolean op) {
        return this.permissionManager.getDefaultPermissions(op);
    }

    @Override
    public void recalculatePermissionDefaults(@NotNull Permission perm) {
        this.permissionManager.recalculatePermissionDefaults(perm);
    }

    @Override
    public void subscribeToPermission(@NotNull String permission, @NotNull Permissible permissible) {
        this.permissionManager.subscribeToPermission(permission, permissible);
    }

    @Override
    public void unsubscribeFromPermission(@NotNull String permission, @NotNull Permissible permissible) {
        this.permissionManager.unsubscribeFromPermission(permission, permissible);
    }

    @Override
    public @NotNull Set<Permissible> getPermissionSubscriptions(@NotNull String permission) {
        return this.permissionManager.getPermissionSubscriptions(permission);
    }

    @Override
    public void subscribeToDefaultPerms(boolean op, @NotNull Permissible permissible) {
        this.permissionManager.subscribeToDefaultPerms(op, permissible);
    }

    @Override
    public void unsubscribeFromDefaultPerms(boolean op, @NotNull Permissible permissible) {
        this.permissionManager.unsubscribeFromDefaultPerms(op, permissible);
    }

    @Override
    public @NotNull Set<Permissible> getDefaultPermSubscriptions(boolean op) {
        return this.permissionManager.getDefaultPermSubscriptions(op);
    }

    @Override
    public @NotNull Set<Permission> getPermissions() {
        return this.permissionManager.getPermissions();
    }

    @Override
    public void addPermissions(@NotNull List<Permission> perm) {
        this.permissionManager.addPermissions(perm);
    }

    @Override
    public void clearPermissions() {
        this.permissionManager.clearPermissions();
    }

    @Override
    public Permissible createPermissible(@NotNull ServerOperator operator) {
        return this.permissionManager.createPermissible(operator);
    }

    @Override
    public Permissible createCommandBlockPermissible() {
        return this.permissionManager.createCommandBlockPermissible();
    }

    @Override
    public CompletableFuture<Optional<Permissible>> loadPlayerPermissible(@NotNull UUID playerUuid) {
        return this.permissionManager.loadPlayerPermissible(playerUuid);
    }
    // Etc

    @Override
    public boolean useTimings() {
        return co.aikar.timings.Timings.isTimingsEnabled();
    }

    @Override
    public void registerInterface(@NotNull Class<? extends PluginLoader> loader) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    public MutableGraph<String> getInstanceManagerGraph() {
        return instanceManager.getDependencyGraph();
    }
}
