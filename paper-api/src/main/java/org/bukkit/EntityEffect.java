package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * A list of all Effects that can happen to entities.
 */
public enum EntityEffect {

    /**
     * When mobs get hurt.
     */
    HURT(2),

    /**
     * When a mob dies.
     * <p />
     * <b>This will cause client-glitches!
     */
    DEATH(3),

    /**
     * The smoke when taming a wolf fails.
     * <p />
     * Without client-mods this will be ignored if the entity is not a wolf.
     */
    WOLF_SMOKE(6),

    /**
     * The hearts when taming a wolf succeeds.
     * <p />
     * Without client-mods this will be ignored if the entity is not a wolf.
     */
    WOLF_HEARTS(7),

    /**
     * When a wolf shakes (after being wet).
     * <p />
     * Without client-mods this will be ignored if the entity is not a wolf.
     */
    WOLF_SHAKE(8),

    /**
     * When a sheep eats a LONG_GRASS block.
     */
    SHEEP_EAT(10);

    private final byte data;
    private final static Map<Byte, EntityEffect> BY_DATA = Maps.newHashMap();

    EntityEffect(final int data) {
        this.data = (byte) data;
    }

    /**
     * Gets the data value of this EntityEffect
     *
     * @return The data value
     */
    public byte getData() {
        return data;
    }

    /**
     * Gets the EntityEffect with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link EntityEffect} representing the given value, or null if it doesn't exist
     */
    public static EntityEffect getByData(final byte data) {
        return BY_DATA.get(data);
    }


    static {
        for (EntityEffect entityEffect : values()) {
            BY_DATA.put(entityEffect.data, entityEffect);
        }
    }
}
