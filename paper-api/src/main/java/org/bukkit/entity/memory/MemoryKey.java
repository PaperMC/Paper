package org.bukkit.entity.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a key used for accessing memory values of a
 * {@link org.bukkit.entity.LivingEntity}.
 *
 * @param <T> the class type of the memory value
 */
public final class MemoryKey<T> implements Keyed {

    private final NamespacedKey namespacedKey;
    private final Class<T> tClass;

    private MemoryKey(NamespacedKey namespacedKey, Class<T> tClass) {
        this.namespacedKey = namespacedKey;
        this.tClass = tClass;
        MEMORY_KEYS.put(namespacedKey, this);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return namespacedKey;
    }

    /**
     * Gets the class of values associated with this memory.
     *
     * @return the class of value objects
     */
    @NotNull
    public Class<T> getMemoryClass() {
        return tClass;
    }

    private static final Map<NamespacedKey, MemoryKey> MEMORY_KEYS = new HashMap<>();
    //
    public static final MemoryKey<Location> HOME = new MemoryKey<>(NamespacedKey.minecraft("home"), Location.class);
    public static final MemoryKey<Location> MEETING_POINT = new MemoryKey<>(NamespacedKey.minecraft("meeting_point"), Location.class);
    public static final MemoryKey<Location> JOB_SITE = new MemoryKey<>(NamespacedKey.minecraft("job_site"), Location.class);

    /**
     * Returns a {@link MemoryKey} by a {@link NamespacedKey}.
     *
     * @param namespacedKey the {@link NamespacedKey} referencing a
     * {@link MemoryKey}
     * @return the {@link MemoryKey} or null when no {@link MemoryKey} is
     * available under that key
     */
    @Nullable
    public static MemoryKey getByKey(@NotNull NamespacedKey namespacedKey) {
        return MEMORY_KEYS.get(namespacedKey);
    }

    /**
     * Returns the set of all MemoryKeys.
     *
     * @return the memoryKeys
     */
    @NotNull
    public static Set<MemoryKey> values() {
        return new HashSet<>(MEMORY_KEYS.values());
    }
}
