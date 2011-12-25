package org.bukkit.plugin;

/**
 * A registered service provider.
 *
 * @param <T> Service
 */
public class RegisteredServiceProvider<T> implements Comparable<RegisteredServiceProvider<?>> {

    private Class<T> service;
    private Plugin plugin;
    private T provider;
    private ServicePriority priority;

    public RegisteredServiceProvider(Class<T> service, T provider, ServicePriority priority, Plugin plugin) {

        this.service = service;
        this.plugin = plugin;
        this.provider = provider;
        this.priority = priority;
    }

    public Class<T> getService() {
        return service;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public T getProvider() {
        return provider;
    }

    public ServicePriority getPriority() {
        return priority;
    }

    public int compareTo(RegisteredServiceProvider<?> other) {
        if (priority.ordinal() == other.getPriority().ordinal()) {
            return 0;
        } else {
            return priority.ordinal() < other.getPriority().ordinal() ? 1 : -1;
        }
    }
}
