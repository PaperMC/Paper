package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents various types of worlds that may exist
 */
public enum WorldType {
    NORMAL("DEFAULT"),
    FLAT("FLAT");

    private final static Map<String, WorldType> lookup = new HashMap<String, WorldType>();
    private final String name;

    static {
        for (WorldType type : values()) {
            lookup.put(type.name, type);
        }
    }

    private WorldType(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this WorldType
     *
     * @return Name of this type
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a Worldtype by its name
     *
     * @param name Name of the WorldType to get
     * @return Requested WorldType, or null if not found
     */
    public static WorldType getByName(String name) {
        return lookup.get(name.toUpperCase());
    }
}
