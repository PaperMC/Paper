package org.bukkit.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A simple services manager.
 * 
 * @author sk89q
 */
public class SimpleServicesManager implements ServicesManager {

    /**
     * Map of providers.
     */
    private final Map<Class<?>, List<RegisteredServiceProvider<?>>> providers =
            new HashMap<Class<?>, List<RegisteredServiceProvider<?>>>();

    /**
     * Register a provider of a service.
     * 
     * @param <T> Provider
     * @param service service class
     * @param provider provider to register
     * @param plugin plugin with the provider
     * @param priority priority of the provider
     */
    public <T> void register(Class<T> service, T provider,
            Plugin plugin, ServicePriority priority) {
        
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> registered =
                    providers.get(service);
            
            if (registered == null) {
                registered = new ArrayList<RegisteredServiceProvider<?>>();
                providers.put(service, registered);
            }
            
            registered.add(new RegisteredServiceProvider<T>(
                    service, provider, priority, plugin));
            
            // Make sure that providers are in the right order in order
            // for priorities to work correctly
            Collections.sort(registered);
        }
    }

    /**
     * Unregister all the providers registered by a particular plugin.
     * 
     * @param plugin
     */
    public void unregisterAll(Plugin plugin) {
        synchronized (providers) {
            Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it =
                providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();
                    Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();
                    
                    try {
                        // Removed entries that are from this plugin
                        while (it2.hasNext()) {
                            if (it2.next().getPlugin() == plugin) {
                                it2.remove();
                            }
                        }
                    } catch (NoSuchElementException e) {
                        // Why does Java suck
                    }
                    
                    // Get rid of the empty list
                    if (entry.getValue().size() == 0) {
                        it.remove();
                    }
                }
            } catch (NoSuchElementException e) {
            }
        }
    }

    /**
     * Unregister a particular provider for a particular service.
     * 
     * @param service 
     * @param provider
     */
    public void unregister(Class<?> service, Object provider) {
        synchronized (providers) {
            Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it =
                providers.entrySet().iterator();

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
                            if (it2.next().getProvider() == provider) {
                                it2.remove();
                            }
                        }
                    } catch (NoSuchElementException e) {
                        // Why does Java suck
                    }
                    
                    // Get rid of the empty list
                    if (entry.getValue().size() == 0) {
                        it.remove();
                    }
                }
            } catch (NoSuchElementException e) {
            }
        }
    }

    /**
     * Unregister a particular provider.
     * 
     * @param provider
     */
    public void unregister(Object provider) {
        synchronized (providers) {
            Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it =
                providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();
                    Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();
                    
                    try {
                        // Removed entries that are from this plugin
                        while (it2.hasNext()) {
                            if (it2.next().getProvider() == provider) {
                                it2.remove();
                            }
                        }
                    } catch (NoSuchElementException e) {
                        // Why does Java suck
                    }
                    
                    // Get rid of the empty list
                    if (entry.getValue().size() == 0) {
                        it.remove();
                    }
                }
            } catch (NoSuchElementException e) {
            }
        }
    }
    
    /**
     * Queries for a provider. This may return if no provider has been
     * registered for a service. The highest priority provider is returned.
     * 
     * @param <T>
     * @param service
     * @return provider or null
     */
    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> service) {
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> registered = providers.get(service);
            
            if (registered == null) {
                return null;
            }
            
            // This should not be null!
            return (T) registered.get(0).getProvider();
        }
    }

    /**
     * Queries for a provider registration. This may return if no provider
     * has been registered for a service.
     * 
     * @param <T>
     * @param service
     * @return provider registration or null
     */
    @SuppressWarnings("unchecked")
    public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service) {
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
     * @param plugin
     * @return provider registration or null
     */
    public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin) {
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> ret = 
                    new ArrayList<RegisteredServiceProvider<?>>();
            
            for (List<RegisteredServiceProvider<?>> registered : providers.values()) {
                for (RegisteredServiceProvider<?> provider : registered) {
                    if (provider.getPlugin() == plugin) {
                        ret.add(provider);
                    }
                }
            }
            
            return ret;
        }
    }

    /**
     * Get registrations of providers for a service. The returned list is
     * unmodifiable.
     * 
     * @param <T>
     * @param service
     * @return list of registrations
     */
    @SuppressWarnings("unchecked")
    public <T> Collection<RegisteredServiceProvider<T>> getRegistrations(Class<T> service) {
        synchronized (providers) {
            List<RegisteredServiceProvider<?>> registered = providers.get(service);
            
            if (registered == null) {
                return Collections.unmodifiableList(
                        new ArrayList<RegisteredServiceProvider<T>>());
            }
            
            List<RegisteredServiceProvider<T>> ret =
                    new ArrayList<RegisteredServiceProvider<T>>();
            
            for (RegisteredServiceProvider<?> provider : registered) {
                ret.add((RegisteredServiceProvider<T>) provider);
            }
            
            return Collections.unmodifiableList(ret);
        }
    }

    /**
     * Get a list of known services. A service is known if it has registered
     * providers for it.
     * 
     * @return list of known services
     */
    public Collection<Class<?>> getKnownServices() {
        return Collections.unmodifiableSet(providers.keySet());
    }

    /**
     * Returns whether a provider has been registered for a service. Do not
     * check this first only to call <code>load(service)</code> later, as that
     * would be a non-thread safe situation. 
     * 
     * @param <T> service
     * @param service service to check
     * @return whether there has been a registered provider
     */
    public <T> boolean isProvidedFor(Class<T> service) {
        return getRegistration(service) != null;
    }
}
