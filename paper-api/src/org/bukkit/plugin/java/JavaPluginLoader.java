
package org.bukkit.plugin.java;

import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import java.util.regex.Pattern;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jars
 */
public final class JavaPluginLoader implements PluginLoader {
    private final Pattern[] fileFilters = new Pattern[] {
        Pattern.compile("\\.jar$"),
    };
    
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
