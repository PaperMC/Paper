package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * A list of effects that the server is able to send to players.
 */
public enum Effect {
    CLICK2(1000),
    CLICK1(1001),
    BOW_FIRE(1002),
    DOOR_TOGGLE(1003),
    EXTINGUISH(1004),
    RECORD_PLAY(1005),
    GHAST_SHRIEK(1007),
    GHAST_SHOOT(1008),
    BLAZE_SHOOT(1009),
    SMOKE(2000),
    STEP_SOUND(2001),
    POTION_BREAK(2002),
    ENDER_SIGNAL(2003),
    MOBSPAWNER_FLAMES(2004);

    private final int id;
    private static final Map<Integer, Effect> BY_ID = Maps.newHashMap();

    Effect(int id) {
        this.id = id;
    }

    /**
     * Gets the ID for this effect.
     *
     * @return ID of this effect
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the Effect associated with the given ID.
     *
     * @param id ID of the Effect to return
     * @return Effect with the given ID
     */
    public static Effect getById(int id) {
        return BY_ID.get(id);
    }

    static {
        for (Effect effect : values()) {
            BY_ID.put(effect.id, effect);
        }
    }
}
