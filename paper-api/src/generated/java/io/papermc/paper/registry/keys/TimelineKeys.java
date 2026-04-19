package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.Timeline;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#TIMELINE}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedClass
public final class TimelineKeys {
    /**
     * {@code minecraft:day}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Timeline> DAY = create(key("day"));

    /**
     * {@code minecraft:early_game}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Timeline> EARLY_GAME = create(key("early_game"));

    /**
     * {@code minecraft:moon}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Timeline> MOON = create(key("moon"));

    /**
     * {@code minecraft:villager_schedule}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Timeline> VILLAGER_SCHEDULE = create(key("villager_schedule"));

    private TimelineKeys() {
    }

    /**
     * Creates a typed key for {@link Timeline} in the registry {@code minecraft:timeline}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<Timeline> create(final Key key) {
        return TypedKey.create(RegistryKey.TIMELINE, key);
    }
}
