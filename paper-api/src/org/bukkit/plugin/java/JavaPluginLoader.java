
package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.*;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jar
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
        PluginDescriptionFile description = null;

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(String.format("%s does not exist", file.getPath())));
        }
        try {
            JarFile jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new InvalidPluginException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            InputStream stream = jar.getInputStream(entry);
            description = new PluginDescriptionFile(stream);

            stream.close();
            jar.close();
        } catch (IOException ex) {
            throw new InvalidPluginException(ex);
        } catch (InvalidDescriptionException ex) {
            throw new InvalidPluginException(ex);
        }

        try {
            ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
            Class<?> jarClass = Class.forName(description.getMain(), true, loader);
            Class<? extends JavaPlugin> plugin = jarClass.asSubclass(JavaPlugin.class);
            Constructor<? extends JavaPlugin> constructor = plugin.getConstructor(PluginLoader.class, Server.class, PluginDescriptionFile.class, File.class, ClassLoader.class);

            result = constructor.newInstance(this, server, description, file, loader);
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }

        return (Plugin)result;
    }

    public Pattern[] getPluginFileFilters() {
        return fileFilters;
    }

    public void callEvent(RegisteredListener registration, Event event) {
        Listener listener = registration.getListener();

        if (listener instanceof PlayerListener) {
            PlayerListener trueListener = (PlayerListener)listener;

            switch (event.getType()) {
                case PLAYER_JOIN:
                    trueListener.onPlayerJoin((PlayerEvent)event);
                    break;
                case PLAYER_QUIT:
                    trueListener.onPlayerQuit((PlayerEvent)event);
                    break;
                case PLAYER_COMMAND:
                    trueListener.onPlayerCommand((PlayerChatEvent)event);
                    break;
                case PLAYER_CHAT:
                    trueListener.onPlayerChat((PlayerChatEvent)event);
                    break;
            }
        }
    }
}
