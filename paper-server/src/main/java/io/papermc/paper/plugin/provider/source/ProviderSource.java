package io.papermc.paper.plugin.provider.source;

import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import java.io.IOException;

/**
 * A provider source is responsible for giving PluginTypes an EntrypointHandler for
 * registering providers at.
 *
 * @param <I> input context
 * @param <C> context
 */
public interface ProviderSource<I, C> {

    /**
     * Prepares the context for use in {@link #registerProviders(EntrypointHandler, Object)}.
     *
     * @param context the context to prepare
     * @return the prepared context, ready for use in {@link #registerProviders(EntrypointHandler, Object)}
     * @throws IOException if an error occurs while preparing the context
     */
    C prepareContext(I context) throws IOException;

    /**
     * Uses the prepared context to register providers at the given entrypoint handler.
     *
     * @param entrypointHandler the entrypoint handler to register providers at
     * @param context           the context to register providers at
     * @throws Exception if an error occurs while registering providers
     */
    void registerProviders(EntrypointHandler entrypointHandler, C context) throws Exception;
}
