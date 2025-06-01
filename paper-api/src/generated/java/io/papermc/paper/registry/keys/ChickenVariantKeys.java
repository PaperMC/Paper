package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#CHICKEN_VARIANT}.
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
public final class ChickenVariantKeys {
    /**
     * {@code minecraft:cold}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Chicken.Variant> COLD = create(key("cold"));

    /**
     * {@code minecraft:temperate}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Chicken.Variant> TEMPERATE = create(key("temperate"));

    /**
     * {@code minecraft:warm}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Chicken.Variant> WARM = create(key("warm"));

    private ChickenVariantKeys() {
    }

    /**
     * Creates a typed key for {@link Chicken.Variant} in the registry {@code minecraft:chicken_variant}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<Chicken.Variant> create(final Key key) {
        return TypedKey.create(RegistryKey.CHICKEN_VARIANT, key);
    }
}
