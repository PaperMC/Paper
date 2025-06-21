package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.FluidTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Fluid;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#FLUID}.
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
public final class FluidTags {
    /**
     * {@code #minecraft:lava}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Fluid> LAVA = fetch(FluidTagKeys.LAVA);

    /**
     * {@code #minecraft:water}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<Fluid> WATER = fetch(FluidTagKeys.WATER);

    private FluidTags() {
    }

    private static Tag<Fluid> fetch(final TagKey<Fluid> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.FLUID).getTag(tagKey);
    }
}
