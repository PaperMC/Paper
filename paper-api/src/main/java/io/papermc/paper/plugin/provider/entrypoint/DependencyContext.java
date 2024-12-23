package io.papermc.paper.plugin.provider.entrypoint;

import io.papermc.paper.plugin.configuration.PluginMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * A dependency context is a read-only abstraction of a type/concept that can resolve dependencies between plugins.
 * <p>
 * This may for example be the server wide plugin manager itself, capable of validating if a dependency exists between
 * two {@link PluginMeta} instances, however the implementation is not limited to such a concrete use-case.
 */
@NullMarked
@ApiStatus.Internal
public interface DependencyContext {

    /**
     * Computes if the passed {@link PluginMeta} defined the passed dependency as a transitive dependency.
     * A transitive dependency, as implied by its name, may not have been configured directly by the passed plugin
     * but could also simply be a dependency of a dependency.
     * <p>
     * A simple example of this method would be
     * <pre>{@code
     * dependencyContext.isTransitiveDependency(pluginMetaA, pluginMetaC);
     * }</pre>
     * which would return {@code true} if {@code pluginMetaA} directly or indirectly depends on {@code pluginMetaC}.
     *
     * @param plugin the plugin meta this computation should consider the requester of the dependency status for the
     *               passed potential dependency.
     * @param depend the potential transitive dependency of the {@code plugin} parameter.
     * @return a simple boolean flag indicating if {@code plugin} considers {@code depend} as a transitive dependency.
     */
    boolean isTransitiveDependency(PluginMeta plugin, PluginMeta depend);

    /**
     * Computes if this dependency context is aware of a dependency that provides/matches the passed identifier.
     * <p>
     * A dependency in this methods context is any dependable artefact. It does not matter if anything actually depends
     * on said artefact, its mere existence as a potential dependency is enough for this method to consider it a
     * dependency. If this dependency context is hence aware of an artefact with the matching identifier, this
     * method returns {@code true}.
     *
     * @param pluginIdentifier the unique identifier of the dependency with which to probe this dependency context.
     * @return a plain boolean flag indicating if this dependency context is aware of a potential dependency with the
     * passed identifier.
     */
    boolean hasDependency(String pluginIdentifier);

}
