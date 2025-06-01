package org.bukkit.entity.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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

    private static final Map<NamespacedKey, MemoryKey<?>> MEMORY_KEYS = new HashMap<>();

    // Start generate - MemoryKey
    // @GeneratedFrom 1.21.6-pre1
    public static final MemoryKey<Boolean> ADMIRING_DISABLED = new MemoryKey<>(NamespacedKey.minecraft("admiring_disabled"), Boolean.class);

    public static final MemoryKey<Boolean> ADMIRING_ITEM = new MemoryKey<>(NamespacedKey.minecraft("admiring_item"), Boolean.class);

    public static final MemoryKey<UUID> ANGRY_AT = new MemoryKey<>(NamespacedKey.minecraft("angry_at"), UUID.class);

    public static final MemoryKey<Boolean> ATE_RECENTLY = new MemoryKey<>(NamespacedKey.minecraft("ate_recently"), Boolean.class);

    public static final MemoryKey<Boolean> ATTACK_COOLING_DOWN = new MemoryKey<>(NamespacedKey.minecraft("attack_cooling_down"), Boolean.class);

    public static final MemoryKey<Long> CANT_REACH_WALK_TARGET_SINCE = new MemoryKey<>(NamespacedKey.minecraft("cant_reach_walk_target_since"), Long.class);

    public static final MemoryKey<Boolean> DANCING = new MemoryKey<>(NamespacedKey.minecraft("dancing"), Boolean.class);

    public static final MemoryKey<Boolean> DANGER_DETECTED_RECENTLY = new MemoryKey<>(NamespacedKey.minecraft("danger_detected_recently"), Boolean.class);

    public static final MemoryKey<Boolean> DISABLE_WALK_TO_ADMIRE_ITEM = new MemoryKey<>(NamespacedKey.minecraft("disable_walk_to_admire_item"), Boolean.class);

    public static final MemoryKey<Integer> GAZE_COOLDOWN_TICKS = new MemoryKey<>(NamespacedKey.minecraft("gaze_cooldown_ticks"), Integer.class);

    public static final MemoryKey<Boolean> GOLEM_DETECTED_RECENTLY = new MemoryKey<>(NamespacedKey.minecraft("golem_detected_recently"), Boolean.class);

    public static final MemoryKey<Boolean> HAS_HUNTING_COOLDOWN = new MemoryKey<>(NamespacedKey.minecraft("has_hunting_cooldown"), Boolean.class);

    public static final MemoryKey<Long> HEARD_BELL_TIME = new MemoryKey<>(NamespacedKey.minecraft("heard_bell_time"), Long.class);

    public static final MemoryKey<Location> HIDING_PLACE = new MemoryKey<>(NamespacedKey.minecraft("hiding_place"), Location.class);

    public static final MemoryKey<Location> HOME = new MemoryKey<>(NamespacedKey.minecraft("home"), Location.class);

    public static final MemoryKey<Boolean> HUNTED_RECENTLY = new MemoryKey<>(NamespacedKey.minecraft("hunted_recently"), Boolean.class);

    public static final MemoryKey<Boolean> IS_PANICKING = new MemoryKey<>(NamespacedKey.minecraft("is_panicking"), Boolean.class);

    public static final MemoryKey<Boolean> IS_TEMPTED = new MemoryKey<>(NamespacedKey.minecraft("is_tempted"), Boolean.class);

    public static final MemoryKey<Integer> ITEM_PICKUP_COOLDOWN_TICKS = new MemoryKey<>(NamespacedKey.minecraft("item_pickup_cooldown_ticks"), Integer.class);

    public static final MemoryKey<Location> JOB_SITE = new MemoryKey<>(NamespacedKey.minecraft("job_site"), Location.class);

    public static final MemoryKey<Long> LAST_SLEPT = new MemoryKey<>(NamespacedKey.minecraft("last_slept"), Long.class);

    public static final MemoryKey<Long> LAST_WOKEN = new MemoryKey<>(NamespacedKey.minecraft("last_woken"), Long.class);

    public static final MemoryKey<Long> LAST_WORKED_AT_POI = new MemoryKey<>(NamespacedKey.minecraft("last_worked_at_poi"), Long.class);

    public static final MemoryKey<Location> LIKED_NOTEBLOCK_POSITION = new MemoryKey<>(NamespacedKey.minecraft("liked_noteblock"), Location.class);

    public static final MemoryKey<Integer> LIKED_NOTEBLOCK_COOLDOWN_TICKS = new MemoryKey<>(NamespacedKey.minecraft("liked_noteblock_cooldown_ticks"), Integer.class);

    public static final MemoryKey<UUID> LIKED_PLAYER = new MemoryKey<>(NamespacedKey.minecraft("liked_player"), UUID.class);

    public static final MemoryKey<Integer> LONG_JUMP_COOLING_DOWN = new MemoryKey<>(NamespacedKey.minecraft("long_jump_cooling_down"), Integer.class);

    public static final MemoryKey<Boolean> LONG_JUMP_MID_JUMP = new MemoryKey<>(NamespacedKey.minecraft("long_jump_mid_jump"), Boolean.class);

    public static final MemoryKey<Location> MEETING_POINT = new MemoryKey<>(NamespacedKey.minecraft("meeting_point"), Location.class);

    public static final MemoryKey<Boolean> PACIFIED = new MemoryKey<>(NamespacedKey.minecraft("pacified"), Boolean.class);

    public static final MemoryKey<Integer> PLAY_DEAD_TICKS = new MemoryKey<>(NamespacedKey.minecraft("play_dead_ticks"), Integer.class);

    public static final MemoryKey<Location> POTENTIAL_JOB_SITE = new MemoryKey<>(NamespacedKey.minecraft("potential_job_site"), Location.class);

    public static final MemoryKey<Integer> RAM_COOLDOWN_TICKS = new MemoryKey<>(NamespacedKey.minecraft("ram_cooldown_ticks"), Integer.class);

    public static final MemoryKey<Boolean> SNIFFER_DIGGING = new MemoryKey<>(NamespacedKey.minecraft("sniffer_digging"), Boolean.class);

    public static final MemoryKey<Boolean> SNIFFER_HAPPY = new MemoryKey<>(NamespacedKey.minecraft("sniffer_happy"), Boolean.class);

    public static final MemoryKey<Integer> TEMPTATION_COOLDOWN_TICKS = new MemoryKey<>(NamespacedKey.minecraft("temptation_cooldown_ticks"), Integer.class);

    public static final MemoryKey<Integer> TIME_TRYING_TO_REACH_ADMIRE_ITEM = new MemoryKey<>(NamespacedKey.minecraft("time_trying_to_reach_admire_item"), Integer.class);

    public static final MemoryKey<Boolean> UNIVERSAL_ANGER = new MemoryKey<>(NamespacedKey.minecraft("universal_anger"), Boolean.class);

    public static final MemoryKey<Integer> VISIBLE_ADULT_HOGLIN_COUNT = new MemoryKey<>(NamespacedKey.minecraft("visible_adult_hoglin_count"), Integer.class);

    public static final MemoryKey<Integer> VISIBLE_ADULT_PIGLIN_COUNT = new MemoryKey<>(NamespacedKey.minecraft("visible_adult_piglin_count"), Integer.class);
    // End generate - MemoryKey
    /**
     * @deprecated this constant uses the wrong generic type, the sniffer now stores different positions
     * from possibly different worlds. Use the relevant methods in {@link org.bukkit.entity.Sniffer} directly
     * for now.
     */
    @Deprecated // Paper
    public static final MemoryKey<Location> SNIFFER_EXPLORED_POSITIONS = new MemoryKey<>(NamespacedKey.minecraft("sniffer_explored_positions"), Location.class);

    /**
     * Returns a {@link MemoryKey} by a {@link NamespacedKey}.
     *
     * @param namespacedKey the {@link NamespacedKey} referencing a
     * {@link MemoryKey}
     * @return the {@link MemoryKey} or null when no {@link MemoryKey} is
     * available under that key
     */
    @Nullable
    public static MemoryKey<?> getByKey(@NotNull NamespacedKey namespacedKey) {
        return MEMORY_KEYS.get(namespacedKey);
    }

    /**
     * Returns the set of all MemoryKeys.
     *
     * @return the memoryKeys
     */
    @NotNull
    public static Set<MemoryKey<?>> values() {
        return new HashSet<>(MEMORY_KEYS.values());
    }
}
