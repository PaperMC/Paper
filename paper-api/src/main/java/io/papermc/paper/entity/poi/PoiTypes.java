package io.papermc.paper.entity.poi;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;

/**
 * All the built-in point of interest types.
 */
@NullMarked
public final class PoiTypes {

    // Start generate - PoiTypes
    public static final PoiType ARMORER = get("armorer");

    public static final PoiType BEE_NEST = get("bee_nest");

    public static final PoiType BEEHIVE = get("beehive");

    public static final PoiType BUTCHER = get("butcher");

    public static final PoiType CARTOGRAPHER = get("cartographer");

    public static final PoiType CLERIC = get("cleric");

    public static final PoiType FARMER = get("farmer");

    public static final PoiType FISHERMAN = get("fisherman");

    public static final PoiType FLETCHER = get("fletcher");

    public static final PoiType HOME = get("home");

    public static final PoiType LEATHERWORKER = get("leatherworker");

    public static final PoiType LIBRARIAN = get("librarian");

    public static final PoiType LIGHTNING_ROD = get("lightning_rod");

    public static final PoiType LODESTONE = get("lodestone");

    public static final PoiType MASON = get("mason");

    public static final PoiType MEETING = get("meeting");

    public static final PoiType NETHER_PORTAL = get("nether_portal");

    public static final PoiType SHEPHERD = get("shepherd");

    public static final PoiType TEST_INSTANCE = get("test_instance");

    public static final PoiType TOOLSMITH = get("toolsmith");

    public static final PoiType WEAPONSMITH = get("weaponsmith");
    // End generate - PoiTypes

    private static PoiType get(@KeyPattern.Value final String key) {
        return Registry.POINT_OF_INTEREST_TYPE.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    private PoiTypes() {
    }
}
