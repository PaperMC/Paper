package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

import org.bukkit.block.BlockFace;
import org.bukkit.potion.Potion;

/**
 * A list of effects that the server is able to send to players.
 */
public enum Effect {
    /**
     * An alternate click sound.
     */
    CLICK2(1000, Type.SOUND),
    /**
     * A click sound.
     */
    CLICK1(1001, Type.SOUND),
    /**
     * Sound of a bow firing.
     */
    BOW_FIRE(1002, Type.SOUND),
    /**
     * Sound of a door opening/closing.
     */
    DOOR_TOGGLE(1003, Type.SOUND),
    /**
     * Sound of fire being extinguished.
     */
    EXTINGUISH(1004, Type.SOUND),
    /**
     * A song from a record. Needs the record item ID as additional info
     */
    RECORD_PLAY(1005, Type.SOUND, Material.class),
    /**
     * Sound of ghast shrieking.
     */
    GHAST_SHRIEK(1007, Type.SOUND),
    /**
     * Sound of ghast firing.
     */
    GHAST_SHOOT(1008, Type.SOUND),
    /**
     * Sound of blaze firing.
     */
    BLAZE_SHOOT(1009, Type.SOUND),
    /**
     * Sound of zombies chewing on wooden doors.
     */
    ZOMBIE_CHEW_WOODEN_DOOR(1010, Type.SOUND),
    /**
     * Sound of zombies chewing on iron doors.
     */
    ZOMBIE_CHEW_IRON_DOOR(1011, Type.SOUND),
    /**
     * Sound of zombies destroying a door.
     */
    ZOMBIE_DESTROY_DOOR(1012, Type.SOUND),
    /**
     * A visual smoke effect. Needs direction as additional info.
     */
    SMOKE(2000, Type.VISUAL, BlockFace.class),
    /**
     * Sound of a block breaking. Needs block ID as additional info.
     */
    STEP_SOUND(2001, Type.SOUND, Material.class),
    /**
     * Visual effect of a splash potion breaking. Needs potion data value as additional info.
     */
    POTION_BREAK(2002, Type.VISUAL, Potion.class),
    /**
     * An ender eye signal; a visual effect.
     */
    ENDER_SIGNAL(2003, Type.VISUAL),
    /**
     * The flames seen on a mobspawner; a visual effect.
     */
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
