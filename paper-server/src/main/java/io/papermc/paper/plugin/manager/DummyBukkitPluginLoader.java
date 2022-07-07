package io.papermc.paper.plugin.manager;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.type.PluginFileType;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * A purely internal type that implements the now deprecated {@link PluginLoader} after the implementation
 * of papers new plugin system.
 */
@ApiStatus.Internal
public class DummyBukkitPluginLoader implements PluginLoader {

    private static final Pattern[] PATTERNS = new Pattern[0];

    @Override
    public @NotNull Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, UnknownDependencyException {
        try {
            return PaperPluginManagerImpl.getInstance().loadPlugin(file);
        } catch (InvalidDescriptionException e) {
            throw new InvalidPluginException(e);
        }
    }

    @Override
    public @NotNull PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException {
        try (JarFile jar = new JarFile(file)) {
            PluginFileType<?, ?> type = PluginFileType.guessType(jar);
            if (type == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            PluginMeta meta = type.getConfig(jar);
            if (meta instanceof PluginDescriptionFile pluginDescriptionFile) {
                return pluginDescriptionFile;
            } else {
                throw new InvalidDescriptionException("Plugin type does not use plugin.yml. Cannot read file description.");
            }
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    @Override
    public @NotNull Pattern[] getPluginFileFilters() {
        return PATTERNS;
    }

    @Override
    public @NotNull Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener, @NotNull Plugin plugin) {
        return PaperPluginManagerImpl.getInstance().paperEventManager.createRegisteredListeners(listener, plugin);
    }

    @Override
    public void enablePlugin(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}
