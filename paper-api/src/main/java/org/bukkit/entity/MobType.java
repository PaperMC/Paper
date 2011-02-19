package org.bukkit.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
/**
 * @deprecated Should be using CreatureType
 *
 */
public enum MobType {
    CHICKEN("Chicken"),
    COW("Cow"),
    CREEPER("Creeper"),
    GHAST("Ghast"),
    PIG("Pig"),
    PIG_ZOMBIE("PigZombie"),
    SHEEP("Sheep"),
    SKELETON("Skeleton"),
    SPIDER("Spider"),
    ZOMBIE("Zombie"),
    SQUID("Squid"),
    SLIME("Slime");

    private String name;

    private static final Map<String, MobType> mapping
            = new HashMap<String, MobType>();

    static {
        for (MobType type : EnumSet.allOf(MobType.class)) {
            mapping.put(type.name, type);
        }
    }

    private MobType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MobType fromName(String name) {
        return mapping.get(name);
    }
}
