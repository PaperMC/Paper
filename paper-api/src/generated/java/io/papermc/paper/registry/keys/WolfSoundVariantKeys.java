package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Wolf;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#WOLF_SOUND_VARIANT}.
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
public final class WolfSoundVariantKeys {
    /**
     * {@code minecraft:angry}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.SoundVariant> ANGRY = create(key("angry"));

    /**
     * {@code minecraft:big}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.SoundVariant> BIG = create(key("big"));

    /**
     * {@code minecraft:classic}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.SoundVariant> CLASSIC = create(key("classic"));

    /**
     * {@code minecraft:cute}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.SoundVariant> CUTE = create(key("cute"));

    /**
     * {@code minecraft:grumpy}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.SoundVariant> GRUMPY = create(key("grumpy"));

    /**
     * {@code minecraft:puglin}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.SoundVariant> PUGLIN = create(key("puglin"));

    /**
     * {@code minecraft:sad}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.SoundVariant> SAD = create(key("sad"));

    private WolfSoundVariantKeys() {
    }

    /**
     * Creates a typed key for {@link Wolf.SoundVariant} in the registry {@code minecraft:wolf_sound_variant}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<Wolf.SoundVariant> create(final Key key) {
        return TypedKey.create(RegistryKey.WOLF_SOUND_VARIANT, key);
    }
}
