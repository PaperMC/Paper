package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#TRIM_PATTERN}.
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
public final class TrimPatternKeys {
    /**
     * {@code minecraft:bolt}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> BOLT = create(key("bolt"));

    /**
     * {@code minecraft:coast}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> COAST = create(key("coast"));

    /**
     * {@code minecraft:dune}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> DUNE = create(key("dune"));

    /**
     * {@code minecraft:eye}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> EYE = create(key("eye"));

    /**
     * {@code minecraft:flow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> FLOW = create(key("flow"));

    /**
     * {@code minecraft:host}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> HOST = create(key("host"));

    /**
     * {@code minecraft:raiser}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> RAISER = create(key("raiser"));

    /**
     * {@code minecraft:rib}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> RIB = create(key("rib"));

    /**
     * {@code minecraft:sentry}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> SENTRY = create(key("sentry"));

    /**
     * {@code minecraft:shaper}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> SHAPER = create(key("shaper"));

    /**
     * {@code minecraft:silence}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> SILENCE = create(key("silence"));

    /**
     * {@code minecraft:snout}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> SNOUT = create(key("snout"));

    /**
     * {@code minecraft:spire}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> SPIRE = create(key("spire"));

    /**
     * {@code minecraft:tide}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> TIDE = create(key("tide"));

    /**
     * {@code minecraft:vex}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> VEX = create(key("vex"));

    /**
     * {@code minecraft:ward}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> WARD = create(key("ward"));

    /**
     * {@code minecraft:wayfinder}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> WAYFINDER = create(key("wayfinder"));

    /**
     * {@code minecraft:wild}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<TrimPattern> WILD = create(key("wild"));

    private TrimPatternKeys() {
    }

    /**
     * Creates a typed key for {@link TrimPattern} in the registry {@code minecraft:trim_pattern}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<TrimPattern> create(final Key key) {
        return TypedKey.create(RegistryKey.TRIM_PATTERN, key);
    }
}
