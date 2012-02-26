package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

import org.bukkit.block.BlockFace;
import org.bukkit.potion.Potion;

/**
 * A list of effects that the server is able to send to players.
 */
public enum Effect {
    CLICK2(1000, Type.SOUND),
    CLICK1(1001, Type.SOUND),
    BOW_FIRE(1002, Type.SOUND),
    DOOR_TOGGLE(1003, Type.SOUND),
    EXTINGUISH(1004, Type.SOUND),
    RECORD_PLAY(1005, Type.SOUND, Material.class),
    GHAST_SHRIEK(1007, Type.SOUND),
    GHAST_SHOOT(1008, Type.SOUND),
    BLAZE_SHOOT(1009, Type.SOUND),
    SMOKE(2000, Type.VISUAL, BlockFace.class),
    STEP_SOUND(2001, Type.SOUND, Material.class),
    POTION_BREAK(2002, Type.VISUAL, Potion.class),
    ENDER_SIGNAL(2003, Type.VISUAL),
    MOBSPAWNER_FLAMES(2004, Type.VISUAL);

    private final int id;
    private final Type type;
    private final Class<?> data;
    private static final Map<Integer, Effect> BY_ID = Maps.newHashMap();

    Effect(int id, Type type) {
        this(id,type,null);
    }

    Effect(int id, Type type, Class<?> data) {
        this.id = id;
        this.type = type;
        this.data = data;
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
     * @return The type of the effect.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * @return The class which represents data for this effect, or null if none
     */
    public Class<?> getData() {
        return this.data;
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

    /**
     * Represents the type of an effect.
     */
    public enum Type {SOUND, VISUAL}
}
