package io.papermc.paper.plugin.provider;

/**
 * This is used to mark that a plugin provider is able to hold a status for the /plugins command.
 */
public interface ProviderStatusHolder {

    ProviderStatus getLastProvidedStatus();

    void setStatus(ProviderStatus status);
}
