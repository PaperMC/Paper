package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Fluid;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#FLUID}.
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
public final class FluidKeys {
    /**
     * {@code minecraft:empty}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Fluid> EMPTY = create(key("empty"));

    /**
     * {@code minecraft:flowing_lava}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Fluid> FLOWING_LAVA = create(key("flowing_lava"));

    /**
     * {@code minecraft:flowing_water}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Fluid> FLOWING_WATER = create(key("flowing_water"));

    /**
     * {@code minecraft:lava}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Fluid> LAVA = create(key("lava"));

    /**
     * {@code minecraft:water}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Fluid> WATER = create(key("water"));

    private FluidKeys() {
    }

    private static TypedKey<Fluid> create(final Key key) {
        return TypedKey.create(RegistryKey.FLUID, key);
    }
}
