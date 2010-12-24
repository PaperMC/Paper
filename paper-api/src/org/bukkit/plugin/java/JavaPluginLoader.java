
package org.bukkit.plugin.java;

import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import java.util.regex.Pattern;
import org.bukkit.Server;

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
    
    public Plugin getPlugin(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPluginEnabled(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPluginEnabled(Plugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Plugin loadPlugin(File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Pattern[] getPluginFileFilters() {
        return fileFilters;
    }

}
