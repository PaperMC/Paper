package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.entity.poi.PoiType;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#POINT_OF_INTEREST_TYPE}.
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
public final class PoiTypeKeys {
    /**
     * {@code minecraft:armorer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> ARMORER = create(key("armorer"));

    /**
     * {@code minecraft:bee_nest}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> BEE_NEST = create(key("bee_nest"));

    /**
     * {@code minecraft:beehive}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> BEEHIVE = create(key("beehive"));

    /**
     * {@code minecraft:butcher}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> BUTCHER = create(key("butcher"));

    /**
     * {@code minecraft:cartographer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> CARTOGRAPHER = create(key("cartographer"));

    /**
     * {@code minecraft:cleric}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> CLERIC = create(key("cleric"));

    /**
     * {@code minecraft:farmer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> FARMER = create(key("farmer"));

    /**
     * {@code minecraft:fisherman}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> FISHERMAN = create(key("fisherman"));

    /**
     * {@code minecraft:fletcher}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> FLETCHER = create(key("fletcher"));

    /**
     * {@code minecraft:home}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> HOME = create(key("home"));

    /**
     * {@code minecraft:leatherworker}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> LEATHERWORKER = create(key("leatherworker"));

    /**
     * {@code minecraft:librarian}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> LIBRARIAN = create(key("librarian"));

    /**
     * {@code minecraft:lightning_rod}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> LIGHTNING_ROD = create(key("lightning_rod"));

    /**
     * {@code minecraft:lodestone}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> LODESTONE = create(key("lodestone"));

    /**
     * {@code minecraft:mason}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> MASON = create(key("mason"));

    /**
     * {@code minecraft:meeting}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> MEETING = create(key("meeting"));

    /**
     * {@code minecraft:nether_portal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> NETHER_PORTAL = create(key("nether_portal"));

    /**
     * {@code minecraft:shepherd}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> SHEPHERD = create(key("shepherd"));

    /**
     * {@code minecraft:test_instance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> TEST_INSTANCE = create(key("test_instance"));

    /**
     * {@code minecraft:toolsmith}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> TOOLSMITH = create(key("toolsmith"));

    /**
     * {@code minecraft:weaponsmith}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PoiType> WEAPONSMITH = create(key("weaponsmith"));

    private PoiTypeKeys() {
    }

    private static TypedKey<PoiType> create(final Key key) {
        return TypedKey.create(RegistryKey.POINT_OF_INTEREST_TYPE, key);
    }
}
