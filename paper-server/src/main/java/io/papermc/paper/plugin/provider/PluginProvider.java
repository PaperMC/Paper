package io.papermc.paper.plugin.provider;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * PluginProviders are created by a {@link io.papermc.paper.plugin.provider.source.ProviderSource},
 * which is loaded into an {@link io.papermc.paper.plugin.entrypoint.EntrypointHandler}.
 * <p>
 * A PluginProvider is responsible for providing part of a plugin, whether it's a Bootstrapper or Server Plugin.
 * The point of this class is to be able to create the actual instance later, as at the time this is created the server
 * may be missing some key parts. For example, the Bukkit singleton will not be initialized yet, therefor we need to
 * have a PluginServerProvider load the server plugin later.
 * <p>
 * Plugin providers are currently not exposed in any way of the api. It is preferred that this stays this way,
 * as providers are only needed for initialization.
 *
 * @param <T> provider type
 */
@ApiStatus.Internal
public interface PluginProvider<T> {

    @NotNull
    Path getSource();

    default Path getFileName() {
        return this.getSource().getFileName();
    }

    default Path getParentSource() {
        return this.getSource().getParent();
    }

    JarFile file();

    T createInstance();

    PluginMeta getMeta();

    ComponentLogger getLogger();

    LoadOrderConfiguration createConfiguration(@NotNull Map<String, PluginProvider<?>> toLoad);

    // Returns a list of missing dependencies
    List<String> validateDependencies(@NotNull DependencyContext context);

}
