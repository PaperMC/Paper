package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Frog;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#FROG_VARIANT}.
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
@GeneratedFrom("1.21.3")
@NullMarked
@ApiStatus.Experimental
public final class FrogVariantKeys {
    /**
     * {@code minecraft:cold}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Frog.Variant> COLD = create(key("cold"));

    /**
     * {@code minecraft:temperate}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Frog.Variant> TEMPERATE = create(key("temperate"));

    /**
     * {@code minecraft:warm}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Frog.Variant> WARM = create(key("warm"));

    private FrogVariantKeys() {
    }

    private static TypedKey<Frog.Variant> create(final Key key) {
        return TypedKey.create(RegistryKey.FROG_VARIANT, key);
    }
}
