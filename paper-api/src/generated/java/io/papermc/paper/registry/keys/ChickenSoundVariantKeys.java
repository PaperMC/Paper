package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#CHICKEN_SOUND_VARIANT}.
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
public final class ChickenSoundVariantKeys {
    /**
     * {@code minecraft:classic}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Chicken.SoundVariant> CLASSIC = create(key("classic"));

    /**
     * {@code minecraft:picky}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Chicken.SoundVariant> PICKY = create(key("picky"));

    private ChickenSoundVariantKeys() {
    }

    /**
     * Creates a typed key for {@link Chicken.SoundVariant} in the registry {@code minecraft:chicken_sound_variant}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<Chicken.SoundVariant> create(final Key key) {
        return TypedKey.create(RegistryKey.CHICKEN_SOUND_VARIANT, key);
    }
}
