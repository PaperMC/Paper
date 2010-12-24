
package com.dinnerbone.bukkit.sample;

import java.io.File;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Sample plugin for Bukkit
 *
 * @author Dinnerbone
 */
public class SamplePlugin extends JavaPlugin {
    public SamplePlugin(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, plugin, cLoader);
        System.out.println("Johnny five is alive!");
    }

    public void onDisable() {
        System.out.println("Goodbye world!");
    }

    public void onEnable() {
        System.out.println("Hello world!");
    }
}
