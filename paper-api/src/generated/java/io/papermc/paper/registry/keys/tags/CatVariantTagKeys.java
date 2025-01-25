package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#CAT_VARIANT}.
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
@GeneratedFrom("1.21.4")
@NullMarked
@ApiStatus.Experimental
public final class CatVariantTagKeys {
    /**
     * {@code #minecraft:default_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Cat.Type> DEFAULT_SPAWNS = create(key("default_spawns"));

    /**
     * {@code #minecraft:full_moon_spawns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Cat.Type> FULL_MOON_SPAWNS = create(key("full_moon_spawns"));

    private CatVariantTagKeys() {
    }

    /**
     * Creates a tag key for {@link Cat.Type} in the registry {@code minecraft:cat_variant}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<Cat.Type> create(final Key key) {
        return TagKey.create(RegistryKey.CAT_VARIANT, key);
    }
}
