package io.papermc.paper.plugin.entrypoint.classloader.group;

import io.papermc.paper.plugin.provider.classloader.ClassLoaderAccess;
import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader;
import io.papermc.paper.plugin.provider.classloader.PluginClassLoaderGroup;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ApiStatus.Internal
public class LockingClassLoaderGroup implements PluginClassLoaderGroup {

    private final PluginClassLoaderGroup parent;
    private final Map<String, ClassLockEntry> classLoadLock = new HashMap<>();

    public LockingClassLoaderGroup(PluginClassLoaderGroup parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable Class<?> getClassByName(String name, boolean resolve, ConfiguredPluginClassLoader requester) {
        // make MT safe
        ClassLockEntry lock;
        synchronized (this.classLoadLock) {
            lock = this.classLoadLock.computeIfAbsent(name, (x) -> new ClassLockEntry(new AtomicInteger(0), new java.util.concurrent.locks.ReentrantReadWriteLock()));
            lock.count.incrementAndGet();
        }
        lock.reentrantReadWriteLock.writeLock().lock();
        try {
            return parent.getClassByName(name, resolve, requester);
        } finally {
            synchronized (this.classLoadLock) {
                lock.reentrantReadWriteLock.writeLock().unlock();
                if (lock.count.get() == 1) {
                    this.classLoadLock.remove(name);
                } else {
                    lock.count.decrementAndGet();
                }
            }
        }
    }

    @Override
    public void remove(ConfiguredPluginClassLoader configuredPluginClassLoader) {
        this.parent.remove(configuredPluginClassLoader);
    }

    @Override
    public void add(ConfiguredPluginClassLoader configuredPluginClassLoader) {
        this.parent.add(configuredPluginClassLoader);
    }

    @Override
    public ClassLoaderAccess getAccess() {
        return this.parent.getAccess();
    }

    public PluginClassLoaderGroup getParent() {
        return parent;
    }

    record ClassLockEntry(AtomicInteger count, ReentrantReadWriteLock reentrantReadWriteLock) {
    }

    @Override
    public String toString() {
        return "LockingClassLoaderGroup{" +
            "parent=" + this.parent +
            ", classLoadLock=" + this.classLoadLock +
            '}';
    }
}
