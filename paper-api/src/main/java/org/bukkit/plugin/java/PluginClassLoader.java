package org.bukkit.plugin.java;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
@org.jetbrains.annotations.ApiStatus.Internal // Paper
public final class PluginClassLoader extends URLClassLoader implements io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader { // Paper
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    private final ClassLoader libraryLoader;
    final JavaPlugin plugin;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    private final Set<String> seenIllegalAccess = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private java.util.logging.Logger logger; // Paper - add field
    private io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup classLoaderGroup; // Paper
    public io.papermc.paper.plugin.provider.entrypoint.DependencyContext dependencyContext; // Paper

    static {
        ClassLoader.registerAsParallelCapable();
    }

    /**
     * @hidden
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public PluginClassLoader(@Nullable final ClassLoader parent, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file, @Nullable ClassLoader libraryLoader, JarFile jarFile, io.papermc.paper.plugin.provider.entrypoint.DependencyContext dependencyContext) throws IOException, InvalidPluginException, MalformedURLException { // Paper - use JarFile provided by SpigotPluginProvider
        super(file.getName(), new URL[] {file.toURI().toURL()}, parent);
        this.loader = null; // Paper - pass null into loader field

        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = jarFile; // Paper - use JarFile provided by SpigotPluginProvider
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();
        this.libraryLoader = libraryLoader;

        this.logger = com.destroystokyo.paper.utils.PaperPluginLogger.getLogger(description); // Paper - Register logger early
        // Paper start
        this.dependencyContext = dependencyContext;
        this.classLoaderGroup = io.papermc.paper.plugin.provider.classloader.PaperClassLoaderStorage.instance().registerSpigotGroup(this);
        // Paper end

        Class<?> jarClass;
        try {
            jarClass = Class.forName(description.getMain(), true, this);
        } catch (ClassNotFoundException ex) {
            throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
        }

        Class<? extends JavaPlugin> pluginClass;
        try {
            pluginClass = jarClass.asSubclass(JavaPlugin.class);
        } catch (ClassCastException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' must extend JavaPlugin", ex);
        }

        Constructor<? extends JavaPlugin> pluginConstructor;
        try {
            pluginConstructor = pluginClass.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' must have a no-args constructor", ex);
        }

        try {
            // Support non-public constructors
            pluginConstructor.setAccessible(true);
        } catch (InaccessibleObjectException | SecurityException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' constructor inaccessible", ex);
        }

        try {
            plugin = pluginConstructor.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' constructor inaccessible", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' must not be abstract", ex);
        } catch (IllegalArgumentException ex) {
            throw new InvalidPluginException("Could not invoke main class `" + description.getMain() + "' constructor", ex);
        } catch (ExceptionInInitializerError | InvocationTargetException ex) {
            throw new InvalidPluginException("Exception initializing main class `" + description.getMain() + "'", ex);
        }
    }

    @Override
    public URL getResource(String name) {
        // Paper start
        URL resource = findResource(name);
        if (resource == null && libraryLoader != null) {
            return libraryLoader.getResource(name);
        }
        return resource;
        // Paper end
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        // Paper start
        java.util.ArrayList<URL> resources = new java.util.ArrayList<>();
        addEnumeration(resources, findResources(name));
        if (libraryLoader != null) {
            addEnumeration(resources, libraryLoader.getResources(name));
        }
        return Collections.enumeration(resources);
        // Paper end
    }

    // Paper start
    private <T> void addEnumeration(java.util.ArrayList<T> list, Enumeration<T> enumeration) {
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
    }
    // Paper end

    // Paper start
    @Override
    public Class<?> loadClass(@NotNull String name, boolean resolve, boolean checkGlobal, boolean checkLibraries) throws ClassNotFoundException {
        return this.loadClass0(name, resolve, checkGlobal, checkLibraries);
    }
    @Override
    public io.papermc.paper.plugin.configuration.PluginMeta getConfiguration() {
        return this.description;
    }

    @Override
    public void init(JavaPlugin plugin) {
        this.initialize(plugin);
    }

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    // Paper end

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClass0(name, resolve, true, true);
    }

    Class<?> loadClass0(@NotNull String name, boolean resolve, boolean checkGlobal, boolean checkLibraries) throws ClassNotFoundException {
        try {
            Class<?> result = super.loadClass(name, resolve);

            // SPIGOT-6749: Library classes will appear in the above, but we don't want to return them to other plugins
            if (checkGlobal || result.getClassLoader() == this) {
                return result;
            }
        } catch (ClassNotFoundException ex) {
        }

        if (checkLibraries && libraryLoader != null) {
            try {
                return libraryLoader.loadClass(name);
            } catch (ClassNotFoundException ex) {
            }
        }

        if (checkGlobal) {
            // This ignores the libraries of other plugins, unless they are transitive dependencies.
            Class<?> result = this.classLoaderGroup.getClassByName(name, resolve, this); // Paper

            if (result != null) {
                // If the class was loaded from a library instead of a PluginClassLoader, we can assume that its associated plugin is a transitive dependency and can therefore skip this check.
                // Paper - Totally delete the illegal access logic, we are never going to enforce it anyways here.

                return result;
            }
        }

        throw new ClassNotFoundException(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            String path = name.replace('.', '/').concat(".class");
            // Add details to zip file errors - help debug classloading
            JarEntry entry;
            try {
                entry = jar.getJarEntry(path);
            } catch (IllegalStateException zipFileClosed) {
                if (plugin == null) {
                    throw zipFileClosed;
                }
                throw new IllegalStateException("The plugin classloader for " + plugin.getName() + " has thrown a zip file error.", zipFileClosed);
            }

            if (entry != null) {
                byte[] classBytes;

                try (InputStream is = jar.getInputStream(entry)) {
                    classBytes = ByteStreams.toByteArray(is);
                } catch (IOException ex) {
                    throw new ClassNotFoundException(name, ex);
                }

                classBytes = org.bukkit.Bukkit.getServer().getUnsafe().processClass(description, path, classBytes); // Paper

                int dot = name.lastIndexOf('.');
                if (dot != -1) {
                    String pkgName = name.substring(0, dot);
                    if (getPackage(pkgName) == null) {
                        try {
                            if (manifest != null) {
                                definePackage(pkgName, manifest, url);
                            } else {
                                definePackage(pkgName, null, null, null, null, null, null, null);
                            }
                        } catch (IllegalArgumentException ex) {
                            if (getPackage(pkgName) == null) {
                                throw new IllegalStateException("Cannot find package " + pkgName);
                            }
                        }
                    }
                }

                CodeSigner[] signers = entry.getCodeSigners();
                CodeSource source = new CodeSource(url, signers);

                result = defineClass(name, classBytes, 0, classBytes.length, source);
            }

            if (result == null) {
                result = super.findClass(name);
            }

            classes.put(name, result);
            this.setClass(name, result); // Paper
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            // Paper start
            Collection<Class<?>> classes = getClasses();
            for (Class<?> clazz : classes) {
                removeClass(clazz);
            }
            // Paper end
            super.close();
        } finally {
            jar.close();
        }
    }

    @NotNull
    Collection<Class<?>> getClasses() {
        return classes.values();
    }

    public synchronized void initialize(@NotNull JavaPlugin javaPlugin) { // Paper
        Preconditions.checkArgument(javaPlugin != null, "Initializing plugin cannot be null");
        Preconditions.checkArgument(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(org.bukkit.Bukkit.getServer(), description, dataFolder, file, this, description, this.logger); // Paper
    }

    // Paper start
    @Override
    public String toString() {
        JavaPlugin currPlugin = plugin != null ? plugin : pluginInit;
        return "PluginClassLoader{" +
                   "plugin=" + currPlugin +
                   ", pluginEnabled=" + (currPlugin == null ? "uninitialized" : currPlugin.isEnabled()) +
                   ", url=" + file +
                   '}';
    }

    void setClass(@NotNull final String name, @NotNull final Class<?> clazz) {
        if (org.bukkit.configuration.serialization.ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends org.bukkit.configuration.serialization.ConfigurationSerializable> serializable = clazz.asSubclass(org.bukkit.configuration.serialization.ConfigurationSerializable.class);
            org.bukkit.configuration.serialization.ConfigurationSerialization.registerClass(serializable);
        }
    }

    private void removeClass(@NotNull Class<?> clazz) {
        if (org.bukkit.configuration.serialization.ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends org.bukkit.configuration.serialization.ConfigurationSerializable> serializable = clazz.asSubclass(org.bukkit.configuration.serialization.ConfigurationSerializable.class);
            org.bukkit.configuration.serialization.ConfigurationSerialization.unregisterClass(serializable);
        }
    }

    @Override
    public @Nullable io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup getGroup() {
        return this.classLoaderGroup;
    }

    // Paper end
}
