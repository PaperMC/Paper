package io.papermc.paper.plugin.provider;

import org.jetbrains.annotations.ApiStatus;

/**
 * This is used for the /plugins command, where it will look in the {@link io.papermc.paper.plugin.entrypoint.LaunchEntryPointHandler} and
 * use the provider statuses to determine the color.
 */
@ApiStatus.Internal
public enum ProviderStatus {
    INITIALIZED,
    ERRORED,
}
