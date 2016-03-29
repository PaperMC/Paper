package org.bukkit.craftbukkit.scheduler;


import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class MinecraftInternalPlugin extends PluginBase {
    private boolean enabled = true;

    private final String pluginName;
    private PluginDescriptionFile pdf;

    public MinecraftInternalPlugin() {
        this.pluginName = "Minecraft";
        pdf = new PluginDescriptionFile(pluginName, "1.0", "nms");
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
        return pdf;
    }

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
