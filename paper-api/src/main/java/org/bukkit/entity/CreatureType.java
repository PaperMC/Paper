package org.bukkit.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CreatureType {
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

    private static final Map<String, CreatureType> mapping
            = new HashMap<String, CreatureType>();

    static {
        for (CreatureType type : EnumSet.allOf(CreatureType.class)) {
            mapping.put(type.name, type);
        }
    }

    private CreatureType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CreatureType fromName(String name) {
        return mapping.get(name);
    }
}
