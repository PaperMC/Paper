package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Fluid;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#FLUID}.
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
@GeneratedFrom("1.21.6-pre1")
@ApiStatus.Experimental
public final class FluidTagKeys {
    /**
     * {@code #minecraft:lava}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Fluid> LAVA = create(key("lava"));

    /**
     * {@code #minecraft:water}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Fluid> WATER = create(key("water"));

    private FluidTagKeys() {
    }

    /**
     * Creates a tag key for {@link Fluid} in the registry {@code minecraft:fluid}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    @ApiStatus.Experimental
    public static TagKey<Fluid> create(final Key key) {
        return TagKey.create(RegistryKey.FLUID, key);
    }
}
