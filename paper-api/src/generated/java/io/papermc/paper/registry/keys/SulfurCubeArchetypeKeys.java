package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.SulfurCube;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#SULFUR_CUBE_ARCHETYPE}.
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
public final class SulfurCubeArchetypeKeys {
    /**
     * {@code minecraft:bouncy}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> BOUNCY = create(key("bouncy"));

    /**
     * {@code minecraft:explosive}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> EXPLOSIVE = create(key("explosive"));

    /**
     * {@code minecraft:fast_flat}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> FAST_FLAT = create(key("fast_flat"));

    /**
     * {@code minecraft:fast_sliding}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> FAST_SLIDING = create(key("fast_sliding"));

    /**
     * {@code minecraft:high_resistance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> HIGH_RESISTANCE = create(key("high_resistance"));

    /**
     * {@code minecraft:hot}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> HOT = create(key("hot"));

    /**
     * {@code minecraft:light}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> LIGHT = create(key("light"));

    /**
     * {@code minecraft:regular}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> REGULAR = create(key("regular"));

    /**
     * {@code minecraft:slow_bouncy}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> SLOW_BOUNCY = create(key("slow_bouncy"));

    /**
     * {@code minecraft:slow_flat}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> SLOW_FLAT = create(key("slow_flat"));

    /**
     * {@code minecraft:slow_sliding}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> SLOW_SLIDING = create(key("slow_sliding"));

    /**
     * {@code minecraft:sticky}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<SulfurCube.Archetype> STICKY = create(key("sticky"));

    private SulfurCubeArchetypeKeys() {
    }

    /**
     * Creates a typed key for {@link SulfurCube.Archetype} in the registry {@code minecraft:sulfur_cube_archetype}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<SulfurCube.Archetype> create(final Key key) {
        return TypedKey.create(RegistryKey.SULFUR_CUBE_ARCHETYPE, key);
    }
}
