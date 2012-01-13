package org.bukkit.plugin.messaging;

import com.avaje.ebean.EbeanServer;
import java.io.File;
import java.io.InputStream;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event.Type;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.util.config.Configuration;

public class TestPlugin implements Plugin {
    private boolean enabled = true;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public File getDataFolder() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public PluginDescriptionFile getDescription() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Configuration getConfiguration() {
        throw new UnsupportedOperationException("Not supported.");
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

    public long getTiming(Type type) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void incTiming(Type type, long delta) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void resetTimings() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
