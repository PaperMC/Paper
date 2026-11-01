package org.bukkit.plugin;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BukkitTestPlugin extends PluginBase {
    private boolean enabled = true;

    private final String pluginName;

    public BukkitTestPlugin(final String pluginName) {
        this.pluginName = pluginName;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public File getDataFolder() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return new PluginDescriptionFile(this.pluginName, "1.0", "test.test");
    }

    // Paper start
    @Override
    public PluginMeta getPluginMeta() {
        return this.getDescription();
    }
    // Paper end

    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable InputStream getResource(final String filename) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void saveResource(final String resourcePath, final boolean replace) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public PluginLogger getLogger() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Server getServer() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void onDisable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void onLoad() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void onEnable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setNaggable(final boolean canNag) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(final String worldName, final @Nullable String id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(final String worldName, final @Nullable String id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @Nullable List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    // Paper start - lifecycle events
    @Override
    public LifecycleEventManager<Plugin> getLifecycleManager() {
        throw new UnsupportedOperationException("Not supported.");
    }
    // Paper end - lifecycle events
}
