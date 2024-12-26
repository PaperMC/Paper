package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.worldgen.LevelStem;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#LEVEL_STEM}.
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
@GeneratedFrom("1.21.6")
@ApiStatus.Experimental
public final class LevelStemKeys {
    private LevelStemKeys() {
    }

    /**
     * Creates a typed key for {@link LevelStem} in the registry {@code minecraft:dimension}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    @ApiStatus.Experimental
    public static TypedKey<LevelStem> create(final Key key) {
        return TypedKey.create(RegistryKey.LEVEL_STEM, key);
    }
}
