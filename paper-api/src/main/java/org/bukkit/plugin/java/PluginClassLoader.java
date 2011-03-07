package org.bukkit.plugin.java;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

    public PluginClassLoader(final JavaPluginLoader loader, final URL[] urls, final ClassLoader parent) {
        super(urls, parent);

        this.loader = loader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = classes.get(name);

        if (result == null) {
            ClassNotFoundException ex = null;

            try {
                result = super.findClass(name);
            } catch (ClassNotFoundException e) {
                ex = e;
            }

            if (result != null) {
                loader.setClass(name, result);
            } else {
                result = loader.getClassByName(name);
            }

            if (result != null ) {
                classes.put(name, result);
            } else {
                throw ex;
            }
        }

        return result;
    }

    public Set<String> getClasses() {
        return classes.keySet();
    }
}
