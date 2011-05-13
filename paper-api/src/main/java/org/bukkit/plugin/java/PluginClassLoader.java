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
        return findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        // We use the following load order:
        // 1. Local first, this avoids IllegalAccessError exceptions for duplicate classes
        // 2. Global cache second which prevents ClassCastException's apparently
        Class<?> result = classes.get(name);
        if ( null == result ) {
            try {
                result = super.findClass(name);
                classes.put(name, result);
                loader.setClass(name, result);
            } catch ( ClassNotFoundException e ) {
                if ( checkGlobal ) {
                    result = loader.getClassByName(name);
                    if ( null == result ) {
                        // We really couldnt find it
                        throw new ClassNotFoundException(name);
                    }
                } else {
                    throw e; // no more options just rethrow
                }
            }
        }

        // NOTE: setClass already does a not exists check
        loader.setClass(name, result);

        return result;
    }


    public Set<String> getClasses() {
        return classes.keySet();
    }
}
