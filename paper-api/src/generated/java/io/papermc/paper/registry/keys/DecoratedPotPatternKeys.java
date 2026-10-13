package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.block.pot.PotPatternType;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#DECORATED_POT_PATTERN}.
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
public final class DecoratedPotPatternKeys {
    /**
     * {@code minecraft:angler}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> ANGLER = create(key("angler"));

    /**
     * {@code minecraft:archer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> ARCHER = create(key("archer"));

    /**
     * {@code minecraft:arms_up}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> ARMS_UP = create(key("arms_up"));

    /**
     * {@code minecraft:blade}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> BLADE = create(key("blade"));

    /**
     * {@code minecraft:brewer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> BREWER = create(key("brewer"));

    /**
     * {@code minecraft:burn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> BURN = create(key("burn"));

    /**
     * {@code minecraft:danger}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> DANGER = create(key("danger"));

    /**
     * {@code minecraft:explorer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> EXPLORER = create(key("explorer"));

    /**
     * {@code minecraft:flow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> FLOW = create(key("flow"));

    /**
     * {@code minecraft:friend}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> FRIEND = create(key("friend"));

    /**
     * {@code minecraft:guster}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> GUSTER = create(key("guster"));

    /**
     * {@code minecraft:heart}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> HEART = create(key("heart"));

    /**
     * {@code minecraft:heartbreak}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> HEARTBREAK = create(key("heartbreak"));

    /**
     * {@code minecraft:howl}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> HOWL = create(key("howl"));

    /**
     * {@code minecraft:miner}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> MINER = create(key("miner"));

    /**
     * {@code minecraft:mourner}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> MOURNER = create(key("mourner"));

    /**
     * {@code minecraft:plenty}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> PLENTY = create(key("plenty"));

    /**
     * {@code minecraft:prize}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> PRIZE = create(key("prize"));

    /**
     * {@code minecraft:scrape}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> SCRAPE = create(key("scrape"));

    /**
     * {@code minecraft:sheaf}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> SHEAF = create(key("sheaf"));

    /**
     * {@code minecraft:shelter}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> SHELTER = create(key("shelter"));

    /**
     * {@code minecraft:skull}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> SKULL = create(key("skull"));

    /**
     * {@code minecraft:snort}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotPatternType> SNORT = create(key("snort"));

    private DecoratedPotPatternKeys() {
    }

    /**
     * Creates a typed key for {@link PotPatternType} in the registry {@code minecraft:decorated_pot_pattern}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<PotPatternType> create(final Key key) {
        return TypedKey.create(RegistryKey.DECORATED_POT_PATTERN, key);
    }
}
