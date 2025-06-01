package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Wolf;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#WOLF_VARIANT}.
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
public final class WolfVariantKeys {
    /**
     * {@code minecraft:ashen}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> ASHEN = create(key("ashen"));

    /**
     * {@code minecraft:black}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> BLACK = create(key("black"));

    /**
     * {@code minecraft:chestnut}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> CHESTNUT = create(key("chestnut"));

    /**
     * {@code minecraft:pale}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> PALE = create(key("pale"));

    /**
     * {@code minecraft:rusty}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> RUSTY = create(key("rusty"));

    /**
     * {@code minecraft:snowy}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> SNOWY = create(key("snowy"));

    /**
     * {@code minecraft:spotted}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> SPOTTED = create(key("spotted"));

    /**
     * {@code minecraft:striped}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> STRIPED = create(key("striped"));

    /**
     * {@code minecraft:woods}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Wolf.Variant> WOODS = create(key("woods"));

    private WolfVariantKeys() {
    }

    /**
     * Creates a typed key for {@link Wolf.Variant} in the registry {@code minecraft:wolf_variant}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<Wolf.Variant> create(final Key key) {
        return TypedKey.create(RegistryKey.WOLF_VARIANT, key);
    }
}
