package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple services manager.
 */
public class SimpleServicesManager implements ServicesManager {

    /**
     * Map of providers.
     */
    private final Map<Class<?>, List<RegisteredServiceProvider<?>>> providers = new HashMap<Class<?>, List<RegisteredServiceProvider<?>>>();

    /**
     * Register a provider of a service.
     *
     * @param <T> Provider
     * @param service service class
     * @param provider provider to register
     * @param plugin plugin with the provider
     * @param priority priority of the provider
     */
    @Override
    public <T> void register(@NotNull Class<T> service, @NotNull T provider, @NotNull Plugin plugin, @NotNull ServicePriority priority) {
        RegisteredServiceProvider<T> registeredProvider = null;
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> registered = providers.get(service);
            if (registered == null) {
                registered = new ArrayList<RegisteredServiceProvider<?>>();
                providers.put(service, registered);
            }

            registeredProvider = new RegisteredServiceProvider<T>(service, provider, priority, plugin);

            // Insert the provider into the collection, much more efficient big O than sort
            int position = Collections.binarySearch(registered, registeredProvider);
            if (position < 0) {
                registered.add(-(position + 1), registeredProvider);
            } else {
                registered.add(position, registeredProvider);
            }

        }
        Bukkit.getServer().getPluginManager().callEvent(new ServiceRegisterEvent(registeredProvider));
    }

    /**
     * Unregister all the providers registered by a particular plugin.
     *
     * @param plugin The plugin
     */
    @Override
    public void unregisterAll(@NotNull Plugin plugin) {
        ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
        synchronized (providers) {
            Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it = providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();
                    Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();

                    try {
                        // Removed entries that are from this plugin

                        while (it2.hasNext()) {
                            RegisteredServiceProvider<?> registered = it2.next();

                            if (registered.getPlugin().equals(plugin)) {
                                it2.remove();
                                unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                            }
                        }
                    } catch (NoSuchElementException e) { // Why does Java suck
                    }

                    // Get rid of the empty list
                    if (entry.getValue().size() == 0) {
                        it.remove();
                    }
                }
            } catch (NoSuchElementException e) {}
        }
        for (ServiceUnregisterEvent event : unregisteredEvents) {
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Unregister a particular provider for a particular service.
     *
     * @param service The service interface
     * @param provider The service provider implementation
     */
    @Override
    public void unregister(@NotNull Class<?> service, @NotNull Object provider) {
        ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
        synchronized (providers) {
            Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it = providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();

                    // We want a particular service
                    if (entry.getKey() != service) {
                        continue;
                    }

                    Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();

                    try {
                        // Removed entries that are from this plugin

                        while (it2.hasNext()) {
                            RegisteredServiceProvider<?> registered = it2.next();

                            if (registered.getProvider() == provider) {
                                it2.remove();
                                unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                            }
                        }
                    } catch (NoSuchElementException e) { // Why does Java suck
                    }

                    // Get rid of the empty list
                    if (entry.getValue().size() == 0) {
                        it.remove();
                    }
                }
            } catch (NoSuchElementException e) {}
        }
        for (ServiceUnregisterEvent event : unregisteredEvents) {
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Unregister a particular provider.
     *
     * @param provider The service provider implementation
     */
    @Override
    public void unregister(@NotNull Object provider) {
        ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
        synchronized (providers) {
            Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it = providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();
                    Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();

                    try {
                        // Removed entries that are from this plugin

                        while (it2.hasNext()) {
                            RegisteredServiceProvider<?> registered = it2.next();

                            if (registered.getProvider().equals(provider)) {
                                it2.remove();
                                unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                            }
                        }
                    } catch (NoSuchElementException e) { // Why does Java suck
                    }

                    // Get rid of the empty list
                    if (entry.getValue().size() == 0) {
                        it.remove();
                    }
                }
            } catch (NoSuchElementException e) {}
        }
        for (ServiceUnregisterEvent event : unregisteredEvents) {
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    /**
     * Queries for a provider. This may return if no provider has been
     * registered for a service. The highest priority provider is returned.
     *
     * @param <T> The service interface
     * @param service The service interface
     * @return provider or null
     */
    @Override
    @Nullable
    public <T> T load(@NotNull Class<T> service) {
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> registered = providers.get(service);

            if (registered == null) {
                return null;
            }

            // This should not be null!
            return service.cast(registered.get(0).getProvider());
        }
    }

    /**
     * Queries for a provider registration. This may return if no provider
     * has been registered for a service.
     *
     * @param <T> The service interface
     * @param service The service interface
     * @return provider registration or null
     */
    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> RegisteredServiceProvider<T> getRegistration(@NotNull Class<T> service) {
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> registered = providers.get(service);

            if (registered == null) {
                return null;
            }

            // This should not be null!
            return (RegisteredServiceProvider<T>) registered.get(0);
        }
    }

    /**
     * Get registrations of providers for a plugin.
     *
     * @param plugin The plugin
     * @return provider registrations
     */
    @Override
    @NotNull
    public List<RegisteredServiceProvider<?>> getRegistrations(@NotNull Plugin plugin) {
        ImmutableList.Builder<RegisteredServiceProvider<?>> ret = ImmutableList.<RegisteredServiceProvider<?>>builder();
        synchronized (providers) {
            for (List<RegisteredServiceProvider<?>> registered : providers.values()) {
                for (RegisteredServiceProvider<?> provider : registered) {
                    if (provider.getPlugin().equals(plugin)) {
                        ret.add(provider);
                    }
                }
            }
        }
        return ret.build();
    }

    /**
     * Get registrations of providers for a service. The returned list is
     * an unmodifiable copy.
     *
     * @param <T> The service interface
     * @param service The service interface
     * @return a copy of the list of registrations
     */
    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <T> List<RegisteredServiceProvider<T>> getRegistrations(@NotNull Class<T> service) {
        ImmutableList.Builder<RegisteredServiceProvider<T>> ret;
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> registered = providers.get(service);

            if (registered == null) {
                return ImmutableList.<RegisteredServiceProvider<T>>of();
            }

            ret = ImmutableList.<RegisteredServiceProvider<T>>builder();

            for (RegisteredServiceProvider<?> provider : registered) {
                ret.add((RegisteredServiceProvider<T>) provider);
            }

        }
        return ret.build();
    }

    /**
     * Get a list of known services. A service is known if it has registered
     * providers for it.
     *
     * @return a copy of the set of known services
     */
    @Override
    @NotNull
    public Set<Class<?>> getKnownServices() {
        synchronized (providers) {
            return ImmutableSet.<Class<?>>copyOf(providers.keySet());
        }
    }

    /**
     * Returns whether a provider has been registered for a service.
     *
     * @param <T> service
     * @param service service to check
     * @return true if and only if there are registered providers
     */
    @Override
    public <T> boolean isProvidedFor(@NotNull Class<T> service) {
        synchronized (providers) {
            return providers.containsKey(service);
        }
    }
}
