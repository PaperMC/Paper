package org.bukkit;

/**
 * A list of all Effects that can happen to entities.
 */
public enum EntityEffect {

    /**
     * When mobs get hurt.
     */
    HURT((byte) 2),

    /**
     * When a mob dies.
     * <p>
     * <b>This will cause client-glitches!
     */
    DEATH((byte) 3),

    /**
     * The smoke when taming a wolf fails.
     * <p>
     * Without client-mods this will be ignored if the entity is not a wolf.
     */
    WOLF_SMOKE((byte) 6),

    /**
     * The hearts when taming a wolf succeeds.
     * <p>
     * Without client-mods this will be ignored if the entity is not a wolf.
     */
    WOLF_HEARTS((byte) 7),

    /**
     * When a wolf shakes (after being wet).
     * <p>
     * Without client-mods this will be ignored if the entity is not a wolf.
     */
    WOLF_SHAKE((byte) 8);

    private final byte data;

    EntityEffect(byte data) {
        this.data = data;
    }

    /**
     * @return The data-value that is sent to the client to play this effect.
     */
    public byte getData() {
        return data;
    }
}
