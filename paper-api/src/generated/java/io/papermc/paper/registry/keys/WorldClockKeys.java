package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.WorldClock;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#WORLD_CLOCK}.
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
public final class WorldClockKeys {
    /**
     * {@code minecraft:overworld}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldClock> OVERWORLD = create(key("overworld"));

    /**
     * {@code minecraft:the_end}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<WorldClock> THE_END = create(key("the_end"));

    private WorldClockKeys() {
    }

    /**
     * Creates a typed key for {@link WorldClock} in the registry {@code minecraft:world_clock}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<WorldClock> create(final Key key) {
        return TypedKey.create(RegistryKey.WORLD_CLOCK, key);
    }
}
