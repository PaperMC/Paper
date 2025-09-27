package io.papermc.paper.profile;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.CachedUserNameToIdResolver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

/**
 * This cache allows maintaining the pre-1.21.9 behavior of profile resolving APIs
 * in a more controlled manner (responsibility is properly separated from {@link CachedUserNameToIdResolver}).
 * Vanilla lookups populate this cache, but do not read from it to maintain their
 * vanilla behavior.
 */
@NullMarked
public final class PaperFilledProfileCache {
    private static final Logger LOGGER = LogUtils.getClassLogger();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, GameProfile> byName = new HashMap<>();
    private final Map<UUID, GameProfile> byId = new HashMap<>();
    private final Map<UUID, Long> lastOperation = new ConcurrentHashMap<>();
    private final AtomicLong operationCount = new AtomicLong(0);
    private final ScheduledExecutorService cleanupExecutor;

    public PaperFilledProfileCache() {
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(
            Thread.ofPlatform()
                .name(this.getClass().getSimpleName() + "-cleanup-", 0)
                .daemon(true)
                .uncaughtExceptionHandler((thread, throwable) ->
                    LOGGER.warn("Uncaught exception in thread {}", thread.getName(), throwable))
                .factory()
        );
        this.cleanupExecutor.scheduleAtFixedRate(
            () -> {
                if (this.tryCancelCleanup()) {
                    return;
                }
                this.performCleanup();
            },
            15L,
            15L,
            TimeUnit.SECONDS
        );
    }

    public void updateIfCached(final GameProfile profile) {
        if (this.getIfCached(profile.id()) != null) {
            this.add(profile);
        }
    }

    public void add(final GameProfile profile) {
        try {
            this.lock.writeLock().lock();
            this.byName.put(profile.name(), profile);
            this.byId.put(profile.id(), profile);
            this.lastOperation.put(profile.id(), this.operationCount.getAndIncrement());
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public @Nullable GameProfile getIfCached(final UUID uuid) {
        try {
            this.lock.readLock().lock();
            final GameProfile profile = this.byId.get(uuid);
            if (profile != null) {
                this.lastOperation.put(uuid, this.operationCount.getAndIncrement());
            }
            return profile;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public @Nullable GameProfile getIfCached(final String name) {
        try {
            this.lock.readLock().lock();
            final GameProfile profile = this.byName.get(name);
            if (profile != null) {
                this.lastOperation.put(profile.id(), this.operationCount.getAndIncrement());
            }
            return profile;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    private int maxSize() {
        return org.spigotmc.SpigotConfig.userCacheCap;
    }

    private boolean tryCancelCleanup() {
        final MinecraftServer server = MinecraftServer.getServer();
        if (server != null && (!server.isRunning() || server.hasStopped())) {
            this.cleanupExecutor.shutdown();
            return true;
        }
        return false;
    }

    private void performCleanup() {
        final int maxSize = this.maxSize();
        if (this.lastOperation.size() <= maxSize) {
            return;
        }
        try {
            this.lock.writeLock().lock();
            final List<UUID> list = this.lastOperation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
            final Iterator<UUID> iterator = list.iterator();
            while (iterator.hasNext() && this.lastOperation.size() > maxSize) {
                final UUID uuid = iterator.next();
                this.lastOperation.remove(uuid);
                final GameProfile profile = this.byId.remove(uuid);
                this.byName.remove(profile.name());
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
