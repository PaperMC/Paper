package org.bukkit;

/**
 * A list of sounds that the server is able to send to players.
 */
public enum Sound {
    BOW_FIRE(1002),
    CLICK1(1001),
    CLICK2(1000),
    DOOR_SOUND(1003),
    EXTINGUISH(1004),
    RECORD_PLAY(1005),
    SMOKE(2000),
    STEP_SOUND(2001);
    private final int soundIdentifier;

    Sound(int soundIdentifier) {
            this.soundIdentifier = soundIdentifier;
    }

    public int getSoundIdentifier() {
            return this.soundIdentifier;
    }
}

