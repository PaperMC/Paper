package org.bukkit;

/**
 * A list of effects that the server is able to send to players.
 */
public enum Effect {
    BOW_FIRE(1002),
    CLICK1(1001),
    CLICK2(1000),
    DOOR_TOGGLE(1003),
    EXTINGUISH(1004),
    RECORD_PLAY(1005),
    SMOKE(2000),
    STEP_SOUND(2001);

    private final int id;

    Effect(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
