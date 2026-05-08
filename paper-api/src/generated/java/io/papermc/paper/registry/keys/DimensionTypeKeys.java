package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.world.worldgen.DimensionType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#DIMENSION_TYPE}.
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
public final class DimensionTypeKeys {
    /**
     * {@code minecraft:overworld}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DimensionType> OVERWORLD = create(key("overworld"));

    /**
     * {@code minecraft:overworld_caves}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DimensionType> OVERWORLD_CAVES = create(key("overworld_caves"));

    /**
     * {@code minecraft:the_end}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DimensionType> THE_END = create(key("the_end"));

    /**
     * {@code minecraft:the_nether}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DimensionType> THE_NETHER = create(key("the_nether"));

    private DimensionTypeKeys() {
    }

    /**
     * Creates a typed key for {@link DimensionType} in the registry {@code minecraft:dimension_type}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<DimensionType> create(final Key key) {
        return TypedKey.create(RegistryKey.DIMENSION_TYPE, key);
    }
}
