package org.bukkit.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;

public class BukkitTestPlugin extends PluginBase {
    private boolean enabled = true;

    private final String pluginName;

    public BukkitTestPlugin(String pluginName) {
        this.pluginName = pluginName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public File getDataFolder() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return new PluginDescriptionFile(pluginName, "1.0", "test.test");
    }
    // Paper start
    @Override
    public io.papermc.paper.plugin.configuration.PluginMeta getPluginMeta() {
        return getDescription();
    }
    // Paper end

    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public InputStream getResource(String filename) {
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
    public void saveResource(String resourcePath, boolean replace) {
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
        return enabled;
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
    public void setNaggable(boolean canNag) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(String worldName, String id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    // Paper start - lifecycle events
    @Override
    public io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager<Plugin> getLifecycleManager() {
        throw new UnsupportedOperationException("Not supported.");
    }
    // Paper end - lifecycle events
}
