package org.bukkit.plugin.java;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final Server server;
    private final File dataFolder;
    private final File file;

    /**
     * Changed in favor of class loader that initializes plugins
     */
    @Deprecated
    public PluginClassLoader(final JavaPluginLoader loader, final URL[] urls, final ClassLoader parent) {
        this(loader, urls, parent, null, null, null, null);
    }

    public PluginClassLoader(final JavaPluginLoader loader, final URL[] urls, final ClassLoader parent, final Server server, final PluginDescriptionFile description, final File dataFolder, final File file) {
        super(urls, parent);

        this.loader = loader;
        this.server = server;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
    }

    @Override
    public void addURL(URL url) { // Override for access level!
        super.addURL(url);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);
            }

            if (result == null) {
                result = super.findClass(name);

                if (result != null) {
                    loader.setClass(name, result);
                }
            }

            classes.put(name, result);
        }

        return result;
    }

    public Set<String> getClasses() {
        return classes.keySet();
    }

    void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");

        if (server != null && description != null && dataFolder != null && file != null) {
            javaPlugin.initialize(loader, server, description, dataFolder, file, this);
        }
    }
}
