
package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jars
 */
public final class JavaPluginLoader implements PluginLoader {
    private final Server server;
    private final Pattern[] fileFilters = new Pattern[] {
        Pattern.compile("\\.jar$"),
    };
    
    public JavaPluginLoader(Server instance) {
        server = instance;
    }
    
    public Plugin loadPlugin(File file) throws InvalidPluginException {
        JavaPlugin result = null;
        PluginDescriptionFile description = new PluginDescriptionFile("Sample Plugin", "org.bukkit.plugin.sample.main");

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(String.format("%s does not exist", file.getPath())));
        }

        try {
            ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
            Class<?> jarClass = Class.forName(description.getMain());
            Class<? extends JavaPlugin> plugin = jarClass.asSubclass(JavaPlugin.class);
            Constructor<? extends JavaPlugin> constructor = plugin.getConstructor(PluginLoader.class, Server.class, PluginDescriptionFile.class, File.class, ClassLoader.class);

            result = constructor.newInstance(this, server, description, file, loader);
        } catch (Exception ex) {
            throw new InvalidPluginException(ex);
        }

        return (Plugin)result;
    }

    public Pattern[] getPluginFileFilters() {
        return fileFilters;
    }

}
