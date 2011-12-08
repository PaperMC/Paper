package org.bukkit.plugin.messaging;

import java.io.File;
import java.io.InputStream;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

import com.avaje.ebean.EbeanServer;

public class TestPlugin extends Plugin {
    private boolean enabled = true;

    final private String pluginName;

    public TestPlugin(String pluginName) {
        this.pluginName = pluginName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return pluginName;
    }

    public File getDataFolder() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public PluginDescriptionFile getDescription() {
        return new PluginDescriptionFile(pluginName, "1.0", "test.test");
    }

    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public InputStream getResource(String filename) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void saveConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void saveDefaultConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void saveResource(String resourcePath, boolean replace) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void reloadConfig() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public PluginLogger getLogger() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public PluginLoader getPluginLoader() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Server getServer() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void onDisable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void onLoad() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void onEnable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isNaggable() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void setNaggable(boolean canNag) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public EbeanServer getDatabase() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return getName().equals(((TestPlugin) obj).getName());
    }
}
