package org.bukkit.plugin;

import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages services and service providers. Services are an interface
 * specifying a list of methods that a provider must implement. Providers are
 * implementations of these services. A provider can be queried from the
 * services manager in order to use a service (if one is available). If
 * multiple plugins register a service, then the service with the highest
 * priority takes precedence.
 */
public interface ServicesManager {

    /**
     * Register a provider of a service.
     *
     * @param <T> Provider
     * @param service service class
     * @param provider provider to register
     * @param plugin plugin with the provider
     * @param priority priority of the provider
     */
    public <T> void register(@NotNull Class<T> service, @NotNull T provider, @NotNull Plugin plugin, @NotNull ServicePriority priority);

    /**
     * Unregister all the providers registered by a particular plugin.
     *
     * @param plugin The plugin
     */
    public void unregisterAll(@NotNull Plugin plugin);

    /**
     * Unregister a particular provider for a particular service.
     *
     * @param service The service interface
     * @param provider The service provider implementation
     */
    public void unregister(@NotNull Class<?> service, @NotNull Object provider);

    /**
     * Unregister a particular provider.
     *
     * @param provider The service provider implementation
     */
    public void unregister(@NotNull Object provider);

    /**
     * Queries for a provider. This may return null if no provider has been
     * registered for a service. The highest priority provider is returned.
     *
     * @param <T> The service interface
     * @param service The service interface
     * @return provider or null
     */
    @Nullable
    public <T> T load(@NotNull Class<T> service);

    /**
     * Queries for a provider registration. This may return null if no provider
     * has been registered for a service.
     *
     * @param <T> The service interface
     * @param service The service interface
     * @return provider registration or null
     */
    @Nullable
    public <T> RegisteredServiceProvider<T> getRegistration(@NotNull Class<T> service);

    /**
     * Get registrations of providers for a plugin.
     *
     * @param plugin The plugin
     * @return provider registrations
     */
    @NotNull
    public List<RegisteredServiceProvider<?>> getRegistrations(@NotNull Plugin plugin);

    /**
     * Get registrations of providers for a service. The returned list is
     * unmodifiable.
     *
     * @param <T> The service interface
     * @param service The service interface
     * @return list of registrations
     */
    @NotNull
    public <T> Collection<RegisteredServiceProvider<T>> getRegistrations(@NotNull Class<T> service);

    /**
     * Get a list of known services. A service is known if it has registered
     * providers for it.
     *
     * @return list of known services
     */
    @NotNull
    public Collection<Class<?>> getKnownServices();

    /**
     * Returns whether a provider has been registered for a service. Do not
     * check this first only to call <code>load(service)</code> later, as that
     * would be a non-thread safe situation.
     *
     * @param <T> service
     * @param service service to check
     * @return whether there has been a registered provider
     */
    public <T> boolean isProvidedFor(@NotNull Class<T> service);

}
